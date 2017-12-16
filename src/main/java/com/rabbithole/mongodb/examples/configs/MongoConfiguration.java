package com.rabbithole.mongodb.examples.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.converter.Converter;
// import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.rabbithole.mongodb.converters.impl.UserReaderConverter;

/**
 * We don’t need to define MongoTemplate bean in the previous configuration as it’s already defined in AbstractMongoConfiguration
 */
// @Configuration
// @EnableMongoRepositories(basePackages = "org.rabbithole.mongodb.repos")
public class MongoConfiguration extends AbstractMongoConfiguration {
	
	private static final String DB_NAME = "rabbithole";
	private static final String DB_HOST = "localhost";
	private static final int DB_PORT = 27017;
	
	private static final String BASE_PACKAGE = "com.rabbithole.mongodb";
	
	@Override
	protected String getDatabaseName() {
		
		return DB_NAME;
	}
	
	@Override
	public Mongo mongo() throws Exception {
		
		return new MongoClient(DB_HOST, DB_PORT);
	}
	
	@Override
	protected String getMappingBasePackage() {
		
		return BASE_PACKAGE;
	}
	
	@Override
	public CustomConversions customConversions() {
		
		final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
		converters.add(new UserReaderConverter());
		return new CustomConversions(converters);
	}
	
}
