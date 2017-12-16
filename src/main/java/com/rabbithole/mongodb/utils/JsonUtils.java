package com.rabbithole.mongodb.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * TODO
 * have a look to
 * 		{@link org.springframework.data.mongodb.core.query.SerializationUtils#serializeToJsonSafely(Object)}
 * 		{@link com.mongodb.util.JSON#parse(Object)}
 * 		{@link com.mongodb.util.JSON#serialize(Object)}
 */
// @Slf4j
public final class JsonUtils {
	
	private JsonUtils() {}
	
	/*
	 * TODO [best-effort]
	 * implement methods bulkReadAndParseJsonToObjects
	 * to read a json file line by line and converting them to dao
	 */
	
	/*****************
	 * SERIALIZE: OBJECT >>> JSON
	 *****************/

	/**
	 * Serialize an Object {@link T} to a JSON string
	 *
	 * @param object
	 *
	 * @return {@link java.lang.String}
	 *
	 * @throws JsonProcessingException
	 */
	public static <T> String serializeObjectToJsonString(final T object, final boolean prettyPrint)
			throws JsonProcessingException {

		return writeValueAsString(object, prettyPrint);
	}

	/**
	 * Bulk serialize a {@link java.util.List} of Object {@link T} to a JSON string
	 *
	 * @param objects
	 *
	 * @return {@link java.lang.String}
	 *
	 * @throws JsonProcessingException
	 */
	public static <T> String bulkSerializeObjectsToJsonString(final List<T> objects, final boolean prettyPrint)
			throws JsonProcessingException {

		return writeValueAsString(objects, prettyPrint);
	}

	/**
	 * Serialize an Object {@link T} to a JSON file (OutputStream)
	 *
	 * @param outputStream
	 * @param object
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static <T> void serializeObjectToJsonFile(final OutputStream outputStream, final T object)
			throws JsonGenerationException, JsonMappingException, IOException {

		writeValueToOutputStream(outputStream, object);
	}
	
	/**
	 * Serialize an Object {@link T} to a JSON file (File)
	 *
	 * @param file
	 * @param object
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static <T> void serializeObjectToJsonFile(final File file, final T object)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		writeValueToFile(file, object);
	}

	/**
	 * Bulk serialize a {@link java.util.List} of Object {@link T} to a JSON file ({@link java.io.OutputStream})
	 *
	 * @param outputStream
	 * @param objects
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static <T> void bulkSerializeObjectsToJsonFile(final OutputStream outputStream, final List<T> objects)
			throws JsonGenerationException, JsonMappingException, IOException {

		writeValueToOutputStream(outputStream, objects);
	}

	/**
	 * Bulk serialize a {@link java.util.List} of Object {@link T} to a JSON file ({@link java.io.File})
	 *
	 * @param file
	 * @param objects
	 *
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 */
	public static <T> void bulkSerializeObjectsToJsonFile(final File file, final List<T> objects)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		writeValueToFile(file, objects);
	}
	
	private static String writeValueAsString(final Object genericObj, final boolean prettyPrint)
			throws JsonProcessingException {

		if (prettyPrint) {
			return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(genericObj);
		} else {
			return new ObjectMapper().writeValueAsString(genericObj);
		}
	}
	
	private static void writeValueToOutputStream(final OutputStream outputStream, final Object genericObj)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		new ObjectMapper().writeValue(outputStream, genericObj);
	}
	
	private static void writeValueToFile(final File file, final Object genericObj)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		new ObjectMapper().writeValue(file, genericObj);
	}
	
	/*****************
	 * PARSE: JSON >>> OBJECT
	 *****************/

	/**
	 * Parse a JSON String to single Object {@link T}
	 *
	 * @param jsonString
	 * @param clazz
	 *
	 * @return {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> T parseJsonToObject(final String jsonString, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		
		return new ObjectMapper().readValue(jsonString, clazz);
	}
	
	/**
	 * Parse a JSON String to a List of Object {@link T}
	 *
	 * *** PLEASE NOTE ***
	 * The JSON file (read through the InputStream) should contain a valid JSON array (comma-separated) of {@link T} objects, like following
	 * 		[ {{@link T}}, {{@link T}}, {{@link T}}, ... ]
	 * ***     ***     ***
	 *
	 * @param jsonString
	 * @param clazz
	 *
	 * @return {@link java.util.List} of {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> bulkParseJsonToObjects(final String jsonString, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(
				jsonString, buildCollectionJavaType(mapper, List.class, clazz));
	}

	/**
	 * Parse an InputStream to a List of Object {@link T}
	 *
	 * *** PLEASE NOTE ***
	 * The JSON file (read through the InputStream) should contain a valid JSON array (comma-separated) of {@link T} objects, like following
	 * 		[ {{@link T}}, {{@link T}}, {{@link T}}, ... ]
	 * ***     ***     ***
	 *
	 * @param jsonObjString
	 * @param clazz
	 *
	 * @return {@link java.util.List} of {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> bulkParseJsonToObjects(final InputStream jsonInputStream, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {

		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(
				jsonInputStream, buildCollectionJavaType(mapper, List.class, clazz));
	}
	
	/**
	 * Parse a JSON File (containing an array of JSON objects) to a List of Object {@link T}
	 *
	 * *** PLEASE NOTE ***
	 * The JSON file (read through the InputStream) should contain a valid JSON array (comma-separated) of {@link T} objects, like following
	 * 		[ {{@link T}}, {{@link T}}, {{@link T}}, ... ]
	 * ***     ***     ***
	 *
	 * @param jsonObjString
	 * @param clazz
	 *
	 * @return {@link java.util.List} of {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<T> bulkParseJsonToObjects(final File jsonfile, final Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(
				jsonfile, buildCollectionJavaType(mapper, List.class, clazz));
	}
	
	/**
	 * Build a collection {@link com.fasterxml.jackson.databind.JavaType}
	 *
	 * @param mapper
	 * @param collectionClass
	 * @param elementClass
	 *
	 * @return {@link com.fasterxml.jackson.databind.JavaType}
	 */
	public static JavaType buildCollectionJavaType(final ObjectMapper mapper, final Class<? extends Collection> collectionClass,
			final Class<?> elementClass) {
		
		return mapper.getTypeFactory()
				.constructCollectionType(collectionClass, elementClass);
	}
	
	/**
	 * Parse a JSON String to Object {@link T}
	 *
	 * *** PLEASE NOTE ***
	 * The {@link com.fasterxml.jackson.core.type.TypeReference<T>} is not able to infer the right Type of T, so it will return
	 * a generic LinkedHashMap.
	 * ***     ***     ***
	 *
	 * @param jsonObjStr
	 *
	 * @return {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Deprecated
	public static <T> T parseJsonToObject(final String jsonObjStr)
			throws JsonParseException, JsonMappingException, IOException {

		return new ObjectMapper().readValue(jsonObjStr, new TypeReference<T>() {});
	}
	
	/**
	 * Parse an InputStream to a List of Object {@link T}
	 *
	 * *** PLEASE NOTE ***
	 * The JSON file (read through the InputStream) should contain a valid JSON array (comma-separated) of {@link T} objects, like following
	 * 		[ {{@link T}}, {{@link T}}, {{@link T}}, ... ]
	 *
	 * BUT
	 *
	 * The {@link com.fasterxml.jackson.core.type.TypeReference<T>} is not able to infer the right Type of T, so it will return
	 * a generic LinkedHashMap.
	 * ***     ***     ***
	 *
	 * @param jsonInStream
	 *
	 * @return {@link java.util.List} of {@link T}
	 *
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	@Deprecated
	public static <T> List<T> parseJsonToMultipleObjects(final InputStream jsonInStream)
			throws JsonParseException, JsonMappingException, IOException {
		
		return new ObjectMapper().readValue(jsonInStream, new TypeReference<List<T>>() {});
	}
	
}
