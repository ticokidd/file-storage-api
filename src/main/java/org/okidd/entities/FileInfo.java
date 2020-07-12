package org.okidd.entities;

import java.util.Objects;

/**
 * @author octaviokidd
 */
public class FileInfo {
	
	private final Long id;
	private final String name;
	private final Long version;
	
	public FileInfo(Long id, String name, Long version) {
		this.id = id;
		this.name = name;
		this.version = version;
	}
	
	public FileInfo(File file) {
		this.id = file.getId();
		this.name = file.getName();
		this.version = file.getVersion();
	}
	
	public Long getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Long getVersion() {
		return version;
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
