package org.okidd.repositories;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okidd.entities.FileVersion;
import org.okidd.dtos.FileInfo;
import org.okidd.testutils.IntegrationTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;
import static org.okidd.testutils.IntegrationTestUtils.testFileContent1;
import static org.okidd.testutils.IntegrationTestUtils.testFileContentType1;
import static org.okidd.testutils.IntegrationTestUtils.testFileName1;
import static org.okidd.testutils.IntegrationTestUtils.testFileVersion1;

/**
 * @author octaviokidd
 */
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:application-test.properties")
@DataJpaTest
public class FileVersionRepositoryIntegrationTest {
	
	@Autowired
	private FileRepository fileRepository;
	
	@Before
	public void setUp() {
		IntegrationTestUtils.createThreeFiles(fileRepository);
	}
	
	@After
	public void cleanUp() {
		IntegrationTestUtils.deleteThreeFiles(fileRepository);
	}
	
	@Test
	public void testFindAllProjected_filesExist_allFileInfosReturned() {
		Iterable<FileInfo> queryResult = fileRepository.findBy();
		assertTrue("Query result should not be empty", queryResult.iterator().hasNext());

		List<FileInfo> results = new ArrayList<>();
		fileRepository.findBy().forEach(results::add);

		assertEquals("Incorrect number of results", 3, results.size());
	}
	
	@Test
	public void testFindAllVersionProjected_versionsExist_allFileInfosForFileNameReturned() {
		Collection<FileInfo> queryResult = fileRepository.findAllByNameOrderByVersionDesc(testFileName1, FileInfo.class);
		
		assertEquals("Query result should not be empty", 2, queryResult.size());
		FileInfo[] results = queryResult.toArray(new FileInfo[0]);
		assertEquals("First result should have latest version", 2L, results[0].getVersion().longValue());
		assertEquals("Last result should have oldest version", 1L, results[1].getVersion().longValue());
	}
	
	@Test
	public void testFindByNameAndVersion_fileExists_fileReturned() {
		FileVersion retrievedFile = fileRepository.findTopByNameAndVersion(testFileName1, testFileVersion1);
		assertNotNull("Missing auto-generated id", retrievedFile.getId());
		assertEquals("Name changed on save/retrieval", testFileName1, retrievedFile.getName());
		assertEquals("Version changed on save/retrieval", testFileVersion1, retrievedFile.getVersion());
		assertEquals("Content type changed on save/retrieval", testFileContentType1, retrievedFile.getContentType());
		assertNotNull("Missing auto-generated creation timestamp", retrievedFile.getCreated());
		assertEquals("Content changed on save/retrieval", testFileContent1, retrievedFile.getContent());
	}
	
	@Test
	public void testUniqueConstraintOnNameVersion_nameVersionDupleExists_saveFails() {
		FileVersion repeatedNameVersionFile = new FileVersion(testFileName1, testFileVersion1, testFileContentType1, testFileContent1);
		String failedAssertMsg = "Name-Version constraint on File entity not working.";
		assertThrows(failedAssertMsg, DataIntegrityViolationException.class,
				() -> fileRepository.save(repeatedNameVersionFile));
	}
	
}
