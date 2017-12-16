package com.rabbithole.mongodb.services.impl;

import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.querydsl.core.types.Predicate;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.QUser;
import com.rabbithole.mongodb.daos.User;
import com.rabbithole.mongodb.repos.UserRepository;
import com.rabbithole.mongodb.services.UserService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// Spring
@Service("userService")
public class UserServiceImpl implements UserService {
	
	// @Autowired(required = true)
	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private UserRepository userRepository;

	@Override
	public User insertNew(final User user) {

		log.debug("Insert new User: " + user);

		return getUserRepository().insert(user);
	}

	@Override
	public List<User> insertNew(final List<User> users) {
		
		if (log.isDebugEnabled()) {
			log.debug("Insert " + users.size() + " Users:");
			users.forEach(
					u -> log.debug(ProjectConstants.LOG_INDENT + u));
		}
		
		return getUserRepository().insert(users);
	}
	
	@Override
	public User insertOrUpdate(final User user) {

		log.debug("Insert or Update User: " + user.toString());

		// The save operation has save-or-update semantics: if an id is present, it performs an update, if not – it does an insert.
		return getUserRepository().save(user);
	}
	
	@Override
	public List<User> insertOrUpdate(final List<User> users) {
		
		if (log.isDebugEnabled()) {
			log.debug("Insert or Update " + users.size() + " Users:");
			users.forEach(
					u -> log.debug(ProjectConstants.LOG_INDENT + u));
		}
		
		return getUserRepository().save(users);
	}
	
	@Override
	public boolean exists(final User user) {

		log.debug("Exists User: " + user);

		return getUserRepository().exists(
				Example.of(user));
	}

	@Override
	public boolean exists(final String email) {

		log.debug("Exists User: " + email);

		return exists(
				User.builder()
						.email(email).build());
	}
	
	@Override
	public List<User> findAll() {

		log.debug("Find all Users");
		
		return getUserRepository().findAll();
	}

	@Override
	public List<User> findAllSorted(final Sort.Direction direction, final String sortingField) {

		log.debug("Find all Users sorting field: " + sortingField + " and direction: " + direction);

		return getUserRepository().findAll(
				new Sort(direction, sortingField));
	}
	
	@Override
	public List<User> findAllPaged(final int pageSize, final int page) {
		
		log.debug("Find all Users pagede, page: " + page + " and page size: " + pageSize);

		return getUserRepository().findAll(new PageRequest(pageSize, page)).getContent();
	}
	
	@Override
	public List<User> findOnlyEmails() {
		
		log.debug("Find only Users emails");

		return getUserRepository().findOnlyEmails();
	}
	
	@Override
	public Optional<User> findById(final ObjectId id) {

		log.debug("Find User by id (ObjectId): " + id.toHexString());

		return Optional.ofNullable(
				getUserRepository().findById(id));
	}

	@Override
	public Optional<User> findById(final Object objId) {
		
		log.debug("Find User by id (Object): " + objId);

		return Optional.ofNullable(
				getUserRepository().findById(objId));
	}
	
	@Override
	public Optional<User> findById(final String hexStringId) {

		log.debug("Find User by id (String): " + hexStringId);
		
		return Optional.ofNullable(
				getUserRepository().findById(hexStringId));
	}
	
	@Override
	public Optional<User> findByEmail(final String email) {
		
		log.debug("Find User by email: " + email);
		
		return Optional.ofNullable(getUserRepository().findByEmail(email));
	}

	@Override
	public Optional<User> findOneByName(final String name) {
		
		log.debug("Find one User by name: " + name);
		
		final User userExample = User.builder().name(name).build();
		final Example<User> example = Example.of(userExample);
		return Optional.ofNullable(
				getUserRepository().findOne(example));
	}

	@Override
	public Optional<User> findFirstByName(final String name) {
		
		log.debug("Find first User by name: " + name);

		final List<User> users = findByName(name);
		if (users != null && !users.isEmpty()) {
			return Optional.ofNullable(users.get(0));
		}
		return Optional.empty();
	}

	@Override
	public List<User> findByName(final String name) {

		log.debug("Find Users by name: " + name);

		return getUserRepository().findByName(name);
	}
	
	@Override
	public List<User> findByNameStartingWith(final String partialName) {
		
		log.debug("Find Users which name starts with: " + partialName);

		return getUserRepository().findByNameStartingWith(partialName);
	}
	
	@Override
	public List<User> findByNameMatchingRegexp(final String regex) {
		
		log.debug("Find Users which name match regex: " + regex);

		return getUserRepository().findByRegexpName(regex);
	}
	
	@Override
	public List<User> findByNameWithQueryDsl(final String name) {

		log.debug("Find Users using QueryDsl by name: " + name);
		
		final QUser qUser = new QUser(ProjectConstants.USER_ENTITY_ID);
		final Predicate predicate = qUser.name.eq(name);
		return (List<User>) getUserRepository().findAll(predicate);
	}
	
	@Override
	public void delete(final User user) {

		log.debug("Remove User: " + user);
		
		getUserRepository().delete(user);
	}

	@Override
	public void delete(final List<User> users) {

		if (log.isDebugEnabled()) {
			log.debug("Remove " + users.size() + " Users:");
			users.forEach(
					u -> log.debug(ProjectConstants.LOG_INDENT + u));
		}
		
		getUserRepository().delete(users);
	}
	
	@Override
	public void deleteAll() {

		log.debug("Remove all Users");
		
		getUserRepository().deleteAll();
	}
	
	@Override
	public int updateSurnameByEmail(final String email, final String newSurname) {
		
		log.debug("Update User's surname (new: " + newSurname + ") searching it by email: " + email);
		
		return getUserRepository().updateUserSurnameByEmail(email, newSurname);
	}
	
}
