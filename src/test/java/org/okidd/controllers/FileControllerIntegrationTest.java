package org.okidd.controllers;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okidd.Application;
import org.okidd.repositories.FileRepository;
import org.okidd.testutils.IntegrationTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.okidd.testutils.IntegrationTestUtils.testFileName1;
import static org.okidd.testutils.IntegrationTestUtils.testFileName2;
import static org.okidd.testutils.IntegrationTestUtils.testFileVersion1;
import static org.okidd.testutils.IntegrationTestUtils.testFileVersion1_2;
import static org.okidd.testutils.IntegrationTestUtils.testFileVersion2;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author octaviokidd
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@AutoConfigureCache
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@ImportAutoConfiguration
public class FileControllerIntegrationTest {
	
	private static final String mockFileName = "test-file.mock";
	private static final String mockFileContentType = "image/jpeg";
	private static final byte[] mockFileContent = mockFileContentType.getBytes();
	
	@Autowired
	private MockMvc mvc;

	@Autowired
	private FileRepository fileRepository;
	
	@Before
	public void setUpBeforeClass() {
		IntegrationTestUtils.createThreeFiles(fileRepository);
	}
	
	@After
	public void cleanUp() {
		IntegrationTestUtils.deleteThreeFiles(fileRepository);
	}
	
	@Test
	public void testListFileVersions_findAll_returnFileInfoJsonArrayOrderedByNameAscVersionDesc() throws Exception {
		mvc.perform(get("/file/list"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is(testFileName2)))
				.andExpect(jsonPath("$[0].version").value(is(testFileVersion2), Long.class))
				.andExpect(jsonPath("$[1].name", is(testFileName1)))
				.andExpect(jsonPath("$[1].version").value(is(testFileVersion1_2), Long.class))
				.andExpect(jsonPath("$[2]name", is(testFileName1)))
				.andExpect(jsonPath("$[2].version").value(is(testFileVersion1), Long.class));
	}
	
	@Test
	public void testUploadDownloadDeleteNewFile_validNewFile_fileCreatedDownloadedAndDeleted() throws Exception {
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", mockFileName, mockFileContentType,
				mockFileContent);
		
		mvc.perform(multipart("/file/add_new").file(mockMultipartFile)).andExpect(status().isOk());
		
		MvcResult result = mvc.perform(get("/file?filename=" + mockFileName)).andExpect(status().isOk())
				.andReturn();
		
		assertArrayEquals("Unexpected file content", mockFileContent, result.getResponse().getContentAsByteArray());
		
		mvc.perform(delete("/file?filename=" + mockFileName + "&version=1")).andExpect(status().isNoContent());
	}
	
	@Test
	public void testDownloadFile_invalidFile_statusNotFound() throws Exception {
		mvc.perform(get("/file?filename=somebadfilename.jpog")).andExpect(status().isNotFound());
	}
	
}
