package com.rabbithole.mongodb.utils;

import java.util.Arrays;

import org.springframework.context.ApplicationContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class SpringUtils {
	
	private SpringUtils() {}
	
	/**
	 * Print out all Bean names found in given Spring Application Context
	 *
	 * @param applicationContext
	 */
	public static void logAppContextBeanNames(final ApplicationContext applicationContext) {
		
		log.debug("Bean names list:");

		Arrays.asList(
				applicationContext.getBeanDefinitionNames())
				.forEach(
						b -> log.debug("\t" + b));
	}

}
