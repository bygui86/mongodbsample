package com.rabbithole.mongodb.apps;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.services.ZipCodeService;
import com.rabbithole.mongodb.utils.FileUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ExportMongoDbApplication {
	
	@Getter(value = AccessLevel.PROTECTED)
	private final ZipCodeService zipCodeService;
	
	public void exportTest() throws JsonParseException, JsonMappingException, IOException {

		log.info("Export ZipCodes to JSON file...");

		importZipCodes(ProjectConstants.IMPORT_JSON_PARTIAL_FILEPATH);
		log.info(ProjectConstants.EMPTY_STRING);

		exportAllZipCodes(ProjectConstants.EXPORT_JSON_FILEPATH);
		
		deleteAllZipCodes();
	}
	
	private void exportAllZipCodes(final String jsonFilePath) throws IOException {

		getZipCodeService().exportAllToJsonFile(jsonFilePath);
	}
	
	private void importZipCodes(final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException {

		getZipCodeService().importFromJsonFile(
				jsonFilePath, FileUtils.ImportFileLocation.CLASSPTH);
	}
	
	private void deleteAllZipCodes() {
		
		getZipCodeService().deleteAll();
	}
	
}
