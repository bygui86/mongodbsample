package com.rabbithole.mongodb.constants;

public final class ProjectConstants {

	private ProjectConstants() {}
	
	// general
	public final static String EMPTY_STRING = "";

	// logs
	public static final String LOG_NEWLINE = "\n";
	public static final String LOG_DOUBLE_NEWLINE = LOG_NEWLINE + LOG_NEWLINE;
	public static final String LOG_TRIPLE_NEWLINE = LOG_DOUBLE_NEWLINE + LOG_NEWLINE;
	public static final String LOG_INDENT = "\t";
	public static final String LOG_DOUBLE_INDENT = LOG_INDENT + LOG_INDENT;
	public static final String LOG_TRIPLE_INDENT = LOG_DOUBLE_INDENT + LOG_INDENT;
	
	// daos fields
	// user
	public static final String USER_ENTITY_ID = "user";
	public static final String USER_FIELDS_ID = "_id";
	public static final String USER_FIELDS_EMAIL = "email";
	public static final String USER_FIELDS_NAME = "name";
	public static final String USER_FIELDS_SURNAME = "surname";
	public static final String USER_FIELDS_BDAY = "birthday";
	public static final String USER_FIELDS_ADDRESS = "address";
	// address
	public static final String ADDRESS_ENTITY_ID = "address";
	public static final String ADDRESS_FIELDS_ID = "_id";
	public static final String ADDRESS_FIELDS_STREETNAME = "streetName";
	public static final String ADDRESS_FIELDS_STREETNUMBER = "streetNumber";
	public static final String ADDRESS_FIELDS_CITY = "city";
	public static final String ADDRESS_FIELDS_COUNTRY = "country";
	// gridfs
	public static final String GRIDFS_FIELDS_ID = "_id";
	public static final String GRIDFS_METADATA_CRITERIA_PREFIX = "metadata.";
	// zipcodes
	public static final String ZIPCODE_ENTITY_ID = "zipCode";
	public static final String ZIPCODE_FIELDS_CITY = "city";
	public static final String ZIPCODE_FIELDS_STATE = "state";
	public static final String ZIPCODE_FIELDS_POPULATION = "population";
	// statepopulation
	public static final String STATEPOPULATION_FIELDS_ID = "_id";
	public static final String STATEPOPULATION_FIELDS_STATE = "state";
	public static final String STATEPOPULATION_FIELDS_TOTALPOPULATION = "totalPopulation";
	public static final String STATEPOPULATION_TEMP_FIELDS_CITYPOPULATION = "cityPopulation";
	public static final String STATEPOPULATION_TEMP_FIELDS_AVGCITYPOPULATION = "avgCityPopulation";

	// queries
	public static final String QUERY_FIND_USERS_BY_REGEXP_NAME = "{ name : { $regex: ?0 } }";
	public static final String QUERY_VALUE_FIND_USERS_ONLY_EMAILS = "{}";
	public static final String QUERY_FIELDS_FIND_USERS_ONLY_EMAILS = "{ id: 1, email: 1 }";
	
	// import/export
	public static final String IMPORT_JSON_TEST_FILEPATH = "import/usa-zipcodes-test.json";
	public static final String IMPORT_JSON_SINGLE_FILEPATH = "import/usa-zipcodes-single.json";
	public static final String IMPORT_JSON_PARTIAL_FILEPATH = "import/usa-zipcodes-partial.json";
	public static final String IMPORT_JSON_FULL_FILEPATH = "import/usa-zipcodes-full.json";
	public static final String EXPORT_JSON_FILEPATH = "/export/zipcodes.json";
	
}
