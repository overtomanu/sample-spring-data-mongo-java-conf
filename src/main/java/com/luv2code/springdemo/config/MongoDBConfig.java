package com.luv2code.springdemo.config;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.luv2code.springdemo.entity.Customer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings.Builder;

@PropertySource({ "classpath:persistence-mongodb.properties" })
public class MongoDBConfig extends AbstractMongoClientConfiguration {

	@Autowired
	private Environment env;

	@Override
	protected String getDatabaseName() {
		return env.getProperty("mongodb.database");
	}

	@Override
	protected Collection<String> getMappingBasePackages() {
		return Collections.singleton(Customer.class.getPackageName());
	}

	@Override
	protected void configureClientSettings(Builder builder) { 
		builder.applyConnectionString(
				new ConnectionString(env.getProperty("mongodb.url"))).build();
	}

	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory)
			throws Exception {
		return new MongoTransactionManager(dbFactory);
	}

}
