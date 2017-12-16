package com.rabbithole.mongodb.apps;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Sort.Direction;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbithole.mongodb.aggregators.ZipCodeAggregator;
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
public class ZipCodeMongoDbApplication {
	
	private final static String ZIPCODE_OBJID_LOG_PREFIX = "ZipCode.ObjId: ";
	private final static String ZIPCODE_LOG_PREFIX = "ZipCode: ";
	private final static String STATEPOPULATION_LOG_PREFIX = "StatePopulation: ";

	@Getter(value = AccessLevel.PROTECTED)
	private final ZipCodeService zipCodeService;

	@Getter(value = AccessLevel.PROTECTED)
	private final ZipCodeAggregator zipCodeAggregator;

	public void zipCodeTest() throws JsonParseException, JsonMappingException, IOException {
		
		log.info("Test ZipCode...");
		
		importZipCodes(ProjectConstants.IMPORT_JSON_FULL_FILEPATH);
		log.info(ProjectConstants.EMPTY_STRING);
		
		getStatesWithPopulationGreaterThan(10000000, Direction.DESC);
		log.info(ProjectConstants.EMPTY_STRING);

		insertNewZipCode();
		log.info(ProjectConstants.EMPTY_STRING);

		findZipCodesByCity("CHICOPEE");
		log.info(ProjectConstants.EMPTY_STRING);

		getSmallestStateByAvgCityPopulation();
		
		deleteAllZipCodes();
	}

	private void importZipCodes(final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException {
		
		getZipCodeService().importFromJsonFile(
				jsonFilePath, FileUtils.ImportFileLocation.CLASSPTH);
	}
	
	private void getStatesWithPopulationGreaterThan(final long populationThreshold, final Direction sortingDirection) {
		
		getZipCodeAggregator().getStatesWithPopulationGreaterThan(populationThreshold, sortingDirection)
				.forEach(
						s -> log.info(STATEPOPULATION_LOG_PREFIX + s));
	}

	private void findZipCodesByCity(final String city) {

		final List<ZipCode> codes = getZipCodeService().findByCity(city);
		codes.forEach(zc -> {
			log.info(ZIPCODE_OBJID_LOG_PREFIX + zc.getId());

			log.info(ZIPCODE_LOG_PREFIX + zc);
		});
	}
	
	private void insertNewZipCode() {

		final ZipCode code = ZipCode.builder()
				.zipCode("20100")
				.city("Lost village")
				.location(new Double[] { 47.123456d, 58.777d })
				.population(Long.valueOf(666))
				.state("Neverland")
				.build();
		
		final ZipCode before = getZipCodeService().insert(code);
		log.info(ZIPCODE_OBJID_LOG_PREFIX + before.getId());
		log.info(ZIPCODE_LOG_PREFIX + before);
	}

	private void getSmallestStateByAvgCityPopulation() {
		
		log.info(STATEPOPULATION_LOG_PREFIX
				+ getZipCodeAggregator().getSmallestStateByAvgCityPopulation());
	}
	
	private void deleteAllZipCodes() {
		
		getZipCodeService().deleteAll();
	}

}
