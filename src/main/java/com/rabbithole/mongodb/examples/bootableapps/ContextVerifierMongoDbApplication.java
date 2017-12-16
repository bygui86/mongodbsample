package com.rabbithole.mongodb.examples.bootableapps;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
// @SpringBootApplication
public class ContextVerifierMongoDbApplication implements CommandLineRunner {

	@Resource
	@Getter
	@Setter
	private ApplicationContext applicationContext;

	public static void main(final String[] args) {
		
		SpringApplication.run(ContextVerifierMongoDbApplication.class, args);
	}
	
	@Override
	public void run(final String... args) throws Exception {
		
		final List<String> beanNames = Arrays.asList(getApplicationContext().getBeanDefinitionNames());
		final int beanCount = getApplicationContext().getBeanDefinitionCount();
		log.info(beanCount + " beans found:");
		beanNames.forEach(
				b -> {
					// if (b.toLowerCase().contains("refresolver")
					// || b.toLowerCase().contains("dbref")
					// || b.toLowerCase().contains("resolver")) {
					log.info("\t" + b);
					// }
				});
	}
	
}
