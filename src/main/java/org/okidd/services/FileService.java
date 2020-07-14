package org.okidd.services;

import org.okidd.entities.FileVersion;
import org.okidd.dtos.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author octaviokidd
 */
public interface FileService {
	
	/**
	 * Saves brand new file. Does not accept files with names that match existing records.
	 *
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	FileVersion saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException;
	
	/**
	 * Saves new file version. Does not accept files with name that do not match existing records.
	 *
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	FileVersion saveNewFileVersion(MultipartFile multipartFile) throws IOException, IllegalArgumentException;
	
	/**
	 * Retrieves latest version of a given file
	 *
	 * @param filename
	 * @return
	 */
	FileVersion findLatest(String filename);
	
	/**
	 *
	 * @param filename
	 * @param version
	 * @return
	 */
	FileVersion findVersion(String filename, Long version);
	
	/**
	 *
	 * @param filename
	 * @return
	 */
	Iterable<FileInfo> findAllVersions(String filename);
	
	/**
	 *
	 *
	 * @return
	 */
	Iterable<FileInfo> findAll();
	
	/**
	 *
	 * @param filename
	 * @param version
	 */
	void deleteVersion(String filename, Long version);
	
	/**
	 *
	 * @param filename
	 */
	void deleteAllVersions(String filename);

}
