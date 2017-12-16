package com.rabbithole.mongodb.configs;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.bson.Transformer;
import org.joda.time.DateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.CustomConversions;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;

import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.utils.MongoDbUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * No need to extend {@link org.springframework.data.mongodb.config.AbstractMongoConfiguration} because url, database name and credentials are already defined as properties:
 * 		. spring.data.mongodb.host
 * 		. spring.data.mongodb.port [optional, default is 27017]
 * 		. spring.data.mongodb.database
 * 		. spring.data.mongodb.username
 * 		. spring.data.mongodb.password
 *
 * AbstractMongoConfiguration will create a MongoTemplate instance and registered with the container under the name mongoTemplate. But this is already done for us by Spring Boot.
 */
@Slf4j
@Configuration
public class MongoDbSpringBeansConfiguration {
	
	/**
	 * MongoDB database reference resolver to retrieve a DBObject from a DBRef field
	 *
	 * @param mongoDbFactory
	 *
	 * @return {@link org.springframework.data.mongodb.core.convert.DbRefResolver}
	 */
	@Bean(name = "dbRefResolver")
	@Resource
	public DbRefResolver createDbRefResolver(final MongoDbFactory mongoDbFactory) {

		log.debug("Creating DBRef Resolver...");

		return new DefaultDbRefResolver(mongoDbFactory);
	}
	
	/**
	 * Populate the MongoDB object encoding map with custom mapping
	 *
	 * @return Encoding map for MongoDB
	 */
	@Bean(name = "mongoDbEncodingHookMap")
	protected Map<Class<?>, Transformer> createAndLoadMongoDbEncodingHookMap() {

		log.debug("Creating MongoDB ENCODING hooks map...");
		
		final Map<Class<?>, Transformer> encMap = new HashMap<>();
		// Add hook to encode Joda DateTime as a java.util.Date
		log.debug("Encoding hook: Joda DateTime >>> java.util.Date");
		encMap.put(
				DateTime.class,
				o -> new Date(((DateTime) o).getMillis()));

		log.debug("Loading MongoDB BSON ENCODING hooks...");
		MongoDbUtils.addEncodingHooks(encMap);

		return encMap;
	}

	/**
	 * Populate the MongoDB object decoding map with custom mapping
	 *
	 * @return Decoding map for MongoDB
	 */
	@Bean(name = "mongoDbDecodingHookMap")
	protected Map<Class<?>, Transformer> createAndLoadMongoDbDecodingHookMap() {

		log.debug("Creating MongoDB DECODING hooks map...");
		
		final Map<Class<?>, Transformer> decMap = new HashMap<>();
		// Add hook to decode java.util.Date as a Joda DateTime
		log.debug("Decoding hook: java.util.Date >>> Joda DateTime");
		decMap.put(
				Date.class,
				o -> new DateTime(((Date) o).getTime()));

		log.debug("Loading MongoDB BSON DECODING hooks...");
		MongoDbUtils.addDecodingHooks(decMap);
		
		return decMap;
	}

	/**
	 * The method customConversions in AbstractMongoConfiguration (or its extensions) can be used to configure custom Converters.
	 *
	 * @param customConverters
	 *
	 * @return {@link org.springframework.data.mongodb.core.convert.CustomConversions}
	 */
	// @Primary
	@Bean(name = "mongoDbCustomConversionsList")
	// @Autowired
	@Resource
	protected CustomConversions createAndLoadCustomConverters(final List<Converter<?, ?>> customConverters) {
		
		log.debug("Creating and loading MongoDB custom converters...");

		if (customConverters == null || customConverters.isEmpty()) {
			log.info("No custom converters found");
		} else {
			log.debug("Customer converters found: " + customConverters.size());
			customConverters.forEach(
					cv -> log.debug(ProjectConstants.LOG_INDENT + cv.getClass()));
		}

		return new CustomConversions(customConverters);
	}

	// WORKING BUT WARN: Autowired annotation should only be used on methods with parameters
	// @PostConstruct
	// @Autowired
	// protected void loadingBsonHooks() {
	//
	// log.debug("Loading MongoDB BSON encoding/decoding hooks...");
	//
	// MongoDbUtils.addEncodingHooks(createMongoDbEncodingHookMap());
	// MongoDbUtils.addDecodingHooks(createMongoDbDecodingHookMap());
	// }
	
	// NOT WORKING: violating java life-cycle using annotation @PostConstruct on a method with parameters
	// @PostConstruct
	// @Autowired
	// protected void loadingBsonHooks(final Map<Class<?>, Transformer> encodingHooksMap, final Map<Class<?>, Transformer> decodingHooksMap) {
	//
	// log.debug("Loading MongoDB BSON encoding/decoding hooks...");
	//
	// MongoDbUtils.addEncodingHooks(encodingHooksMap);
	// MongoDbUtils.addDecodingHooks(decodingHooksMap);
	// }

	// NOT WORKING: violating java life-cycle using annotation @PostConstruct on a method with parameters
	// @PostConstruct
	// protected void loadingBsonHooks(@Autowired final Map<Class<?>, Transformer> encodingHooksMap,
	// @Autowired final Map<Class<?>, Transformer> decodingHooksMap) {
	//
	// log.debug("Loading MongoDB BSON encoding/decoding hooks...");
	//
	// MongoDbUtils.addEncodingHooks(encodingHooksMap);
	// MongoDbUtils.addDecodingHooks(decodingHooksMap);
	// }
	
}
