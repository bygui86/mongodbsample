package com.rabbithole.mongodb.events.listeners.impl;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.AfterConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterLoadEvent;
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This is an example of how to modify the lifecycle events of a generic object, to intercept it:
 * 		. before being converted to MongoDB document
 * 		. before being save to MongoDB
 * 		. after being saved to MongoDB
 * 		. after being loaded from MongoDB
 * 		. after being converted from MongoDB document to Java dao
 *
 * To intercept one of the events, we need to register a subclass of AbstractMappingEventListener and override one of the methods here.
 * Simply declaring these beans in your Spring ApplicationContext will cause them to be invoked whenever the event is dispatched.
 *
 * Lifecycle events are only emitted for root level types. Complex types used as properties within a document root are not subject of event
 * publication unless they are document references annotated with @DBRef.
 */
@Slf4j
// @Component("genericEventListener")
public class GenericEventListener extends AbstractMongoEventListener<Object> {
	
	// @Resource(name = "mongoTemplate")
	@Getter
	@Setter
	private MongoOperations mongoOperations;

	// @Resource
	@Getter
	@Setter
	private MappingMongoConverter mappingMongoConverter;

	/**
	 * Called in MongoTemplate insert, insertList and save operations BEFORE the object is CONVERTED to a DBObject using a MongoConveter
	 *
	 * Direction: Java >>> MongoDB
	 */
	@Override
	public void onBeforeConvert(final BeforeConvertEvent<Object> event) {
		
		log.debug("EventListener BeforeConvert [OBJECT]: " + event.getSource());
	}
	
	/**
	 * Called in MongoTemplate insert, insertList and save operations BEFORE INSERTING/SAVING the DBObject in the database.
	 *
	 * Direction: Java >>> MongoDB
	 */
	@Override
	public void onBeforeSave(final BeforeSaveEvent<Object> event) {

		log.debug("EventListener BeforeSave [OBJECT]: " + event.getSource());
	}

	/**
	 * Called in MongoTemplate insert, insertList and save operations AFTER INSERTING/SAVING the DBObject in the database.
	 *
	 * Direction: Java >>> MongoDB
	 */
	@Override
	public void onAfterSave(final AfterSaveEvent<Object> event) {
		
		log.debug("EventListener AfterSave [OBJECT]: " + event.getSource());
	}

	/**
	 * Called in MongoTemplate find, findAndRemove, findOne and getCollection methods AFTER the DBObject is RETRIEVED from the database.
	 *
	 * Direction: MongoDB >>> Java
	 */
	@Override
	public void onAfterLoad(final AfterLoadEvent<Object> event) {
		
		log.debug("EventListener AfterLoad [OBJECT]: " + event.getSource());
	}
	
	/**
	 * Called in MongoTemplate find, findAndRemove, findOne and getCollection methods AFTER the DBObject retrieved from the database was CONVERTED to a POJO.
	 *
	 * Direction: MongoDB >>> Java
	 */
	@Override
	public void onAfterConvert(final AfterConvertEvent<Object> event) {
		
		log.debug("EventListener AfterConvert [OBJECT]: " + event.getSource());
	}
	
}
