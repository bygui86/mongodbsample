package com.rabbithole.mongodb.apps;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.dao.DuplicateKeyException;

import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.Address;
import com.rabbithole.mongodb.daos.User;
import com.rabbithole.mongodb.services.AddressService;
import com.rabbithole.mongodb.services.UserService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class UserMongoDbApplication {
	
	private final static String USER_LOG_PREFIX = "User: ";
	private final static String USER_ID_LOG_PREFIX = "User found by ID: ";
	private final static String USER_NAME_LOG_PREFIX = "User found by NAME: ";
	private final static String USER_EMAIL_LOG_PREFIX = "User with EMAIL ONLY: ";
	
	@Getter(value = AccessLevel.PROTECTED)
	private final UserService userService;
	
	@Getter(value = AccessLevel.PROTECTED)
	private final AddressService addressService;

	public void userTests() {
		
		log.info("Test User...");
		
		// Emails
		final String matEmail = "matteo@rabbithole.com";
		final String emaEmail = "emanuela@rabbithole.com";
		final String johnEmail = "john@rabbithole.com";
		final String stevenEmail = "steven@rabbithole.com";
		final String guyEmail = johnEmail;
		
		// Addresses
		final Address matAddrress = buildAddress("Vogelsangstrasse", 46, "Zurich", "Switzerland");
		final Address emaAddrress = buildAddress("Roentgenstrasse", 22, "Zurich", "Switzerland");
		final Address johnAddrress = buildAddress("W. Sunset Blvd.", 8033, "Hollywood-Ca", "Usa");
		final Address stevenAddrress = buildAddress("Flower Street", 1000, "Glendale - CA", "Usa");
		// final Address guyAddrress = buildAddress("Hayden Avenue", 3532, "Culver City - CA", "Usa");
		
		// Birthdays
		final DateTime matBday = new DateTime(1986, 1, 27, 0, 0);
		final DateTime emaBday = new DateTime(1991, 4, 9, 0, 0);
		final DateTime johnBday = new DateTime(1948, 1, 16, 0, 0);
		// final DateTime stevenBday = new DateTime(1947, 12, 18, 0, 0);
		// final DateTime gyuBday = new DateTime(1968, 9, 10, 0, 0);
		
		inserNewUser(
				"Matteo", "Baiguini", matEmail, matBday, matAddrress);
		insertOrUpdateUser(
				"Emanuela", "Pacifico", emaEmail, emaBday, emaAddrress);
		// Carpenter
		inserNewUser(
				"John", null, johnEmail, johnBday, johnAddrress);
		inserNewUser(
				"Steven", "Spielberg", stevenEmail, null, stevenAddrress);
		insertDuplicatedUser("Guy", "Ritchie", guyEmail);
		log.info(ProjectConstants.EMPTY_STRING);
		
		findAllUsers();
		log.info(ProjectConstants.EMPTY_STRING);
		
		findOnlyUsersEmails();
		log.info(ProjectConstants.EMPTY_STRING);
		
		final Optional<ObjectId> emaId = findFirstUserByEmail(emaEmail);
		if (!emaId.isPresent()) {
			log.error("User " + emaEmail + " not found!");
		}
		findUsersByName("Matteo", "J", "a$");
		log.info(ProjectConstants.EMPTY_STRING);
		
		if (existsUser(johnEmail)) {
			final Optional<ObjectId> johnId = findFirstUserByEmail(johnEmail);
			if (johnId.isPresent()) {
				updateUserSurnameByEmail(johnEmail, "Carpenter");
				findUserById(johnId.get());
			} else {
				log.error("User " + johnEmail + " not found!");
			}
		} else {
			log.error("User " + johnEmail + " does not exist!");
		}
		
		deleteAllUsers();
		deleteAllAddresses();
	}
	
	private void inserNewUser(final String name, final String surname, final String email, final DateTime bDay, final Address address) {
		
		getUserService().insertNew(
				buildUser(name, surname, email, bDay, address));
	}
	
	private void insertOrUpdateUser(final String name, final String surname, final String email, final DateTime bDay, final Address address) {

		getUserService().insertOrUpdate(
				buildUser(name, surname, email, bDay, address));
	}

	private void insertDuplicatedUser(final String name, final String surname, final String email) {

		try {
			inserNewUser(name, surname, email, null, null);
		} catch (final DuplicateKeyException e) {
			log.error("Error creating user " + email + ": " + e.getMessage());
		}
	}

	private Address insertNewAddress(final String streetName, final long streetNumber, final String city, final String country) {
		
		Address address = buildAddress(streetName, streetNumber, city, country);
		address = getAddressService().insert(address);
		return address;
	}

	private void findAllUsers() {
		
		// getUserService().findAll()
		// .forEach(
		// u -> log.info(USER_LOG_PREFIX + u));
		getUserService().findAll()
				.forEach(
						u -> log.info(USER_LOG_PREFIX + u.toStringPrettyPrint()));
		log.info(ProjectConstants.EMPTY_STRING);
	}

	private void findOnlyUsersEmails() {
		
		getUserService().findOnlyEmails()
				.forEach(
						u -> log.info(USER_EMAIL_LOG_PREFIX + u));
		log.info(ProjectConstants.EMPTY_STRING);
	}
	
	private Optional<ObjectId> findFirstUserByEmail(final String email) {
		
		final Optional<User> optUser = getUserService().findByEmail(email);
		if (optUser.isPresent()) {
			log.info(USER_LOG_PREFIX + optUser.get());
			log.info(ProjectConstants.EMPTY_STRING);
			return Optional.ofNullable(optUser.get().getId());
		}
		return Optional.empty();
	}
	
	private void findUsersByName(final String name, final String nameStrartingWith, final String regex) {

		final List<User> users = getUserService().findByName(name);
		users.addAll(
				getUserService().findByNameStartingWith(nameStrartingWith));
		users.addAll(
				getUserService().findByNameMatchingRegexp(regex));

		users.forEach(
				u -> log.info(USER_NAME_LOG_PREFIX + u));
		log.info(ProjectConstants.EMPTY_STRING);
	}
	
	private void findUserById(final ObjectId id) {
		
		getUserService().findById(id)
				.ifPresent(u -> {
					log.info(USER_ID_LOG_PREFIX + u);
					log.info(ProjectConstants.EMPTY_STRING);
				});
	}

	private boolean existsUser(final String email) {
		
		return getUserService().exists(
				buildUser(null, null, email, null, null));
	}

	private void updateUserSurnameByEmail(final String email, final String newSurname) {

		if (getUserService().updateSurnameByEmail(email, newSurname) != 1) {
			log.error("Error updating User " + email);
			return;
		}
	}

	private void deleteAllAddresses() {

		getAddressService().deleteAll();
	}

	private void deleteAllUsers() {
		
		getUserService().deleteAll();
	}
	
	private User buildUser(final String name, final String surname, final String email, final DateTime bDay, final Address address) {

		return User.builder()
				.name(name)
				.surname(surname)
				.email(email)
				.bDay(bDay)
				.address(address)
				.build();
	}

	private Address buildAddress(final String streetName, final long streetNumber, final String city, final String country) {

		return Address.builder()
				.streetName(streetName)
				.streetNumber(streetNumber)
				.city(city)
				.country(country)
				.build();
	}

}
