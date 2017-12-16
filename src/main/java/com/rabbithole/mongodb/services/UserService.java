package com.rabbithole.mongodb.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Sort;

import com.rabbithole.mongodb.daos.User;

/**
 * The MongoTemplate follows the standard template pattern in Spring and provides a ready to go, basic API to the underlying persistence engine.
 *
 * The repository follows the Spring Data-centric approach and comes with more flexible and complex API operations, based on the well-known access
 * patterns in all Spring Data projects.
 */
public interface UserService {
	
	/**
	 * Insert a new User
	 *
	 * @param user
	 *
	 * @return inserted User
	 */
	User insertNew(final User user);
	
	/**
	 * Insert a list of new Users
	 *
	 * @param users
	 *
	 * @return inserted Users
	 */
	List<User> insertNew(final List<User> users);
	
	/**
	 * Insert a new User or Update an existing one
	 *
	 * @param user
	 *
	 * @return inserted User or updated User
	 */
	User insertOrUpdate(final User user);

	/**
	 * Insert a list of new Users or Update the existing ones
	 *
	 * @param users
	 *
	 * @return inserted Users or updated Users
	 */
	List<User> insertOrUpdate(final List<User> users);

	/**
	 * Check if a User exists
	 *
	 * @param user
	 *
	 * @return TRUE if the User exists, FALSE otherwise
	 */
	boolean exists(final User user);
	
	/**
	 * Check if a User exists
	 *
	 * @param email
	 *
	 * @return TRUE if the User exists, FALSE otherwise
	 */
	boolean exists(final String email);
	
	/**
	 * Find all Users
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findAll();
	
	/**
	 * Find all Users sorted on a field
	 *
	 * @param direction
	 * @param sortingField
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findAllSorted(final Sort.Direction direction, final String sortingField);
	
	/**
	 * Find all Users with pagination
	 *
	 * @param pageSize
	 * @param page
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findAllPaged(final int pageSize, final int page);
	
	/**
	 * Find only Users emails
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findOnlyEmails();

	/**
	 * Find a User by id (ObjectId)
	 *
	 * @param id
	 *
	 * @return {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findById(final ObjectId id);
	
	/**
	 * Find a User by id (Object)
	 *
	 * @param objId
	 *
	 * @return {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findById(final Object objId);
	
	/**
	 * Find a User by id (String)
	 *
	 * @param hexStringId
	 *
	 * @return {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findById(final String hexStringId);

	/**
	 * Find a User by email
	 *
	 * @param email
	 *
	 * @return Optional of {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findByEmail(final String email);
	
	/**
	 * Find a User by name
	 *
	 * @param name
	 * @param useRepo
	 *
	 * @return Optional of {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findOneByName(final String name);
	
	/**
	 * Find the first User by name
	 *
	 * @param name
	 * @param useRepo
	 *
	 * @return Optional of {@link com.rabbithole.mongodb.daos.User}
	 */
	Optional<User> findFirstByName(final String name);
	
	/**
	 * Find all Users by name
	 *
	 * @param name
	 * @param useRepo
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findByName(final String name);
	
	/**
	 * Find all Users by name starting with
	 *
	 * @param partialName
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findByNameStartingWith(final String partialName);
	
	/**
	 * Find all Users by regex on the name
	 *
	 * @param regexp
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findByNameMatchingRegexp(final String regexp);

	/**
	 * Find all Users by name using QueryDSL
	 *
	 * @param name
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	List<User> findByNameWithQueryDsl(final String name);
	
	/**
	 * Delete all Users
	 */
	void deleteAll();

	/**
	 * Delete a User
	 *
	 * @param user
	 */
	void delete(final User user);

	/**
	 * Delete a list of Users
	 *
	 * @param users
	 */
	void delete(final List<User> users);
	
	/**
	 * PLEASE NOTE: This method is not standard and it just demonstrates how to extend and customize a Spring Data automagic repo
	 *
	 * Update User's surname searching it by email
	 *
	 * @param email
	 * @param newSurname
	 *
	 * @return number of rows affected
	 */
	int updateSurnameByEmail(final String email, final String newSurname);

}
