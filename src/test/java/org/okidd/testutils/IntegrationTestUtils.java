package org.okidd.testutils;

import org.okidd.entities.FileVersion;
import org.okidd.repositories.FileRepository;

/**
 * @author octaviokidd
 */
public class IntegrationTestUtils {
	
	public static final String testFileName1 = "test-text.txt";
	public static final Long testFileVersion1 = 1L;
	public static final Long testFileVersion1_2 = 2L;
	public static final String testFileContentType1 = "plain/text";
	public static final byte[] testFileContent1 = testFileContentType1.getBytes();
	
	public static final String testFileName2 = "test-image.jpeg";
	public static final Long testFileVersion2 = 1L;
	public static final String testFileContentType2 = "image/jpeg";
	public static final byte[] testFileContent2 = testFileContentType2.getBytes();
	
	private static FileVersion testFile1;
	private static FileVersion testFile2;
	private static FileVersion testFile3;
	
	private IntegrationTestUtils() {}
	
	public static void createThreeFiles(FileRepository fileRepository) {
		testFile1 = new FileVersion(testFileName1, testFileVersion1, testFileContentType1, testFileContent1);
		testFile2 = new FileVersion(testFileName2, testFileVersion2, testFileContentType2, testFileContent2);
		testFile3 = new FileVersion(testFileName1, testFileVersion1_2, testFileContentType1, testFileContent1);
		fileRepository.save(testFile1);
		fileRepository.save(testFile2);
		fileRepository.save(testFile3);
	}
	
	public static void deleteThreeFiles(FileRepository fileRepository) {
		fileRepository.delete(testFile1);
		fileRepository.delete(testFile2);
		fileRepository.delete(testFile3);
	}
}
