package com.rabbithole.mongodb.services.impl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.rabbithole.mongodb.aggregators.ZipCodeAggregator;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.daos.ZipCode;
import com.rabbithole.mongodb.repos.ZipCodeRepository;
import com.rabbithole.mongodb.services.ZipCodeService;
import com.rabbithole.mongodb.utils.FileUtils;
import com.rabbithole.mongodb.utils.FileUtils.ImportFileLocation;
import com.rabbithole.mongodb.utils.JsonUtils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("zipCodeService")
public class ZipCodeServiceImpl implements ZipCodeService {

	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private ZipCodeRepository zipCodeRepository;
	
	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private ZipCodeAggregator zipCodeAggregator;
	
	@Override
	public ZipCode insert(final ZipCode code) {

		log.debug("Insert new ZipCode: " + code);
		
		return getZipCodeRepository().insert(code);
	}

	@Override
	public List<ZipCode> insert(final List<ZipCode> codes) {

		if (log.isDebugEnabled()) {
			log.debug("Insert " + codes.size() + " ZipCodes:");
			codes.forEach(
					zc -> log.debug(ProjectConstants.LOG_INDENT + zc));
		}

		return getZipCodeRepository().insert(codes);
	}
	
	@Override
	public ZipCode insertOrUpdate(final ZipCode code) {

		log.debug("Insert or Update ZipCode: " + code);
		
		return getZipCodeRepository().insert(code);
	}

	@Override
	public List<ZipCode> insertOrUpdate(final List<ZipCode> codes) {
		
		if (log.isDebugEnabled()) {
			log.debug("Insert or Update " + codes.size() + " ZipCodes:");
			codes.forEach(
					zc -> log.debug(ProjectConstants.LOG_INDENT + zc));
		}
		
		return getZipCodeRepository().insert(codes);
	}
	
	@Override
	public List<ZipCode> findAll() {

		log.debug("Find all ZipCodes");
		
		return getZipCodeRepository().findAll();
	}

	@Override
	public ZipCode findByExample(final ZipCode code) {

		log.debug("Find ZipCodes by example code: " + code);
		
		return getZipCodeRepository().findOne(
				Example.of(code));
	}

	@Override
	public List<ZipCode> findByCity(final String city) {

		log.debug("Find ZipCodes by city: " + city);
		
		return getZipCodeRepository().findByCity(city);
	}
	
	@Override
	public void delete(final ZipCode code) {

		log.debug("Delete ZipCode: " + code);
		
		getZipCodeRepository().delete(code);
	}

	@Override
	public void delete(final List<ZipCode> codes) {

		if (log.isDebugEnabled()) {
			log.debug("Delete " + codes.size() + " ZipCodes:");
			codes.forEach(
					zc -> log.debug(ProjectConstants.LOG_INDENT + zc));
		}

		getZipCodeRepository().delete(codes);
	}
	
	@Override
	public void deleteAll() {

		log.debug("Delete all ZipCodes");
		
		getZipCodeRepository().deleteAll();
	}

	@Override
	public void importFromJsonFile(final String jsonFilePath, final ImportFileLocation fileLocation)
			throws JsonParseException, JsonMappingException, IOException {

		log.debug("Import ZipCodes from JSON file " + jsonFilePath + " in " + fileLocation);

		insertOrUpdate(
				FileUtils.loadAndParseJsonFile(
						jsonFilePath, FileUtils.ImportFileLocation.CLASSPTH, ZipCode.class));
	}

	@Override
	public void exportToJsonFile(final List<ZipCode> codes, final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException {
		
		log.debug("Export " + codes.size() + " ZipCodes to file " + jsonFilePath);
		
		final Optional<File> optFolder = FileUtils.getProjectResourcesFolder(true);
		if (optFolder.isPresent()) {
			final Optional<File> optFile = FileUtils.createNewFile(optFolder.get().getAbsolutePath() + jsonFilePath, true);
			if (optFile.isPresent()) {
				JsonUtils.bulkSerializeObjectsToJsonFile(optFile.get(), codes);
			} else {
				log.error("Export failed: creation of file " + jsonFilePath + " failed");
			}
		} else {
			log.error("Export failed: project resources folder NOT FOUND");
		}
	}
	
	@Override
	public void exportAllToJsonFile(final String jsonFilePath)
			throws JsonParseException, JsonMappingException, IOException {
		
		log.debug("Export all ZipCodes to file " + jsonFilePath);
		
		exportToJsonFile(
				findAll(), jsonFilePath);
	}

}
