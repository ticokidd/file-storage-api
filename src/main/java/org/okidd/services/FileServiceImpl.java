package org.okidd.services;

import org.okidd.entities.File;
import org.okidd.entities.FileInfo;
import org.okidd.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.InvalidTransactionException;
import java.io.IOException;
import java.util.List;

/**
 * @author octaviokidd
 */
@Service
public class FileServiceImpl implements FileService {
	private static final String BAD_FILE_ERR = "File must not be null and it must have a name.";
	
	@Autowired
	private FileRepository fileRepository;
	
	@Override
	public File saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException {
		String filename = getFileName(multipartFile);
		
//		Example<File> queryExample = Example.of(new File(filename));
//		if (fileRepository.count(queryExample) > 0) {
		if (fileRepository.existsByName(filename)) {
			throw new InvalidTransactionException("Version(s) already exist for that file, cannot create anew.");
		}
		
		File newFile = new File(filename, 1L, multipartFile.getContentType(), multipartFile.getBytes());
		fileRepository.save(newFile);
		
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
		fileRepository.save(updatedFile);
		
		return updatedFile;
	}
	
	@Override
	public File findLatest(String filename) {
//		Example<File> queryExample = Example.of(new File(filename));
//		Iterable<File> files = fileRepository.findAll(queryExample, Sort.by(Sort.Direction.DESC, "version"));
//		return files.iterator().hasNext() ? files.iterator().next() : null;
		return fileRepository.findTopByNameOrderByVersionDesc(filename);
	}
	
	@Override
	public File findVersion(String filename, Long version) {
//		Example<File> queryExample = Example.of(new File(filename, version));
//		return fileRepository.findOne(queryExample).orElse(null);
		return fileRepository.findTopByNameAndVersion(filename, version);
	}
	
	@Override
	public Iterable<FileInfo> findAllVersions(String filename) {
		return fileRepository.findAllByNameOrderByVersionDesc(filename, FileInfo.class);
		
//		Example<File> queryExample = Example.of(new File(filename));
//		return fileRepository.findAll(queryExample);
	}
	
	@Override
	public Iterable<FileInfo> findAll() {
		return fileRepository.findAllOrderByNameAscAndVersionDesc(FileInfo.class);
	}
	
	@Override
	public void deleteVersion(String filename, Long version) {
		fileRepository.deleteByNameAndVersion(filename, version);
//		File toDelete = findVersion(filename, version);
//		if (toDelete != null) {
//			fileRepository.delete(toDelete);
//		}
	}
	
	@Override
	public void deleteAllVersions(String filename) {
		fileRepository.deleteAllByName(filename);
//		Iterable<File> toDelete = findAllVersions(filename);
//		if (toDelete.iterator().hasNext()) {
//			fileRepository.deleteAll(toDelete);
//		}
	}
	
	private String getFileName(MultipartFile multipartFile) {
		if (multipartFile == null || multipartFile.getOriginalFilename() == null) {
			throw new IllegalArgumentException(BAD_FILE_ERR);
		}
		
		return StringUtils.getFilename(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
	}
	
}
