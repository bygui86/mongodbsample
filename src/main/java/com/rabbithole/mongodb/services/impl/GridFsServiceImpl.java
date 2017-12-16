package com.rabbithole.mongodb.services.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import javax.annotation.Resource;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.rabbithole.mongodb.constants.ProjectConstants;
import com.rabbithole.mongodb.services.GridFsService;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("gridFsService")
public class GridFsServiceImpl implements GridFsService {
	
	@Resource
	@Getter(value = AccessLevel.PROTECTED)
	@Setter
	private GridFsOperations gridFsOperations;

	@Override
	public GridFSFile storeFile(final String filepath, final String filename, final String mimetype, final Map<String, Object> metadataMap) throws FileNotFoundException {
		
		log.debug("Store file: " + filepath);

		final InputStream inputStream = new FileInputStream(filepath);
		if (metadataMap != null && !metadataMap.isEmpty()) {
			final DBObject metaData = buildDbObject(metadataMap);
			return getGridFsOperations().store(inputStream, filename, mimetype, metaData);
		} else {
			return getGridFsOperations().store(inputStream, filename, mimetype);
		}
	}

	protected DBObject buildDbObject(final Map<String, Object> metadataMap) {

		final DBObject metadata = new BasicDBObject();
		metadataMap.forEach(
				(k, v) -> metadata.put(k, v));
		return metadata;
	}

	@Override
	public List<GridFSDBFile> findAllFiles() {
		
		log.debug("Find all files");

		return findByQuery(null);
	}

	protected List<GridFSDBFile> findByQuery(final Query query) {
		
		return getGridFsOperations().find(query);
	}

	@Override
	public Optional<GridFSDBFile> findFileById(final ObjectId id) {

		log.debug("Find file by id: " + id);
		
		return Optional.ofNullable(
				getGridFsOperations().findOne(
						new Query(
								Criteria.where(ProjectConstants.GRIDFS_FIELDS_ID).is(id))));
	}

	@Override
	public Optional<GridFSDBFile> findFileById(final Object objId) {

		log.debug("Find file by id: " + objId);
		
		return Optional.ofNullable(
				getGridFsOperations().findOne(
						new Query(
								Criteria.where(ProjectConstants.GRIDFS_FIELDS_ID).is(objId.toString()))));
	}

	@Override
	public Optional<GridFSDBFile> findFileById(final String hexStringId) {
		
		log.debug("Find file by id: " + hexStringId);
		
		return Optional.ofNullable(
				getGridFsOperations().findOne(
						new Query(
								Criteria.where(ProjectConstants.GRIDFS_FIELDS_ID).is(hexStringId))));
	}
	
	@Override
	public List<GridFSDBFile> findFilesByMetadata(final Map<String, Object> metadataMap) {
		
		log.debug("Find files by metadata: " + metadataMap);

		return findByQuery(new Query(buildCriteria(metadataMap)));
	}

	protected Criteria buildCriteria(final Map<String, Object> metadataMap) {
		
		Criteria criteria = new Criteria();
		
		final boolean flagFirst = true;
		for (final Entry<String, Object> e : metadataMap.entrySet()) {
			if (flagFirst) {
				criteria = Criteria.where(ProjectConstants.GRIDFS_METADATA_CRITERIA_PREFIX + e.getKey()).is(e.getValue());
			} else {
				criteria.and(ProjectConstants.GRIDFS_METADATA_CRITERIA_PREFIX + e.getKey()).is(e.getValue());
			}
		}
		return criteria;
	}

	@Override
	public List<GridFsResource> getResourcesByFilenamePattern(final String filenamePattern) {

		log.debug("Get resources by filename pattern: " + filenamePattern);
		
		return Arrays.asList(getGridFsOperations().getResources(filenamePattern));
	}

	@Override
	public Optional<GridFsResource> getResourceByFilename(final String filename) {

		log.debug("Get resource by filename: " + filename);
		
		return Optional.ofNullable(
				getGridFsOperations().getResource(filename));
	}
	
	@Override
	public void deleteAllFiles() {

		log.debug("Delete all files");
		
		deleteByQuery(null);
	}
	
	protected void deleteByQuery(final Query query) {

		getGridFsOperations().delete(query);
	}
	
	@Override
	public void deleteFileById(final ObjectId id) {

		log.debug("Delete file by id: " + id);
		
		deleteByQuery(
				buildDeleteQueryById(id.toHexString()));
	}

	@Override
	public void deleteFileById(final Object objId) {
		
		log.debug("Delete file by id: " + objId);
		
		deleteByQuery(
				buildDeleteQueryById(objId.toString()));
	}

	@Override
	public void deleteFileById(final String hexStringId) {
		
		log.debug("Delete file by id: " + hexStringId);
		
		deleteByQuery(
				buildDeleteQueryById(hexStringId));
	}
	
	private Query buildDeleteQueryById(final String id) {
		
		return new Query(Criteria.where(ProjectConstants.GRIDFS_FIELDS_ID).is(id));
	}

}
