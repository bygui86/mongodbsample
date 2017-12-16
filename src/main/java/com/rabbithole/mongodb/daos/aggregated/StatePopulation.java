package com.rabbithole.mongodb.daos.aggregated;

import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.utils.JsonUtils;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Data
@Builder
@Slf4j
public class StatePopulation {

	/**
	 * The @Id annotation will map the _id field from output to state in the model
	 */
	@Id
	@NonNull
	private String state;
	
	@NonNull
	private Integer totalPopulation;

	@Override
	public String toString() {
		
		return parseToJson(false);
	}
	
	public String toStringPrettyPrint() {
		
		return parseToJson(true);
	}

	protected String parseToJson(final boolean prettyPrint) {
		
		try {
			return JsonUtils.serializeObjectToJsonString(this, prettyPrint);
			
		} catch (final JsonProcessingException e) {
			log.error("Error converting StatePopulation[state:" + getState() + ", totalPopulation:" + getTotalPopulation()
					+ e.getMessage() + ProjectConstants.LOG_NEWLINE
					+ "Returning empty String to avoid possible NullPointerException");
		}
		return ProjectConstants.EMPTY_STRING;
	}

}
