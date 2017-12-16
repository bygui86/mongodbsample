package com.rabbithole.mongodb.examples.bootableapps;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import com.rabbithole.mongodb.daos.User;
import com.rabbithole.mongodb.services.UserService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * 		. @Configuration tags the class as a source of bean definitions for the application context.
 * 		. @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 * 		  Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.
 * 		  This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 * 		. @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.
 *
 *  The main() method uses Spring Boot’s SpringApplication.run() method to launch an application. Did you notice that there wasn’t a single line of XML? No
 * web.xml file either. This web application is 100% pure Java and you didn’t have to deal with configuring any plumbing or infrastructure.
 *
 * Spring Boot will handle those repositories automatically as long as they are included in the same package (or a sub-package) of
 * your @SpringBootApplication class. For more control over the registration process, you can use the @EnableMongoRepositories annotation.
 *
 * By default, @EnableMongoRepositories will scan the current package for any interfaces that extend one of Spring Data’s repository interfaces. Use it’s
 * basePackageClasses=MyRepository.class to safely tell Spring Data MongoDB to scan a different root package by type if your project layout has multiple
 * projects and its not finding your repositories.
 */
@Slf4j
// @SpringBootApplication
public class TemplateMongoDbApplication implements CommandLineRunner {

	private final static String EMPTY_STRING = "";

	@Resource
	@Getter
	@Setter
	private UserService userService;

	public static void main(final String[] args) {
		
		SpringApplication.run(TemplateMongoDbApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {
		
		log.info(EMPTY_STRING);
		log.info(EMPTY_STRING);
		log.info("Starting MongoDB tests...");
		log.info(EMPTY_STRING);

		userTests();

		log.info(EMPTY_STRING);
		log.info("MongoDB tests finished, exiting...");
		log.info(EMPTY_STRING);
		log.info(EMPTY_STRING);
	}
	
	private void userTests() {

		inserNewUser("Matteo", "matteo@rabbithole.com");
		saveUser("Emanuela", "emanuela@rabbithole.com");
		inserNewUser("John", "john@@rabbithole.com");
		
		findAllUsers();
		
		findUserByEmail("emanuela@rabbithole.com");
		findSpecificUsers("Matteo", "J", "o$");

		final String johnName = "john@@rabbithole.com";
		findUserByEmail(johnName);
		updateUser(johnName, "Carpenter");
		findUserByEmail(johnName);

		removeAllUsers();
	}

	private void inserNewUser(final String name, final String email) {
		
		getUserService().insertNew(
				User.builder().name(name).email(email).build());
	}
	
	private void saveUser(final String name, final String email) {
		
		getUserService().insertOrUpdate(
				User.builder().name(name).email(email).build());
	}
	
	private void findAllUsers() {
		
		getUserService().findAll().forEach(
				u -> log.info("USER: " + u));
		log.info(EMPTY_STRING);
	}

	private void findSpecificUsers(final String name, final String partialName, final String regexp) {

		// Find by name
		final List<User> users = getUserService().findByName(name);
		
		// Find by name starting with
		users.addAll(getUserService().findByNameStartingWith(partialName));

		// Find by name using regex
		users.addAll(getUserService().findByNameMatchingRegexp(regexp));

		users.forEach(
				u -> log.info("USER: " + u));
		log.info(EMPTY_STRING);
	}
	
	private void findUserByEmail(final String name) {
		
		getUserService().findFirstByName(name)
				.ifPresent(
						u -> log.info("USER: " + u));
		log.info(EMPTY_STRING);
	}
	
	private void updateUser(final String email, final String newSurname) {
		
		if (getUserService().updateSurnameByEmail(email, newSurname) != 1) {
			log.error("Error updating User with email [" + email + "]");
			return;
		}
	}
	
	private void removeAllUsers() {
		
		getUserService().deleteAll();
	}
	
}
