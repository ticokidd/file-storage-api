package org.okidd.services;

import org.okidd.entities.File;
import org.okidd.entities.FileInfo;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.InvalidTransactionException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author octaviokidd
 */
@Service
@Qualifier("inMemoryFileService")
public class InMemoryFileServiceImpl extends FileServiceAbstractImpl implements FileService {
	
	private static final Set<File> storage = new HashSet<>();
	
	@Override
	public File saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException {
		String filename = getFileName(multipartFile);
		
		if (storage.stream().anyMatch(file -> file.getName().equals(filename))) {
			throw new InvalidTransactionException("Version(s) already exist for that file, cannot create anew.");
		}
		
		File newFile = new File(filename, 1L, multipartFile.getContentType(), multipartFile.getBytes());
		storage.add(newFile);
		
		return newFile;
	}
	
	@Override
	public File saveNewFileVersion(MultipartFile multipartFile) throws IOException, IllegalArgumentException {
		String filename = getFileName(multipartFile);
		
		File latestFile = findLatest(filename);
		if (latestFile == null) {
			throw new InvalidTransactionException("No version exists for that file, cannot create update version.");
		}
		
		File updatedFile = new File(filename, latestFile.getVersion() + 1L, multipartFile.getContentType(),
				multipartFile.getBytes());
		storage.add(updatedFile);
		
		return updatedFile;
	}
	
	@Override
	public File findLatest(String filename) {
		File rVal = null;
		for (File file : storage) {
			if (file.getName().equals(filename) && (rVal == null || file.getVersion() > rVal.getVersion())) {
				rVal = file;
			}
		}
		return rVal;
	}
	
	@Override
	public File findVersion(String filename, Long version) {
		List<File> result = storage.stream().filter(
				file -> (file.getName().equals(filename) && file.getVersion().equals(version)))
				.collect(Collectors.toList());
		
		return !result.isEmpty() ? result.get(0) : null;
	}
	
	@Override
	public Iterable<FileInfo> findAllVersions(String filename) {
		return storage.stream().filter(file -> file.getName().equals(filename)).map(FileInfo::new)
				.collect(Collectors.toList());
	}
	
	@Override
	public Iterable<FileInfo> findAll() {
		return storage.stream().map(FileInfo::new).collect(Collectors.toList());
	}
	
	@Override
	public void deleteVersion(String filename, Long version) {
		storage.remove(new File(filename, version));
	}
	
	@Override
	public void deleteAllVersions(String filename) {
		storage.removeIf(file -> file.getName().equals(filename));
	}
}
