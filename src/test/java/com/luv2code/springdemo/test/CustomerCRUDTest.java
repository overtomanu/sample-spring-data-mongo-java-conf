package com.luv2code.springdemo.test;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;

import com.luv2code.springdemo.application.PropertySourcesApplicationContextInitializer;
import com.luv2code.springdemo.config.RootConfig;
import com.luv2code.springdemo.entity.Customer;
import com.luv2code.springdemo.service.CustomerService;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		RootConfig.class }, initializers = PropertySourcesApplicationContextInitializer.class)
@ActiveProfiles(profiles = "test")
public class CustomerCRUDTest {
	@Autowired
	CustomerService customerService;

	@Autowired
	Environment env;

	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> customerCollection;

	@BeforeEach
	public void initMongoClient() {
		if (mongoClient == null) {
			mongoClient = MongoClients.create("mongodb://localhost:27017");
			database = mongoClient
					.getDatabase(env.getProperty("mongodb.database"));
			customerCollection = database.getCollection("customer");
		}
	}

	@Test
	public void createCustomerTest() throws SQLException {
		Customer theCustomer = new Customer();
		theCustomer.setFirstName("testCustomerFirstName");
		theCustomer.setLastName("test");
		theCustomer.setEmail("testCustomer@test.com");
		customerService.saveCustomer(theCustomer);

		Document savedCustomer = customerCollection
				.find(eq("firstName", "testCustomerFirstName")).first();

		// Querying using mongoTemplate
		// Declare @Autowired mongoTemplate
		/*
		Customer savedCustomer = mongoTemplate.findOne(
				  Query.query(Criteria.where("firstName").is("testCustomerFirstName")), Customer.class);
		*/

		assertNotNull(savedCustomer);
		assertTrue("Email not as expected",
				"testCustomer@test.com".equals(savedCustomer.get("email")));

	}

	@Test
	public void getAndUpdateCustomerTest() throws SQLException {

		String customerId = "id753";
		String customerFirstName = "test2FirstName";
		customerCollection.insertOne(new Document().append("_id", customerId)
				.append("firstName", customerFirstName)
				.append("lastName", "test2LastName")
				.append("email", "test2@test.com")
				/*.append("createdBy", "anonymous")
				.append("creationDate", LocalDateTime.now())
				.append("lastModifiedBy", "anonymous")
				.append("lastUpdateDate", LocalDateTime.now())*/

				// specifying version as zero is mandatory, else test fails
				.append("version", 0l));

		Customer customer = customerService.getCustomer(customerId);
		assertEquals(customerFirstName, customer.getFirstName());
		customerFirstName = "test2NameUpdated";
		customer.setFirstName(customerFirstName);
		customerService.saveCustomer(customer);

		Document savedCustomer = customerCollection.find(eq("_id", "id753"))
				.first();
		assertNotNull(savedCustomer);
		assertTrue("Updated Name not matching",
				customerFirstName.equals(savedCustomer.get("firstName")));
	}

	@Test
	public void deleteCustomerTest() throws SQLException {
		String customerId = "idDelete";
		String customerFirstName = "test3FirstName";
		customerCollection.insertOne(new Document().append("_id", customerId)
				.append("firstName", customerFirstName)
				.append("lastName", "test3LastName")
				.append("email", "test3@test.com")
				// specifying version as zero is mandatory, else test fails
				.append("version", 0l));

		customerService.deleteCustomer(customerId);
		Document deletedCustomer = customerCollection
				.find(eq("_id", customerId)).first();
		assertNull("deleted customer object not null", deletedCustomer);
	}

}
