package org.okidd.services;

import org.okidd.entities.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author octaviokidd
 */
public interface FileService {
	/*
		- get latest
		- get version list
		- get old version
		- get list of all files
		- add new (fails if at least one version exists)
		- update (fails if no version exists)
		- delete version
		- delete all versions
	*/

	File saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException;
	
	File saveNewFileVersion(MultipartFile multipartFile) throws IOException;
	
	File findLatest(String name);
	
	File findVersion(String name, Long version);
	
	List<File> findAll();
	
	List<Long> getVersionList(String name);
	
	Boolean deleteVersion(String name, Long version);
	
	Boolean deleteAllVersions(String name);

}
