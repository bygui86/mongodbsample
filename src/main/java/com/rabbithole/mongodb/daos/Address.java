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
import com.fasterxml.jackson.core.JsonProcessingException;
import com.querydsl.core.annotations.QueryEntity;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.utils.JsonUtils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Data
@Builder
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@QueryEntity
@Document
/*
 * MongoDB supports compound indexes, where a single index structure holds references to multiple fields.
 * @CompoundIndexes({
 * 		@CompoundIndex(name = "<INDEX_NAME>", def = "{'<FIELD_1>' : 1, '<FIELD_2>': 1}")
 * })
 */
@CompoundIndexes({
		@CompoundIndex(name = "address_idx", unique = true, def = "{ streetName: 1, streetNumber: 1, city: 1,  country: 1 }")
})
public class Address {

	@Id
	@JsonIgnore
	private ObjectId id;
	
	@NonNull
	private String streetName;
	
	// No need of @NonNull of primitive types
	// @NonNull
	private long streetNumber;
	
	@NonNull
	private String city;
	
	@NonNull
	private String country;

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
			log.error("Error converting Address[id:" + getId() + ", streetName:" + getStreetName() +
					", streetNumber:" + getStreetNumber() + ", city:" + getCity() + ", country:" + getCountry() + "] :"
					+ e.getMessage() + ProjectConstants.LOG_NEWLINE
					+ "Returning empty String to avoid possible NullPointerException");
		}
		return ProjectConstants.EMPTY_STRING;
	}

}
