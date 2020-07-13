package org.okidd.services;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author octaviokidd
 */
public class FileServiceAbstractImpl {
	private static final String BAD_FILE_ERR = "File must not be null and it must have a name.";
	
	protected String getFileName(MultipartFile multipartFile) {
		if (multipartFile == null || multipartFile.getOriginalFilename() == null) {
			throw new IllegalArgumentException(BAD_FILE_ERR);
		}
		
		return StringUtils.getFilename(StringUtils.cleanPath(multipartFile.getOriginalFilename()));
	}
	
}
