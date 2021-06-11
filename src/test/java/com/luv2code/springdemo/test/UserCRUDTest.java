package com.luv2code.springdemo.test;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;

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
import com.luv2code.springdemo.entity.Role;
import com.luv2code.springdemo.entity.User;
import com.luv2code.springdemo.service.UserService;
import com.mongodb.DBRef;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
		RootConfig.class }, initializers = PropertySourcesApplicationContextInitializer.class)
@ActiveProfiles(profiles = "test")
public class UserCRUDTest {
	@Autowired
	UserService userService;

	@Autowired
	Environment env;

	MongoClient mongoClient;
	MongoDatabase database;
	MongoCollection<Document> userCollection;

	@BeforeEach
	public void initMongoClient() {
		if (mongoClient == null) {
			mongoClient = MongoClients.create("mongodb://localhost:27017");
			database = mongoClient
					.getDatabase(env.getProperty("mongodb.database"));
			userCollection = database.getCollection("user");
		}
	}

	@Test
	public void findUserTest() {
		// user inserted in script seed_data.sql

		String testUserName = "susan";
		User user = userService.findUserByUserName(testUserName);
		assertNotNull(user);
		assertEquals(user.getUsername(), testUserName);
	}


	@Test
	public void saveUserTest() throws SQLException {
		User user = new User("testUser", "test123", true,
				new HashSet<Role>() {
					{
						add(new Role("ROLE_ADMIN"));
					}
				});
		userService.saveUser(user);
		Document savedUser = userCollection.find(eq("_id", "testUser")).first();
		List<DBRef> roles = (List<DBRef>) savedUser.get("roles");

		/*System.out.println(
				"******#####=>" + obj.getClass() + " | "
						+ ((DBRef) ((List) obj).get(0)).getId());*/

		assertTrue("user doesnt have expected role: ROLE_ADMIN",
				roles.contains(new DBRef("role", "ROLE_ADMIN")));

	}
}
