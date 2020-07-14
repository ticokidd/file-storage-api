package org.okidd.controllers;

import org.okidd.entities.FileVersion;
import org.okidd.dtos.FileInfo;
import org.okidd.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author octaviokidd
 */
@Controller
@RequestMapping(path = "/files")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	/**
	 * Uploads a new file; the file's name must not match an existing file in the system, and a version 1 of the file
	 * will be created with the uploaded file's content.
	 *
	 * @param file the uploaded file
	 * @return 200 OK if the upload succeeded
	 * @throws IOException 400 BAD REQUEST if the filename already exist in the system or if there was an issue
	 * reading the file's content.
	 */
	@PostMapping()
	public ResponseEntity<FileInfo> uploadNewFile(@RequestParam("file") MultipartFile file) throws IOException {
		FileVersion savedFile = fileService.saveNewFile(file);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new FileInfo(savedFile));
	}
	
	/**
	 * Uploads a new version of a file; the file's name must match an existing file in the system, and a new version
	 * will be created with the newly uploaded file's content.
	 *
	 * @param file the uploaded file
	 * @return 200 OK if the upload succeeded
	 * @throws IOException 400 BAD REQUEST if the filename doesn't yet exist in the system or if there was an issue
	 * reading the file's content.
	 */
	@PutMapping()
	public ResponseEntity<FileInfo> uploadNewFileVersion(@RequestParam("file") MultipartFile file) throws IOException {
		FileVersion savedFile = fileService.saveNewFileVersion(file);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new FileInfo(savedFile));
	}
	
	/**
	 * Endpoint to retrieve a file.
	 *
	 * @param filename the name of the file to retrieve
	 * @param version (optional) the specific version you want to retrieve; if not provided, the latest version will be
	 * retrieved
	 * @return the requested file, or 404 Not Found if the given filename does not match at least 1 stored file, or if
	 * the requested version does not exist for the given filename.
	 */
	@GetMapping
	public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename,
			@RequestParam(value = "version", required = false) Long version) {
		
		FileVersion file = version == null ? fileService.findLatest(filename)
				: fileService.findVersion(filename, version);
		
		if (file != null) {
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(file.getContentType()))
					.header(HttpHeaders.CONTENT_DISPOSITION, buildContentDispositionHeaderValue(file.getName()))
					.body(file.getContent());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Endpoint to list all files.
	 *
	 * @param filename (optional) name of a file to list all versions of; if not provided, all version of all files will
	 * be returned.
	 * @return list of {@link FileInfo} objects for all file versions, ordered by name (asc) and version (desc), or for
	 * all versions of the given filename, ordered by version (desc)
	 */
	@GetMapping(path = "/list")
	public ResponseEntity<Iterable<FileInfo>> listFileVersions(
			@RequestParam(value = "filename", required = false) String filename) {
		
		Iterable<FileInfo> files = filename != null ? fileService.findAllVersions(filename) : fileService.findAll();
		
		if (files.iterator().hasNext()) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(files);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	/**
	 * Endpoint to delete files.
	 *
	 * @param filename name of the file to delete
	 * @param version (optional) version of the file to delete; if not provided, all versions of the given filename will
	 * be deleted
	 * @return 204 No Content
	 */
	@DeleteMapping
	public ResponseEntity<Object> deleteFileVersion(@RequestParam("filename") String filename,
			@RequestParam(value = "version", required = false) Long version) {
		
		if (version == null) {
			fileService.deleteAllVersions(filename);
		} else {
			fileService.deleteVersion(filename, version);
		}
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * Utility method.
	 *
	 * @param filename the file to build the content-disposition header with
	 * @return content-disposition header value with the given filename
	 */
	private String buildContentDispositionHeaderValue(String filename) {
		return "attachment; filename=\"" + filename + "\"";
	}
}
