package com.rabbithole.mongodb.examples.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import com.mongodb.WriteResult;
import com.rabbithole.mongodb.daos.User;

public interface UserTemplateService {

	/**
	 * Insert a new user
	 *
	 * @param user
	 */
	void insertNewUser(final User user);

	/**
	 * Insert a new user or Update an existing one
	 *
	 * @param user
	 */
	void insertOrUpdateUser(final User user);

	/**
	 * Find a user by id
	 *
	 * @param id
	 *
	 * @return {@link com.rabbithole.mongodb.daos.User}
	 */
	User findUserById(final ObjectId id);
	
	/**
	 * Find a user by name
	 *
	 * @param name
	 * @param useRepo
	 *
	 * @return Optional of {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findOneUserByName(final String name);

	/**
	 * Find the first user by name
	 *
	 * @param name
	 * @param useRepo
	 *
	 * @return Optional of {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findFirstUserByName(final String name);

	/**
	 * Find all users by name
	 *
	 * @param name
	 * @param useRepo
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findUsersByName(final String name);
	
	/**
	 * Find all users
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findAllUsers();
	
	/**
	 * Remove a user
	 *
	 * @param user
	 * @param useRepo
	 *
	 * @return {@link com.mongodb.WriteResult}
	 */
	WriteResult removeUser(final User user);
	
	/**
	 * Remove all users
	 */
	void removeAllUsers();
	
	/**
	 * Remove the entire collection
	 */
	void dropCollection();
	
}
