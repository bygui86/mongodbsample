package com.rabbithole.mongodb.cascade.callbacks;

import java.lang.reflect.Field;

import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;
// This import was added manually, no clue why Eclipse was not suggesting it!
import org.springframework.util.ReflectionUtils.FieldCallback;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * I don't get the meaning of this class :(
 */
@Slf4j
public class CascadeSaveFieldCallback implements FieldCallback {

	@Getter
	private boolean idFound;

	/**
	 * Why searching on the field for the @Id annotation?
	 */
	@Override
	public void doWith(final Field field) throws IllegalArgumentException, IllegalAccessException {

		log.debug("Search for @Id annotation on field " + field.getName() + "...");
		
		if (!field.isAccessible()) {
			log.debug("Field " + field.getName() + " not accessible, hacking it...");
			ReflectionUtils.makeAccessible(field);
		}

		if (field.isAnnotationPresent(Id.class)) {
			log.debug("@Id annotation found on field " + field.getName());
			idFound = true;
		}
	}

}
