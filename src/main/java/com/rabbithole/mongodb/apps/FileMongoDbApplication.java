package com.rabbithole.mongodb.apps;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import com.mongodb.gridfs.GridFSFile;
import com.rabbithole.mongodb.services.GridFsService;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class FileMongoDbApplication {

	private final static String FILE_LOG_PREFIX = "File: ";
	private final static String RESOURCE_LOG_PREFIX = "Resource: ";

	@Getter(value = AccessLevel.PROTECTED)
	private final GridFsService gridFsService;
	
	public void fileTests() throws FileNotFoundException {

		log.info("Test GridFS file...");
		
		final GridFSFile file = insertFile();
		
		findFile(file.getId());
		
		getResource(file.getFilename());

		deleteAllFiles();
	}

	private GridFSFile insertFile() throws FileNotFoundException {

		final Map<String, Object> metadataMap = new HashMap<>();
		metadataMap.put("author", "Emanuela");
		final GridFSFile file = getGridFsService().storeFile("src/main/resources/static/docs/mongodb-gridfs-test.pdf",
				"mongodb-gridfs-test.pdf", "application/pdf", metadataMap);
		log.debug("FILE: " + file);
		return file;
	}
	
	private void getResource(final String filename) {

		getGridFsService().getResourceByFilename(filename)
				.ifPresent(
						r -> log.info(RESOURCE_LOG_PREFIX + r));
	}
	
	private void findFile(final Object objId) {
		
		getGridFsService().findFileById(objId)
				.ifPresent(
						f -> log.info(FILE_LOG_PREFIX + f));
	}
	
	private void deleteAllFiles() {

		getGridFsService().deleteAllFiles();
	}

}
