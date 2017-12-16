package com.rabbithole.mongodb.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class FileUtils {

	private static final String PROJ_RESOURCES_FOLDER_PATH = "src/main/resources";

	private FileUtils() {}
	
	/**
	 * Return the {@link java.io.File} reference to the project resources folder
	 *
	 * @return {@link java.util.Optional<java.io.File>}
	 *
	 * @throws IOException
	 */
	public static Optional<File> getProjectResourcesFolder(final boolean createIfNotExisting)
			throws IOException {
		
		final FileSystemResource fileSysRes = new FileSystemResource(PROJ_RESOURCES_FOLDER_PATH);
		log.debug("Project resources uri: " + fileSysRes.getURI().getPath());
		File resFolder = null;
		if (fileSysRes.exists()) {
			resFolder = fileSysRes.getFile();
			log.debug("Project resources absolute path: " + resFolder.getAbsolutePath());
		} else {
			log.warn("Project resources folder DOES NOT EXIST");
			if (createIfNotExisting) {
				resFolder = createNewFile(fileSysRes.getURI().getPath());
			}
		}
		return Optional.ofNullable(resFolder);
	}

	/**
	 * Create a new file on FileSystem
	 *
	 * @param filePath
	 * @param removeIfExisting
	 *
	 * @return {@link java.util.Optional<java.io.File>}
	 *
	 * @throws IOException
	 */
	public static Optional<File> createNewFile(final String filePath, final boolean removeIfExisting)
			throws IOException {

		File file = new File(filePath);
		if (file.exists()) {
			log.warn("File " + filePath + " already exists");
			if (removeIfExisting) {
				log.debug("Remove File " + filePath + " to craete a new one");
				file.delete();
				file = createNewFile(filePath);
			}
		} else {
			file = createNewFile(filePath);
		}
		return Optional.ofNullable(file);
	}
	
	private static File createNewFile(final String filePath)
			throws IOException {
		
		File file = new File(filePath);
		if (file.createNewFile()) {
			log.debug("File " + filePath + " created");
		} else {
			log.error("File " + filePath + " not created, an error occurred");
			file = null;
		}
		return file;
	}
	
	public static boolean existsFile(final String filePath) {
		
		log.debug("Exist file " + filePath);
		
		return new File(filePath).exists();
	}
	
	/*****************
	 * INPUT
	 *****************/

	/**
	 * Load and parse a JSON file containing a list of object {@link T}
	 *
	 * @param jsonFilePath
	 * @param fileLocation
	 * @param clazz
	 *
	 * @return List of {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> loadAndParseJsonFile(final String jsonFilePath, final ImportFileLocation fileLocation, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		final InputStream jsonInStream;
		switch (fileLocation) {
			case CLASSPTH:
				jsonInStream = FileUtils.openInputStreamFromClasspath(jsonFilePath);
				break;
			case FILE_SYSTEM:
				jsonInStream = FileUtils.openInputStreamFromFileSystem(jsonFilePath);
				break;
			default:
				jsonInStream = FileUtils.openInputStreamFromClasspath(jsonFilePath);
				break;
		}
		
		log.debug("Parse JSON file " + jsonFilePath + " to " + clazz + "...");

		return JsonUtils.<T> bulkParseJsonToObjects(jsonInStream, clazz);
	}

	/**
	 * Load a file from ClassPath
	 *
	 * *** PLEASE NOTE ***
	 * Returning just an InputStream, because a file included in the ClassPath is not a normal physical one.
	 * ***     ***     ***
	 *
	 * @param filePath
	 *
	 * @return {@link java.io.InputStream}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static InputStream openInputStreamFromClasspath(final String filePath)
			throws JsonParseException, JsonMappingException, IOException {
		
		log.debug("Open InputStream " + filePath + " from ClassPath...");

		final ClassPathResource classPathResource = new ClassPathResource(filePath);
		return classPathResource.getInputStream();
	}
	
	/**
	 * Load a {@link java.io.File} in FileSystem
	 *
	 * @param filePath
	 *
	 * @return {@link java.io.File}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static File openFileFromFileSystem(final String filePath)
			throws JsonParseException, JsonMappingException, IOException {
		
		log.debug("Open File " + filePath + " from FileSystem...");

		final FileSystemResource fileSysResource = new FileSystemResource(filePath);
		return fileSysResource.getFile();
	}
	
	/**
	 * Load an InputStream to a file in FileSystem
	 *
	 * @param filePath
	 *
	 * @return {@link java.io.InputStream}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static InputStream openInputStreamFromFileSystem(final String filePath)
			throws JsonParseException, JsonMappingException, IOException {
		
		log.debug("Open InputStream to file " + filePath + " from FileSystem...");
		
		final FileSystemResource fileSysResource = new FileSystemResource(filePath);
		return fileSysResource.getInputStream();
	}
	
	/**
	 * Enumeration to choose from where to load a file
	 */
	public enum ImportFileLocation {
		
		CLASSPTH, FILE_SYSTEM
	}

}
