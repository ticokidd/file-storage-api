package org.okidd.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.okidd.dtos.FileInfo;
import org.okidd.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author octaviokidd
 */
@RunWith(SpringRunner.class)
@WebMvcTest(FileController.class)
public class FileVersionControllerTest {
	
	private static final String mockFileName = "mock-file.txt";
	private static final Long mockFileVersion1 = 1L;
	private static final Long mockFileVersion2 = 2L;
	private static final String mockFileContentType = "text/plain";
	private static final String mockOtherFileName = "mock-other-file.jpeg";
	private static final Long mockOtherFileVersion = 1L;
	private static final String mockOtherFileContentType = "image/jpeg";
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private FileService fileService;
	
	@Before
	public void setUp() {
		FileInfo fileInfo1 = new FileInfo(mockFileName, mockFileVersion1, mockFileContentType);
		FileInfo fileInfo2 = new FileInfo(mockFileName, mockFileVersion2, mockFileContentType);
		FileInfo otherFileInfo = new FileInfo(mockOtherFileName, mockOtherFileVersion, mockOtherFileContentType);
		List<FileInfo> allInfos = Arrays.asList(fileInfo1, fileInfo2, otherFileInfo);
		List<FileInfo> fileInfos = Arrays.asList(fileInfo1, fileInfo2);
		
		given(fileService.findAll()).willReturn(allInfos);
		given(fileService.findAllVersions(mockFileName)).willReturn(fileInfos);
	}
	
	@Test
	public void testListFileVersions_findAll_returnFileInfoJsonArray() throws Exception {
		mvc.perform(get("/files/list"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)))
				.andExpect(jsonPath("$[0].name", is(mockFileName)))
				.andExpect(jsonPath("$[1].version").value(is(mockFileVersion2), Long.class))
				.andExpect(jsonPath("$[2]name", is(mockOtherFileName)));
	}
	
	@Test
	public void testListFileVersions_findAllVersions_returnFileInfoJsonArray() throws Exception {
		mvc.perform(get("/files/list?filename=" + mockFileName))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", is(mockFileName)))
				.andExpect(jsonPath("$[0].version").value(is(mockFileVersion1), Long.class))
				.andExpect(jsonPath("$[1].version").value(is(mockFileVersion2), Long.class));
	}
}
