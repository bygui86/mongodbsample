package com.rabbithole.mongodb;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.rabbithole.mongodb.aggregators.ZipCodeAggregator;
import com.rabbithole.mongodb.apps.UserMongoDbApplication;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.services.AddressService;
import com.rabbithole.mongodb.services.GridFsService;
import com.rabbithole.mongodb.services.UserService;
import com.rabbithole.mongodb.services.ZipCodeService;
import com.rabbithole.mongodb.utils.SpringUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @SpringBootApplication is a convenience annotation that adds all of the following:
 * 		. @Configuration tags the class as a source of bean definitions for the application context.
 * 		. @EnableAutoConfiguration tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
 * 		  Normally you would add @EnableWebMvc for a Spring MVC app, but Spring Boot adds it automatically when it sees spring-webmvc on the classpath.
 * 		  This flags the application as a web application and activates key behaviors such as setting up a DispatcherServlet.
 * 		. @ComponentScan tells Spring to look for other components, configurations, and services in the hello package, allowing it to find the controllers.
 *
 *  The main() method uses Spring Boot’s SpringApplication.run() method to launch an application. Did you notice that there wasn’t a single line of XML? No
 * web.xml file either. This web application is 100% pure Java and you didn’t have to deal with configuring any plumbing or infrastructure.
 *
 * Spring Boot will handle those repositories automatically as long as they are included in the same package (or a sub-package) of
 * your @SpringBootApplication class. For more control over the registration process, you can use the @EnableMongoRepositories annotation.
 *
 * By default, @EnableMongoRepositories will scan the current package for any interfaces that extend one of Spring Data’s repository interfaces. Use it’s
 * basePackageClasses=MyRepository.class to safely tell Spring Data MongoDB to scan a different root package by type if your project layout has multiple
 * projects and its not finding your repositories.
 */
// Lombok
@Slf4j
// Spring Boot
@SpringBootApplication
// Spring Data MongoDB auditing
// @EnableMongoAuditing
public class MongoDbApplication implements CommandLineRunner {
	
	// @Autowired(required = true)
	@Resource
	@Getter
	private ApplicationContext applicationContext;
	
	@Resource
	@Getter
	private UserService userService;
	
	@Resource
	@Getter
	private GridFsService gridFsService;

	@Resource
	@Getter
	private ZipCodeService zipCodeService;
	
	@Resource
	@Getter
	private ZipCodeAggregator zipCodeAggregator;

	@Resource
	@Getter
	private AddressService addressService;
	
	/**
	 * THE MAIN
	 */
	public static void main(final String[] args) {

		SpringApplication.run(MongoDbApplication.class, args);
	}
	
	/**
	 * THE RUN
	 *
	 * TODO
	 * FUTURE FEATURES:
	 * . MODIFY try other Json-translation technologies (alternative to Jackson) in {@link com.rabbithole.mongodb.utils.JsonUtilsn}
	 * . SET Jackson to convert to string Date and DateTime as simple string and not as entire object
	 * 		(probably not necessary if translating models to dtos)
	 * . MOIDIFY logic of User (getter) and UserReaderConveter to load the age as LAZY
	 * 		(deciding if lazy or not through a property)
	 * . ADD auditing on all daos
	 * 		@CreatedBy, @LastModifiedBy, @CreatedDate, @LastModifiedDate
	 * . ADD @Language example
	 * . ADD @TextIndexed example
	 * . ADD @GeoSpatialIndexed example
	 * . ADD @Version example
	 * . ADD bulk operations example
	 * . RE-IMPLEMENT generic cascade save
	 */
	@Override
	public void run(final String... args) throws Exception {
		
		// logBeanNames();

		log.info(ProjectConstants.EMPTY_STRING);
		log.info(ProjectConstants.EMPTY_STRING);
		log.info("Starting MongoDB tests...");
		log.info(ProjectConstants.EMPTY_STRING);

		new UserMongoDbApplication(getUserService(), getAddressService()).userTests();

		// new FileMongoDbApplication(getGridFsService()).fileTests();

		// new ZipCodeMongoDbApplication(getZipCodeService(), getZipCodeAggregator()).zipCodeTest();

		// new ImportMongoDbApplication(getZipCodeService()).importTest();
		
		// new ExportMongoDbApplication(getZipCodeService()).exportTest();
		
		log.info(ProjectConstants.EMPTY_STRING);
		log.info("MongoDB tests finished, exiting...");
		log.info(ProjectConstants.EMPTY_STRING);
		log.info(ProjectConstants.EMPTY_STRING);
	}

	protected void logBeanNames() {

		SpringUtils.logAppContextBeanNames(
				getApplicationContext());
	}

}
