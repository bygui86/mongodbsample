package com.rabbithole.mongodb.converters.impl;

import java.util.Optional;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.stereotype.Component;

import com.mongodb.DBObject;
import com.mongodb.DBRef;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.Address;
import com.rabbithole.mongodb.daos.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * When storing and querying your objects it is convenient to have a MongoConverter (MappingMongoConverter) instance handle the mapping of all Java types to DBObjects.
 * However, sometimes you may want the MongoConverter does most of the work but allow you to selectively handle the conversion for a particular
 * type or to optimize performance.
 *
 * To selectively handle the conversion yourself, register one or more one or more {@link org.springframework.core.convert.converter.Converter} instances with the
 * MongoConverter.
 * *** Spring 3.0 introduced a core.convert package that provides a general type conversion system. This is described in detail in the Spring reference documentation
 * section entitled {@link http://docs.spring.io/spring/docs/4.3.10.RELEASE/spring-framework-reference/html/validation.html#core-convert}.
 *
 * We have two options:
 * 	1. we can either work with MappingMongoConverter
 * 	2. we can write our own custom converter. To do that, we would need to implement the Converter interface and register
 * 	  the implementation in MongoConfig.
 *
 * Let's go for the second one.
 */
@Slf4j
@Component
@ReadingConverter
public class UserReaderConverter implements Converter<DBObject, User> {
	
	/**
	 * This looks like a pretty weird workaround, but at the time of developing this project I was not able
	 * to find a better solution. The documentation is not covering this specific case.
	 */
	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	protected DbRefResolver dbRefResolver;
	
	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	protected AddressReaderConverter addressReaderConverter;
	
	@Override
	public User convert(final DBObject source) {
		
		log.debug("Converting DBObject: " + source);
		
		final DBRef addressDbRef = (DBRef) source.get(ProjectConstants.USER_FIELDS_ADDRESS);
		
		final DateTime bDay = (DateTime) source.get(ProjectConstants.USER_FIELDS_BDAY);
		return User.builder()
				.id(((ObjectId) source.get(ProjectConstants.USER_FIELDS_ID)))
				.name((String) source.get(ProjectConstants.USER_FIELDS_NAME))
				.surname((String) source.get(ProjectConstants.USER_FIELDS_SURNAME))
				.email((String) source.get(ProjectConstants.USER_FIELDS_EMAIL))
				.bDay(bDay)
				.age(calculateAge(bDay))
				.address(fetchAddress(addressDbRef))
				.build();
	}
	
	protected Address fetchAddress(final DBRef addressDbRef) {
		
		final Optional<DBObject> optAddress = fetchDbObj(addressDbRef);
		if (optAddress.isPresent()) {
			return getAddressReaderConverter().convert(optAddress.get());
		}
		return null;
	}

	protected Optional<DBObject> fetchDbObj(final DBRef dbRef) {
		
		return Optional.ofNullable(
				getDbRefResolver().fetch(dbRef));
	}
	
	protected Integer calculateAge(final DateTime bDay) {

		return bDay != null
				? Integer.valueOf(DateTime.now().getYear() - bDay.getYear())
				: null;
	}

}
