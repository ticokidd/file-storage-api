package org.okidd.controllers;

import org.okidd.entities.File;
import org.okidd.entities.FileInfo;
import org.okidd.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping(path = "/file")
public class FileController {
	
	// inMemoryFileService still needs an active database connection, else the application will fail during startup
	@Autowired
//	@Qualifier("inMemoryFileService")
	@Qualifier("databaseFileService")
	private FileService fileService;
	
	// Note: the endpoints /add_new and /add_new_version could be coalesced into a single POST endpoint that adds new
	// files, and increases the version number when a matching filename is found; they are separated simply to
	// showcase both POST and PUT usage.
	
	@PostMapping(path = "/add_new")
	public ResponseEntity<FileInfo> uploadNewFile(@RequestParam("file") MultipartFile file)
			throws IOException {
		// TODO: reject large files from being uploaded to prevent the database from ballooning
		
		// TODO: error on null file
		
		// TODO: properly handle exceptions
		File savedFile = fileService.saveNewFile(file);
		
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new FileInfo(savedFile));
	}
	
	@PutMapping(path = "/add_new_version")
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
			@RequestParam(value = "version", required = false) Long version) {
		
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
	public ResponseEntity<Iterable<FileInfo>> listFileVersions(@RequestParam(value = "filename", required = false) String filename) {
		Iterable<FileInfo> files = filename != null ? fileService.findAllVersions(filename) : fileService.findAll();
		
		if (files.iterator().hasNext()) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(files);
		} else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping
	public ResponseEntity<Object> deleteFileVersion(@RequestParam("filename") String filename, @RequestParam(value = "version", required = false) Long version) {
		if (version == null) {
			fileService.deleteAllVersions(filename);
		} else {
			fileService.deleteVersion(filename, version);
		}
		return ResponseEntity.noContent().build();
	}
	
	private String buildContentDispositionHeaderValue(String filename) {
		return "attachment; filename=\"" + filename + "\"";
	}
}
