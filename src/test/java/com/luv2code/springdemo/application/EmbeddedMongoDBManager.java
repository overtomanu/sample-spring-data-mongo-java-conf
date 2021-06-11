package com.luv2code.springdemo.application;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import de.flapdoodle.embed.mongo.Command;
import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.Defaults;
import de.flapdoodle.embed.mongo.config.ImmutableMongodConfig;
import de.flapdoodle.embed.mongo.config.MongodConfig;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.process.config.ImmutableRuntimeConfig;
import de.flapdoodle.embed.process.config.RuntimeConfig;
import de.flapdoodle.embed.process.runtime.Network;

@Component
public class EmbeddedMongoDBManager {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EmbeddedMongoDBManager.class);

	private MongodExecutable mongodExecutable = null;

	@Autowired
	private Environment env;

	public void startEmbeddedMongoDB(String user, String password, String host,
			String port, String databaseName) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"*******->startEmbeddedMongoDB(String user={}, String password={}, String host={}, String port={}) - start", //$NON-NLS-1$
					user, password, host, port);
		}

		if (mongodExecutable != null) {
			logger.warn("Mongo db already started:" + mongodExecutable);
			return;
		}

		Command command = Command.MongoD;

		ImmutableRuntimeConfig.Builder immRCBuilder = Defaults
				.runtimeConfigFor(command)
				.artifactStore(
						Defaults.extractedArtifactStoreFor(command)
								.withDownloadConfig(Defaults
										.downloadConfigFor(command).build()))
				// set to false if stopping and starting again
				.isDaemonProcess(true);

		RuntimeConfig runtimeConfig = immRCBuilder.build();

		MongodStarter starter = MongodStarter.getInstance(runtimeConfig);

		int portNo = Integer.parseInt(port);

		ImmutableMongodConfig.Builder immutableMCBuilder = MongodConfig
				.builder().version(Version.Main.PRODUCTION)
				.net(new Net(host, portNo, Network.localhostIsIPv6()));

		MongodConfig mongodConfig = immutableMCBuilder.build();

		mongodExecutable = starter.prepare(mongodConfig);
		try {
			mongodExecutable.start();

			//@formatter:off
			// embedded server with authentication enabled not possible without 
			// running temp file mongoshell script
			//https://stackoverflow.com/questions/33624739/how-can-i-run-flapdoodle-mongodstarter-with-auth-option
			// Insert admin user
			/*
			MongoClient mongoClient = MongoClients
					.create("mongodb://localhost:27017");
			MongoDatabase database = mongoClient.getDatabase("admin");
			// A collection is equivalent to RDBMS table
			MongoCollection<Document> collection = database
					.getCollection("system.users");
			// inserting a document
			
			Document doc = Document.parse("{\n"
					+ "    user: \""+user+"\",\n"
					+ "    pwd:  \""+password+"\",\n"
					+ "    roles: [ { role: \"readWrite\", db: \""+databaseName+"\" } ]\n"
					+ "  }\n");
			//@formatter:on
			collection.insertOne(doc);
			mongoClient.close();
			mongodExecutable.stop();
			runtimeConfig = immRCBuilder.isDaemonProcess(true).build();
			mongodConfig = immutableMCBuilder
					.cmdOptions(MongoCmdOptions.builder().auth(true).build())
					.userName(user).password(password)
					.putParams("authenticationMechanisms", "SCRAM-SHA-1")
					.isConfigServer(false)
					.build();
			starter = MongodStarter.getInstance(runtimeConfig);
			mongodExecutable = starter.prepare(mongodConfig);
			mongodExecutable.start();
			*/
			//@formatter:on
		} catch (Throwable t) {
			if (mongodExecutable != null) { mongodExecutable.stop(); }
			throw t;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("startEmbeddedMongoDB exit: " + mongodExecutable);
		}
	}

	public void stopEmbeddedMongoDB() {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"*******->stopEmbeddedMongoDB() - start mongodExecutable:"
					+ mongodExecutable);
		}

		if (mongodExecutable != null) {
			mongodExecutable.stop();
			mongodExecutable = null;
		}

		if (logger.isDebugEnabled()) {
			logger.debug("stopEmbeddedMongoDB() - end");
		}
	}

	@EventListener
	// should be executed before seed data is loaded by event listener in
	// application code
	@Order(1)
	public void applicationContextRefreshedListener(
			ContextRefreshedEvent contextRefreshedEvent) throws IOException {
		if (logger.isDebugEnabled()) {
			logger.debug(
					"applicationContextRefreshedListener(ContextRefreshedEvent contextRefreshedEvent={}) - start", //$NON-NLS-1$
					contextRefreshedEvent.getApplicationContext()
							.getDisplayName());
		}

		startEmbeddedMongoDB(env.getProperty("mongodb.user"),
				env.getProperty("mongodb.password"),
				env.getProperty("mongodb.host"),
				env.getProperty("mongodb.port"),
				env.getProperty("mongodb.database"));

		if (logger.isDebugEnabled()) {
			logger.debug(
					"applicationContextRefreshedListener(ContextRefreshedEvent contextRefreshedEvent={}) - end",
					contextRefreshedEvent.getApplicationContext()
							.getDisplayName());
		}
	}
	
	// dont need to stop as shutdown hook is added to runtime
	@EventListener(classes = { ContextClosedEvent.class,
			ContextStoppedEvent.class })
	// @EventListener
	public void applicationContextClosedListener(
			ApplicationEvent applicationEvent) {
		if (logger.isDebugEnabled()) {
			logger.debug("applicationContextClosedListener(ApplicationEvent applicationEvent={}) - start", applicationEvent); //$NON-NLS-1$
		}
		
		stopEmbeddedMongoDB();

		if (logger.isDebugEnabled()) {
			logger.debug("applicationContextClosedListener(ApplicationEvent applicationEvent={}) - end", applicationEvent); //$NON-NLS-1$
		}
	}

}
