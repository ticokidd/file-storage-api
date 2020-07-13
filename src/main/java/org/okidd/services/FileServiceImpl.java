package org.okidd.services;

import org.okidd.entities.File;
import org.okidd.entities.FileInfo;
import org.okidd.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.InvalidTransactionException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author octaviokidd
 */
@Service
@Qualifier("databaseFileService")
public class FileServiceImpl extends FileServiceAbstractImpl implements FileService {
	
	@Autowired
	private FileRepository fileRepository;
	
	@Override
	public File saveNewFile(MultipartFile multipartFile) throws IOException, IllegalArgumentException {
		String filename = getFileName(multipartFile);
		
		if (fileRepository.existsByName(filename)) {
			throw new InvalidTransactionException("Version(s) already exist for that file, cannot create anew.");
		}
		
		File newFile = new File(filename, 1L, multipartFile.getContentType(), multipartFile.getBytes());
		return fileRepository.save(newFile);
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
		return fileRepository.save(updatedFile);
	}
	
	@Override
	public File findLatest(String filename) {
		return fileRepository.findTopByNameOrderByVersionDesc(filename);
	}
	
	@Override
	public File findVersion(String filename, Long version) {
		return fileRepository.findTopByNameAndVersion(filename, version);
	}
	
	@Override
	public Iterable<FileInfo> findAllVersions(String filename) {
		return fileRepository.findAllByNameOrderByVersionDesc(filename, FileInfo.class);
	}
	
	/**
	 * <p>Retrieves a list of {@link FileInfo} objects sorted by name (asc) and version (desc).</p>
	 *
	 * <p>See {@link FileRepository#findBy()} for further information on why sorting is occurring at the application
	 * level instead of the database level.</p>
	 *
	 * @return a sorted list of {@link FileInfo} objects.
	 */
	@Override
	public Iterable<FileInfo> findAll() {
		List<FileInfo> unsortedResults = new ArrayList<>();
		fileRepository.findBy().forEach(unsortedResults::add);
		
		if (unsortedResults.isEmpty()) {
			return unsortedResults;
		}
		
		// calling reversed() affects all comparisons declared up to that point; calling it only once at the end causes
		// the list to be sorted on name and version, both descending
		Comparator<FileInfo> compareByNameThenVersion = Comparator.comparing(FileInfo::getName).reversed()
				.thenComparing(FileInfo::getVersion).reversed();
		
		return unsortedResults.stream().sorted(compareByNameThenVersion).collect(Collectors.toList());
	}
	
	@Override
	public void deleteVersion(String filename, Long version) {
		fileRepository.deleteByNameAndVersion(filename, version);
	}
	
	@Override
	public void deleteAllVersions(String filename) {
		fileRepository.deleteAllByName(filename);
	}
	
}
