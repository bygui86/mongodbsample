package com.rabbithole.mongodb.services;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbithole.mongodb.daos.ZipCode;
import com.rabbithole.mongodb.utils.FileUtils.ImportFileLocation;

public interface ZipCodeService {
	
	/**
	 * Insert a new ZipCode
	 *
	 * @param code
	 *
	 * @return Inserted {@link com.rabbithole.mongodb.daos.ZipCode}
	 */
	ZipCode insert(final ZipCode code);

	/**
	 * Insert a list of new ZipCodes
	 *
	 * @param codes
	 *
	 * @return Inserted list of {@link com.rabbithole.mongodb.daos.ZipCode}
	 */
	List<ZipCode> insert(final List<ZipCode> codes);
	
	/**
	 * Insert a new ZipCode or Update the existing one
	 *
	 * @param code
	 *
	 * @return Inserted or Updated {@link com.rabbithole.mongodb.daos.ZipCode}
	 */
	ZipCode insertOrUpdate(final ZipCode code);
	
	/**
	 * Insert a list of new ZipCodes or Update the existing ones
	 *
	 * @param codes
	 *
	 * @return Inserted or Updated list of {@link com.rabbithole.mongodb.daos.ZipCode}
	 */
	List<ZipCode> insertOrUpdate(final List<ZipCode> codes);

	/**
	 * Find all ZipCodes
	 *
	 * @return List of all {@link com.rabbithole.mongodb.daos.ZipCode} found
	 */
	List<ZipCode> findAll();

	/**
	 * Find ZipCode by example
	 *
	 * @param code
	 *
	 * @return {@link com.rabbithole.mongodb.daos.ZipCode}
	 */
	ZipCode findByExample(final ZipCode code);
	
	/**
	 * Find ZipCodes by city
	 *
	 * @param city
	 *
	 * @return List of {@link com.rabbithole.mongodb.daos.ZipCode}
	 */
	List<ZipCode> findByCity(final String city);
	
	/**
	 * Delete a ZipCode
	 *
	 * @param code
	 */
	void delete(final ZipCode code);

	/**
	 * Delete a list of ZipCodes
	 *
	 * @param codes
	 */
	void delete(final List<ZipCode> codes);

	/**
	 * Delete all ZipCodes
	 */
	void deleteAll();
	
	/**
	 * Import ZipCodes from a JSON file give its path
	 *
	 * *** PLEASE NOTE ***
	 * The JSON file should contain a valid array of JSON objects, like following
	 * 		[ {object1}, {object2}, {object3}, ... ]
	 * ***     ***     ***
	 *
	 * @param jsonFilePath
	 * @param fileLocation
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	void importFromJsonFile(final String jsonFilePath, final ImportFileLocation fileLocation)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Export list of ZipCodes to a JSON file
	 *
	 * @param jsonFilePath
	 * @param fileLocation
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	void exportToJsonFile(final List<ZipCode> codes, final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Export all ZipCodes to a JSON file
	 *
	 * @param jsonFilePath
	 * @param fileLocation
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	void exportAllToJsonFile(final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException;

}
