package org.okidd.services;

import org.okidd.entities.File;
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
	
	/**
	 *
	 * @param multipartFile
	 * @return
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	@Override
	public File saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException {
		String filename = getFileName(multipartFile);
		
		Example<File> queryExample = Example.of(new File(filename, null, null));
		if (fileRepository.count(queryExample)  > 0) {
			throw new InvalidTransactionException("Version(s) already exist for that file, cannot create anew.");
		}
		
		File newFile = new File(filename, 1L, multipartFile.getBytes());
		fileRepository.save(newFile);
		
		return newFile;
	}
	
	@Override
	public File saveNewFileVersion(MultipartFile multipartFile) throws IOException, IllegalArgumentException {
		String filename = getFileName(multipartFile);
		
		Example<File> queryExample = Example.of(new File(filename, null, null));
		Iterable<File> files = fileRepository.findAll(queryExample, Sort.by(Sort.Direction.DESC, "version"));
		
		File latestFile = files.iterator().hasNext() ? files.iterator().next() : null;
		
		if (latestFile == null) {
			throw new InvalidTransactionException("No version exists for that file, cannot create update version.");
		}
		
		File newVersion = new File(filename, latestFile.getVersion() + 1, multipartFile.getBytes());
		fileRepository.save(newVersion);
		
		return newVersion;
	}
	
	@Override
	public File findLatest(String name) {
		return null;
	}
	
	@Override
	public File findVersion(String name, Long version) {
		return null;
	}
	
	@Override
	public List<File> findAll() {
		return null;
	}
	
	@Override
	public List<Long> getVersionList(String name) {
		return null;
	}
	
	@Override
	public Boolean deleteVersion(String name, Long version) {
		return null;
	}
	
	@Override
	public Boolean deleteAllVersions(String name) {
		return null;
	}
	
	private String getFileName(MultipartFile multipartFile) {
		if (multipartFile == null || multipartFile.getOriginalFilename() == null) {
			throw new IllegalArgumentException(BAD_FILE_ERR);
		}
		
		return StringUtils.getFilename(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
	}
	
}
