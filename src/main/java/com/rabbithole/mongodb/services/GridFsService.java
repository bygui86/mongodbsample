package com.rabbithole.mongodb.services;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsResource;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;

/**
 * The GridFS storage spec is mainly used for working with files that exceed the BSON-document size limit of 16MB.
 *
 * GridFS uses two collections to store the file metadata and its content. The file’s metadata is stored in the files collection,
 * and the file’s content is stored in the chunks collection. Both collections are prefixed with fs.
 */
public interface GridFsService {
	
	/**
	 * Store a file
	 *
	 * @param filepath
	 * @param filename
	 * @param mimetype
	 * @param metadataMap
	 *
	 * @return {@link com.mongodb.gridfs.GridFSFile}
	 *
	 * @throws FileNotFoundException
	 */
	GridFSFile storeFile(final String filepath, final String filename,
			final String mimetype, final Map<String, Object> metadataMap) throws FileNotFoundException;
	
	/**
	 * Find all files
	 *
	 * @return List of {@link com.mongodb.gridfs.GridFSDBFile}
	 */
	List<GridFSDBFile> findAllFiles();

	/**
	 * Find a file by id
	 *
	 * @param id (ObjectId)
	 *
	 * @return {@link com.mongodb.gridfs.GridFSDBFile}
	 */
	Optional<GridFSDBFile> findFileById(final ObjectId id);

	/**
	 * Find a file by id
	 *
	 * @param objId (Object)
	 *
	 * @return {@link com.mongodb.gridfs.GridFSDBFile}
	 */
	Optional<GridFSDBFile> findFileById(final Object objId);
	
	/**
	 * Find a file by id
	 *
	 * @param hexStringId (String)
	 *
	 * @return {@link com.mongodb.gridfs.GridFSDBFile}
	 */
	Optional<GridFSDBFile> findFileById(final String hexStringId);

	/**
	 * Find all files by specified metadata info
	 *
	 * @param metadataMap
	 *
	 * @return List of {@link com.mongodb.gridfs.GridFSDBFile}
	 */
	List<GridFSDBFile> findFilesByMetadata(final Map<String, Object> metadataMap);
	
	/**
	 * Get resources by file name
	 *
	 * @param filenamePattern
	 *
	 * @return List of {@link org.springframework.data.mongodb.gridfs.GridFsResource}
	 */
	List<GridFsResource> getResourcesByFilenamePattern(final String filenamePattern);

	/**
	 * Get resource by file name
	 *
	 * @param filename
	 *
	 * @return {@link org.springframework.data.mongodb.gridfs.GridFsResource}
	 */
	Optional<GridFsResource> getResourceByFilename(final String filename);
	
	/**
	 * Delete all files
	 */
	void deleteAllFiles();
	
	/**
	 * Delete a file by id
	 *
	 * @param id
	 */
	void deleteFileById(final ObjectId id);

	/**
	 * Delete a file by id
	 *
	 * @param objId
	 */
	void deleteFileById(final Object objId);

	/**
	 * Delete a file by id
	 *
	 * @param hexStringId
	 */
	void deleteFileById(final String hexStringId);
	
}
