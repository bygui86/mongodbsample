package com.rabbithole.mongodb.repos;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.User;

/**
 * Common type of Repository that Spring Data usually provides: auto-generated queries out of method names.
 * The only thing we need to do to leverage these kinds of queries is to declare the method on the repository interface.
 *
 * Now we can auto-wire this UserRepository and use operations from MongoRepository or add custom operations.
 *
 * Here we list the more common type of query that Spring Data usually provides – auto-generated queries out of method names:
 *	. findBy
 *	. startingWith
 *	. endingWith
 *	. between
 *	. like
 *	. orderBy
 *
 * The only thing we need to do to leverage these kinds of queries is to declare the method on the repository interface
 * 		MongoRepository<ENTITY, ID_CLASS>
 *
 *
 * PLEASE NOTE:
 * No annotation are needed. But if using Spring XML configurations, please add
 * either this property in "application.properties"
 * 		spring.data.mongo.repositories.enabled=true
 * or this annotation on the main application class
 * 		@EnableMongoRepositories
 */
public interface UserRepository extends MongoRepository<User, ObjectId>, QueryDslPredicateExecutor<User>, UserRepositoryCustom {
	
	User findById(final ObjectId id);
	
	User findById(final Object objId);
	
	User findById(final String hexStringId);

	User findByEmail(final String email);
	
	List<User> findByName(final String name);

	List<User> findByNameStartingWith(final String regexp);

	List<User> findByNameEndingWith(final String regexp);
	
	// List<User> findByAgeBetween(final int ageGT, final int ageLT);

	@Query(ProjectConstants.QUERY_FIND_USERS_BY_REGEXP_NAME)
	List<User> findByRegexpName(final String regexp);
	
	/**
	 * Projections are a way to fetch only the required fields of a document from a database.
	 * This reduces the amount of data that has to be transferred from database server to client and hence increases performance.
	 * With Spring Data MongDB, projections can be used both with MongoTemplate and MongoRepository.
	 *
	 * The value=”{}” denotes no filters and hence all the documents will be fetched.
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.User}
	 */
	@Query(value = ProjectConstants.QUERY_VALUE_FIND_USERS_ONLY_EMAILS, fields = ProjectConstants.QUERY_FIELDS_FIND_USERS_ONLY_EMAILS)
	List<User> findOnlyEmails();

}
