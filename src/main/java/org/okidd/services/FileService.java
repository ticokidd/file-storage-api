package org.okidd.services;

import org.okidd.entities.FileVersion;
import org.okidd.dtos.FileInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * Service that handles {@link FileVersion}-related tasks.
 *
 * @author octaviokidd
 */
public interface FileService {
	
	/**
	 * Saves brand new file. Does not accept files with names that match existing records.
	 *
	 * @param multipartFile file to save
	 * @return saved entity with the assigned version, and with auto-populated id and created timestamp
	 * @throws IOException if the filename already exist in the system or if there was an issue reading the file's
	 * content
	 * @throws IllegalArgumentException if the file is null or if the file's name is null or empty
	 */
	FileVersion saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException;
	
	/**
	 * Saves new file version. Does not accept files with names that do not match existing records.
	 *
	 * @param multipartFile file to save
	 * @return saved entity with the assigned version, and with auto-populated id and created timestamp
	 * @throws IOException if the filename does not yet exist in the system or if there was an issue reading the file's
	 * content
	 * @throws IllegalArgumentException if the file is null or if the file's name is null or empty
	 */
	FileVersion saveNewFileVersion(MultipartFile multipartFile) throws IOException, IllegalArgumentException;
	
	/**
	 * Retrieves latest version of a given file.
	 *
	 * @param filename name of the file to find
	 * @return the latest version of the given filename, or null if no version exists for that filename
	 */
	FileVersion findLatest(String filename);
	
	/**
	 * Retrieves a specific file's version.
	 *
	 * @param filename name of the file to find
	 * @param version version of the file to find
	 * @return the requested version of the given filename, or null if the specified version does not exists for that
	 * filename
	 */
	FileVersion findVersion(String filename, Long version);
	
	/**
	 * Retrieves a list of {@link FileInfo} objects for the given filename. The list is ordered by version (desc).
	 *
	 * @param filename name of the file to find
	 * @return an ordered list of {@link FileInfo} objects; the list will be empty if no stored files exist that match
	 * the given filename
	 */
	Iterable<FileInfo> findAllVersions(String filename);
	
	/**
	 * Retrieves a list of {@link FileInfo} objects for all saved files. The list is ordered by filename (asc) and
	 * version (desc).
	 *
	 * @return an ordered list of {@link FileInfo} objects; the list is empty if no files exist in the system yet
	 */
	Iterable<FileInfo> findAll();
	
	/**
	 * Deletes a specific file's version.
	 *
	 * @param filename name of the file to delete
	 * @param version version of the file to delete
	 */
	void deleteVersion(String filename, Long version);
	
	/**
	 * Deletes all versions of a specific file.
	 *
	 * @param filename given filename for which all version will be deleted
	 */
	void deleteAllVersions(String filename);

}
