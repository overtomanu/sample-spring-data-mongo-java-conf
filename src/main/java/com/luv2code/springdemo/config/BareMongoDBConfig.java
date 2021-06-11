package com.luv2code.springdemo.config;
//@formatter:off 
/*
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoClientFactoryBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import com.mongodb.ConnectionString;

// This configuration will not work when @EnableMongoAuditing is used
// https://stackoverflow.com/questions/41530270/auditing-with-spring-data-mongodb/41532246
// https://stackoverflow.com/questions/35598595/how-to-customize-mappingmongoconverter-setmapkeydotreplacement-in-spring-boot

@Configuration
@PropertySource({ "classpath:persistence-mongodb.properties" })
*/
public class BareMongoDBConfig {
/*
	@Autowired
	private Environment env;
/*
	/*
	 * Factory bean that creates the com.mongodb.client.MongoClient instance.
	 * 
	 * As compared to instantiating a com.mongodb.client.MongoClient instance directly, 
	 * the FactoryBean has the added advantage of also providing the container 
	 * with an ExceptionTranslator implementation that translates MongoDB exceptions 
	 * to exceptions in Spring’s portable DataAccessException hierarchy for 
	 * data access classes annotated with the @Repository annotation.
	 */
	/*
	public @Bean MongoClientFactoryBean mongo() {
		MongoClientFactoryBean mongo = new MongoClientFactoryBean();
		mongo.setConnectionString(
				new ConnectionString(env.getProperty("mongodb.url")));
		// or configure by mong.setHost,port etc
		// set replicaSet if required
		return mongo;
	}

	public @Bean MongoDatabaseFactory mongoDatabaseFactory() throws Exception {
		return new SimpleMongoClientDatabaseFactory(mongo().getObject(),
				env.getProperty("mongodb.database"));
	}

	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		return new MongoTemplate(mongoDatabaseFactory());
	}

	@Bean
	MongoTransactionManager transactionManager(MongoDatabaseFactory dbFactory)
			throws Exception {
		return new MongoTransactionManager(dbFactory);
	}
*/

}
