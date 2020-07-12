package org.okidd.controllers;

import org.okidd.entities.File;
import org.okidd.entities.FileInfo;
import org.okidd.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author octaviokidd
 */
@Controller
@RequestMapping(path = "/file")
public class FileController {
	
	@Autowired
	private FileService fileService;
	
	@PostMapping(path = "/add_new")
	public ResponseEntity<FileInfo> uploadNewFile(@RequestParam("file") MultipartFile file)
			throws IOException {
		// TODO: reject large files from being uploaded to prevent the database from ballooning
		
		// TODO: error on null file
		
		// TODO: properly handle exceptions
		File savedFile = fileService.saveNewFile(file);
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new FileInfo(savedFile));
	}
	
	@PostMapping(path = "/add_new_version")
	public ResponseEntity<FileInfo> uploadNewFileVersion(@RequestParam("file") MultipartFile file)
			throws IOException {
		// TODO: reject large files from being uploaded to prevent the database from ballooning
		
		// TODO: error on null file
		
		// TODO: properly handle exceptions
		File savedFileVersion = fileService.saveNewFileVersion(file);
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new FileInfo(savedFileVersion));
	}
	
	/**
	 *
	 * @param filename
	 * @param version the specific version you want to retrieve; if null, the latest version will be retrieved
	 * @return
	 */
	@GetMapping
	public ResponseEntity<byte[]> downloadFile(@RequestParam("filename") String filename,
			@RequestParam("version") Long version) {
		
		// TODO: error on null filename
		
		File file = (version == null) ? fileService.findLatest(filename) : fileService.findVersion(filename, version);
		
		if (file != null) {
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(file.getContentType()))
					.header(HttpHeaders.CONTENT_DISPOSITION, buildContentDispositionHeaderValue(file.getName()))
					.body(file.getContent());
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(path = "/list")
	public ResponseEntity<Iterable<FileInfo>> listFileVersions(@RequestParam("filename") String filename) {
		Iterable<FileInfo> files = filename != null ? fileService.findAllVersions(filename) : fileService.findAll();
		
		if (files.iterator().hasNext()) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(files);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping
	public void deleteFileVersion(@RequestParam("filename") String filename, @RequestParam("version") Long version) {
		if (filename == null) {
			return;
		}
		
		if (version == null) {
			fileService.deleteAllVersions(filename);
		} else {
			fileService.deleteVersion(filename, version);
		}
	}
	
	private String buildContentDispositionHeaderValue(String filename) {
		return "attachment; filename=\"" + filename + "\"";
	}
}
