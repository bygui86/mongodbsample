package com.rabbithole.mongodb.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class GeneralBeansConfiguration {
	
	/**
	 * Post processor to translate any MongoExceptions thrown in @Repository annotated classes
	 *
	 * @return {@link org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor}
	 */
	@Bean(name = "stdPersistenceExceptionTranslationPostProcessor")
	public PersistenceExceptionTranslationPostProcessor createPersistenceExceptionTranslationPostProcessor() {
		
		log.debug("Creating Persistence Exception Translation PostProcessor...");

		return new PersistenceExceptionTranslationPostProcessor();
	}
	
}
