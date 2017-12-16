package com.rabbithole.mongodb.examples.bootableapps;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
// import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.ImportResource;
// import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.context.annotation.Bean;

import com.rabbithole.mongodb.services.UserService;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @ImportResource("classpath:/spring-config.xml")
// This annotation is required using Spring XML configuration, otherwise Spring Data automagic repos won't be created
// @EnableMongoRepositories
// if no repos packages specified in Spring XML configuration
// @EnableMongoRepositories(basePackages = "org.rabbithole.mongodb.repos")
// @SpringBootApplication
public class XmlConfigsMongoDbApplication implements CommandLineRunner {
	
	@Resource
	@Getter
	@Setter
	private UserService userService;
	
	@Bean
	public Object beanCreation() {

		log.info("beanCreation");
		return null;
	}

	@PostConstruct
	public void postConstruct() {
		
		log.info("postConstruct");
	}
	
	public static void main(final String[] args) {
		
		SpringApplication.run(XmlConfigsMongoDbApplication.class, args);
	}

	@Override
	public void run(final String... args) throws Exception {

		log.info("Testing Spring XML configuration...");
	}
	
}
