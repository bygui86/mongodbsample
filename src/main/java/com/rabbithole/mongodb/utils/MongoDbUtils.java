package com.rabbithole.mongodb.utils;

import java.util.Map;

import org.bson.BSON;
import org.bson.Transformer;

public final class MongoDbUtils {

	private MongoDbUtils() {}
	
	/**
	 * Add to BSON encoding map all the Transformer in input
	 *
	 * @param bsonEncodingHookMap
	 */
	public static void addEncodingHooks(final Map<Class<?>, Transformer> bsonEncodingHookMap) {
		
		bsonEncodingHookMap.forEach(
				(clazz, transformer) -> BSON.addEncodingHook(clazz, transformer));
	}

	/**
	 * Add to BSON decoding map all the Transformer in input
	 *
	 * @param bsonEncodingHookMap
	 */
	public static void addDecodingHooks(final Map<Class<?>, Transformer> bsonDecodingHookMap) {
		
		bsonDecodingHookMap.forEach(
				(clazz, transformer) -> BSON.addDecodingHook(clazz, transformer));
	}
	
}
