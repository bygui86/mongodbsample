package com.rabbithole.mongodb.examples.configs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
// import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.core.convert.CustomConversions;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.rabbithole.mongodb.converters.impl.UserReaderConverter;

/**
 * We can also use our configuration from scratch without extending AbstractMongoConfiguration
 */
// @Configuration
// @EnableMongoRepositories(basePackages = "org.rabbithole.mongodb.repos")
public class CustomMongoConfiguration {

	private static final String DB_NAME = "rabbithole";
	private static final String DB_HOST = "localhost";
	
	@Bean
	public Mongo mongoClient() throws Exception {
		
		return new MongoClient(DB_HOST);
	}
	
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		
		return new MongoTemplate(mongoClient(), DB_NAME);
	}
	
	@Bean
	public CustomConversions customConversions() {
		
		final List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
		converters.add(new UserReaderConverter());
		return new CustomConversions(converters);
	}
	
}
