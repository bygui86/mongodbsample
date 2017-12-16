package com.rabbithole.mongodb.examples.bootableapps;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import com.rabbithole.mongodb.daos.User;
import com.rabbithole.mongodb.repos.UserRepository;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @SpringBootApplication
public class RepoMongoDbApplication implements CommandLineRunner {
	
	private final static String EMPTY_STRING = "";
	
	@Resource
	@Getter
	@Setter
	private UserRepository userRepository;
	
	public static void main(final String[] args) {

		SpringApplication.run(RepoMongoDbApplication.class, args);
	}
	
	@Override
	public void run(final String... args) throws Exception {

		log.info("Starting MongoDB tests...");
		log.info(EMPTY_STRING);
		
		userTests();
		
		log.info(EMPTY_STRING);
		log.info("MongoDB tests finished, exiting...");
		log.info(EMPTY_STRING);
	}

	private void userTests() {
		
		inserNewUser("Matteo", 31);
		inserNewUser("Emanuela", 26);
		saveUser("John", 25);

		findAllUsers();
		findUser("Emanuela");

		final String johnName = "John";
		findUser(johnName);
		updateUser(johnName, 35);
		findUser(johnName);

		removeAllUsers();
	}
	
	private void inserNewUser(final String name, final int age) {

		// final User newUser = User.builder().name(name).age(age).build();
		// getUserRepository().insert(newUser);
	}

	private void saveUser(final String name, final int age) {

		// final User newUser = User.builder().name(name).age(age).build();
		// getUserRepository().save(newUser);
	}

	private void findAllUsers() {

		final List<User> users = getUserRepository().findAll();
		users.forEach(
				u -> log.info("USER: " + u));
		log.info(EMPTY_STRING);
	}

	private void findUser(final String email) {

		final User user = getUserRepository().findByEmail(email);
		if (user != null) {
			log.info("USER: " + user);
		}
		log.info(EMPTY_STRING);
	}

	private void updateUser(final String userName, final int newAge) {
		
		// final int rowsAffected = getUserRepository().updateUserAgeByName(userName, newAge);
		// if (rowsAffected != 1) {
		// log.error("Error updating User with name [" + userName + "]");
		// return;
		// }
	}

	private void removeAllUsers() {

		getUserRepository().deleteAll();
	}

}
