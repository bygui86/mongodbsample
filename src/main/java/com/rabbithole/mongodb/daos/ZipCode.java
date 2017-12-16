package com.rabbithole.mongodb.daos;

import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.annotations.QueryEntity;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.utils.JsonUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Slf4j
// @JsonIgnoreProperties(value = { "id" })
@JsonIgnoreProperties(ignoreUnknown = true)
@QueryEntity
@Document
@CompoundIndexes({
		@CompoundIndex(name = "zipcode_idx", unique = true, def = "{ zipCode: 1, city: 1, state: 1 }")
})
public class ZipCode {
	
	@Id
	@JsonIgnore
	private ObjectId id;

	@JsonProperty("_id")
	@NonNull
	private String zipCode;
	
	@NonNull
	private String city;
	
	@JsonProperty("loc")
	private Double[] location;
	
	@JsonProperty("pop")
	private Long population;
	
	@NonNull
	private String state;

	@CreatedBy
	@JsonIgnore
	private String createdBy;
	
	@LastModifiedBy
	@JsonIgnore
	private String lastModifiedBy;
	
	@CreatedDate
	@JsonIgnore
	private DateTime creationDate;
	
	@LastModifiedDate
	@JsonIgnore
	private DateTime lastModifiedDate;
	
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
			log.error("Error converting ZipCode[id:" + getId() + ", zipCode:" + getZipCode() + ", city:" + getCity() + ", state:" + getState() + "] :"
					+ e.getMessage() + ProjectConstants.LOG_NEWLINE
					+ "Returning empty String to avoid possible NullPointerException");
		}
		return ProjectConstants.EMPTY_STRING;
	}

}
