package org.okidd.dtos;

import org.okidd.entities.FileVersion;

import java.util.Objects;

/**
 * @author octaviokidd
 */
public class FileInfo {
	
	private final String name;
	private final Long version;
	private final String contentType;
	
	public FileInfo(String name, Long version, String contentType) {
		this.name = name;
		this.version = version;
		this.contentType = contentType;
	}
	
	public FileInfo(FileVersion file) {
		this.name = file.getName();
		this.version = file.getVersion();
		this.contentType = file.getContentType();
	}
	
	public String getName() {
		return name;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FileInfo fileInfo = (FileInfo) o;
		return getName().equals(fileInfo.getName()) && getVersion().equals(fileInfo.getVersion());
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getName(), getVersion());
	}
}
