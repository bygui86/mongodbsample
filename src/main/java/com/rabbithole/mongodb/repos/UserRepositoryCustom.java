package com.rabbithole.mongodb.repos;

/**
 * MongoRepository custom interface
 *
 * The interface naming convention is very strict: the name must be "<POJO_NAME>RepositoryCustom", read following link for further info
 * {@link http://docs.spring.io/spring-data/data-document/docs/current/reference/html/#repositories.custom-implementations}
 */
public interface UserRepositoryCustom {

	/**
	 * Update a User surname finding it using the email
	 *
	 * @param email
	 * @param newSurname
	 *
	 * @return Number of documents affected
	 */
	int updateUserSurnameByEmail(final String email, final String newSurname);
	
}
