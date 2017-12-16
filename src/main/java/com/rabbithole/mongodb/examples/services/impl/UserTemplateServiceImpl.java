package com.rabbithole.mongodb.examples.services.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.WriteResult;
import com.rabbithole.mongodb.daos.User;
import com.rabbithole.mongodb.examples.services.UserTemplateService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The MongoTemplate follows the standard template pattern in Spring and provides a ready to go, basic API to the underlying persistence engine.
 *
 * The repository follows the Spring Data-centric approach and comes with more flexible and complex API operations, based on the well-known access
 * patterns in all Spring Data projects.
 */
@Slf4j
// Spring
// @Service("userService")
public class UserTemplateServiceImpl implements UserTemplateService {
	
	private static final String USER_FIELDS_NAME = "name";
	
	@Resource(name = "mongoTemplate")
	@Getter
	@Setter
	private MongoOperations mongoOperations;
	
	@Override
	public void insertNewUser(final User user) {
		
		log.debug("Insert new user: " + user.toString());
		
		getMongoOperations().insert(user);
	}

	@Override
	public void insertOrUpdateUser(final User user) {
		
		log.debug("Save user: " + user.toString());
		
		// The save operation has save-or-update semantics: if an id is present, it performs an update, if not – it does an insert.
		getMongoOperations().save(user);
	}

	@Override
	public User findUserById(final ObjectId id) {
		
		return getMongoOperations().findById(id, User.class);
	}

	@Override
	public Optional<User> findOneUserByName(final String name) {

		log.debug("Find one user by name: " + name);

		final Criteria criteria = Criteria.where(USER_FIELDS_NAME).is(name);
		return Optional.ofNullable(
				getMongoOperations().findOne(Query.query(criteria), User.class));
	}
	
	@Override
	public Optional<User> findFirstUserByName(final String name) {

		log.debug("Find first user by name: " + name);
		
		final List<User> users = findUsersByName(name);
		if (users != null && !users.isEmpty()) {
			return Optional.ofNullable(users.get(0));
		}
		return Optional.empty();
	}
	
	@Override
	public List<User> findUsersByName(final String name) {
		
		log.debug("Find users by name: " + name);
		
		final Criteria criteria = Criteria.where(USER_FIELDS_NAME).is(name);
		final Query query = new Query(criteria);
		return getMongoOperations().find(query, User.class);
	}

	@Override
	public List<User> findAllUsers() {
		
		log.debug("Find all users");

		return getMongoOperations().findAll(User.class);
	}

	@Override
	public WriteResult removeUser(final User user) {
		
		log.debug("Remove user: " + user.toString());

		return getMongoOperations().remove(user);
	}
	
	@Override
	public void removeAllUsers() {
		
		log.debug("Remove all users");

		getMongoOperations().findAndRemove(
				new Query(), User.class);
	}
	
	@Override
	public void dropCollection() {
		
		log.debug("Drop collection");

		getMongoOperations().dropCollection(User.class);
	}

}
