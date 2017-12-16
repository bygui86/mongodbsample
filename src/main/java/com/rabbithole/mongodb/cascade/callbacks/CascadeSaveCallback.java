package com.rabbithole.mongodb.cascade.callbacks;

import java.lang.reflect.Field;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.util.ReflectionUtils;

import com.rabbithole.mongodb.cascade.annotations.CascadeSave;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * *** ISSUES ***
 * saving an object without ObjectId but already existing in MongoDB (e.g. two users with the same email, that is not allowed),
 * causing an exception:
 * 		com.mongodb.DuplicateKeyException: Write failed with error code 11000 and error message 'E11000 duplicate key error collection ...
 */
@Slf4j
public class CascadeSaveCallback implements ReflectionUtils.FieldCallback {
	
	@Getter(value = AccessLevel.PRIVATE)
	@Setter
	private Object source;
	
	@Getter(value = AccessLevel.PRIVATE)
	@Setter(value = AccessLevel.PRIVATE)
	private MongoOperations mongoOperations;

	public CascadeSaveCallback(final Object source, final MongoOperations mongoOperations) {
		
		log.debug("Constructor with MongoOperations and source: " + source);

		this.source = source;
		this.mongoOperations = mongoOperations;
	}

	@Override
	public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {

		log.debug("Search for @DBRef and @CascadeSave annotations on field " + field.getName() + "...");

		if (!field.isAccessible()) {
			log.debug("Field " + field.getName() + " not accessible, hacking it...");
			ReflectionUtils.makeAccessible(field);
		}

		if (field.isAnnotationPresent(DBRef.class)
				&& field.isAnnotationPresent(CascadeSave.class)) {

			final Object sourceObj = getSource();
			
			log.debug("@DBRef and @CascadeSave annotations found on field " + field.getName()
					+ ", getting the value for object " + sourceObj);
			
			final Object fieldValue = field.get(sourceObj);
			
			if (fieldValue != null) {
				
				log.debug("Field " + field.getName()
						+ " value: " + fieldValue);

				// Why ?! Do we need this call?
				// ReflectionUtils.doWithFields(fieldValue.getClass(), new CascadeSaveFieldCallback());
				
				// ISSUE
				log.debug("Save fieldvalue " + fieldValue + "...");
				getMongoOperations().save(fieldValue);
			}
		}
	}
	
}
