package com.rabbithole.mongodb.apps;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.ZipCode;
import com.rabbithole.mongodb.services.ZipCodeService;
import com.rabbithole.mongodb.utils.FileUtils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class ImportMongoDbApplication {

	@Getter(value = AccessLevel.PROTECTED)
	private final ZipCodeService zipCodeService;
	
	public void importTest() throws JsonParseException, JsonMappingException, IOException {
		
		log.info("Import ZipCodes from JSON file...");

		importZipCodes(ProjectConstants.IMPORT_JSON_PARTIAL_FILEPATH);
		log.info(ProjectConstants.EMPTY_STRING);
		
		findAllZipCodes();
		
		deleteAllZipCodes();
		
		log.info(ProjectConstants.EMPTY_STRING);
		log.info(ProjectConstants.EMPTY_STRING);
		
		final Optional<File> optProjResFolder = FileUtils.getProjectResourcesFolder(false);
		if (optProjResFolder.isPresent()) {
			if (FileUtils.existsFile(
					optProjResFolder.get().getAbsolutePath() + ProjectConstants.EXPORT_JSON_FILEPATH)) {
				
				log.info("Import ZipCodes from previously exported JSON file...");
				
				importZipCodes(ProjectConstants.EXPORT_JSON_FILEPATH);
				log.info(ProjectConstants.EMPTY_STRING);
				
				findAllZipCodes();
				
				deleteAllZipCodes();
			}
		}
	}

	private void importZipCodes(final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException {

		getZipCodeService().importFromJsonFile(
				jsonFilePath, FileUtils.ImportFileLocation.CLASSPTH);
	}
	
	private void findAllZipCodes() {
		
		final List<ZipCode> codes = getZipCodeService().findAll();
		codes.forEach(
				zc -> log.info("ZipCode:" + zc));
	}

	private void deleteAllZipCodes() {
		
		getZipCodeService().deleteAll();
	}
}
