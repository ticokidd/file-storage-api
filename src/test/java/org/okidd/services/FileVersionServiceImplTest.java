package org.okidd.services;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.okidd.entities.FileVersion;
import org.okidd.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.InvalidTransactionException;
import java.io.IOException;

/**
 * @author octaviokidd
 */
@RunWith(SpringRunner.class)
public class FileVersionServiceImplTest {
	
	private static final String mockFileName = "test-file.mock";
	private static final Long mockFileVersion = 1L;
	private static final String mockFileContentType = "image/jpeg";
	private static final byte[] mockFileContent = mockFileContentType.getBytes();
	private static final String mockNewFileName = "new-file.mock";

	@TestConfiguration
	static class FileServiceImplTestContextConfig {
		@Bean
		public FileService fileService() {
			return new FileServiceImpl();
		}
	}
	
	@Autowired
	private FileService fileService;
	
	@MockBean
	private FileRepository fileRepository;
	
	@Before
	public void setUp() {
		FileVersion file = new FileVersion(mockFileName, mockFileVersion, mockFileContentType, mockFileContent);
		Mockito.when(fileRepository.findTopByNameAndVersion(mockFileName, mockFileVersion)).thenReturn(file);
		
		Mockito.when(fileRepository.findTopByNameOrderByVersionDesc(mockNewFileName)).thenReturn(null);
		
		FileVersion newFile = new FileVersion(mockNewFileName, mockFileVersion, mockFileContentType, mockFileContent);
		FileVersion newFileWithId = new FileVersion(mockNewFileName, mockFileVersion, mockFileContentType, mockFileContent);
		newFileWithId.setId(1L);
		Mockito.when(fileRepository.save(newFile)).thenReturn(newFileWithId);
		
		Mockito.when(fileRepository.existsByName(mockNewFileName)).thenReturn(false);
		Mockito.when(fileRepository.existsByName(mockFileName)).thenReturn(true);
	}
	
	@Test
	public void testFindVersion_validNameAndVersion_fileFound() {
		FileVersion file = fileService.findVersion(mockFileName, mockFileVersion);
		assertEquals("File name not expected", mockFileName, file.getName());
	}
	
	@Test
	public void testSaveNewFile_validNewFile_fileSaved() throws IOException {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(mockNewFileName, mockNewFileName,
				mockFileContentType, mockFileContent);
		
		FileVersion file = fileService.saveNewFile(mockMultipartFile);
		
		assertNotNull("Id should not be null", file.getId());
		assertEquals("File name not expected", mockNewFileName, file.getName());
		assertEquals("Version number should be 1 for new files", 1L, file.getVersion().longValue());
	}
	
	@Test
	public void testSaveNewFile_newFileVersion_cannotSaveNewFileVersion() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(mockFileName, mockFileName, mockFileContentType,
				mockFileContent);
		
		assertThrows("New file should not be allowed when versions already exist",
				InvalidTransactionException.class, () -> fileService.saveNewFile(mockMultipartFile));
	}
	
	@Test
	public void testSaveNewFileVersion_newFile_cannotSaveNewFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(mockNewFileName, mockNewFileName,
				mockFileContentType, mockFileContent);
		
		assertThrows("New file version should not be allowed when no versions exist yet",
				InvalidTransactionException.class, () -> fileService.saveNewFileVersion(mockMultipartFile));
	}
	
	public void testSaveNewFile_fileWithoutName_cannotSaveFile() {
		MockMultipartFile mockMultipartFile = new MockMultipartFile(" ", " ", mockFileContentType, mockFileContent);
		
		assertThrows("New file  should not be allowed without name",
				IllegalArgumentException.class, () -> fileService.saveNewFile(mockMultipartFile));
	}

}
