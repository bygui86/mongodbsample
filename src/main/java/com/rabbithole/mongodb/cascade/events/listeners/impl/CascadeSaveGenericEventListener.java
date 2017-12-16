package com.rabbithole.mongodb.cascade.events.listeners.impl;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.util.ReflectionUtils;

import com.rabbithole.mongodb.cascade.callbacks.CascadeSaveCallback;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Note that the mapping framework doesn’t handle cascading operations. So – for instance – if we trigger a save on a parent, the child won’t be saved
 * automatically – we’ll need to explicitly trigger the save on the child if we want to save it as well.
 * This is precisely where life cycle events come in handy. Spring Data MongoDB publishes some very useful life cycle events – such as onBeforeConvert, onBeforeSave, onAfterSave, onAfterLoad and onAfterConvert.
 *
 * Extending the {@link com.rabbithole.mongodb.events.listeners.impl.UserEventListener} specific solution,
 * making the cascade functionality generic for any object.
 */
@Slf4j
// @Component("cascadeSaveGenericEventListener")
public class CascadeSaveGenericEventListener extends AbstractMongoEventListener<Object> {
	
	@Resource(name = "mongoTemplate")
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private MongoOperations mongoOperations;
	
	@Override
	public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
		
		final Object sourceObj = event.getSource();
		
		log.debug("EventListener BeforeConvert [GENERIC]: " + sourceObj);
		
		/*
		 * TODO
		 * Why going through every attribute of the class?
		 * IMH better to search for @DBRef + @CascadeSave annotated attribute and save selectively
		 */
		ReflectionUtils.doWithFields(
				sourceObj.getClass(),
				new CascadeSaveCallback(sourceObj, getMongoOperations()));
	}
	
}
