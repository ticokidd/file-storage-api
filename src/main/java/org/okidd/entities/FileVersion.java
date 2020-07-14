package org.okidd.entities;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.sql.Timestamp;
import java.util.Objects;

/**
 * @author octaviokidd
 */
@Entity
@Table(name = "file",
		indexes = {
			@Index(columnList = "name", name = "name_idx"),
			@Index(columnList = "name,version", name = "name_version_idx")
		},
		uniqueConstraints = @UniqueConstraint(columnNames = {"name", "version"})
)
public class FileVersion {
	
	public FileVersion() {}
	
	public FileVersion(String name) {
		this(name, null, null, null);
	}
	
	public FileVersion(String name, Long version) {
		this(name, version, null, null);
	}
	
	public FileVersion(String name, Long version, String contentType, byte[] content) {
		this.name = name;
		this.version = version;
		this.contentType = contentType;
		this.content = content;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;
	
	@Column(name = "name", columnDefinition = "VARCHAR(256)")
	private String name;
	
	@Column(name = "version")
	private Long version;
	
	@CreationTimestamp
	@Column(name = "created")
	private Timestamp created;
	
	@Column(name = "contentType")
	private String contentType;
	
	@Column(name = "content", columnDefinition = "MEDIUMBLOB")
	private byte[] content;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getVersion() {
		return version;
	}
	
	public void setVersion(Long version) {
		this.version = version;
	}
	
	public Timestamp getCreated() {
		return created;
	}
	
	public void setCreated(Timestamp created) {
		this.created = created;
	}
	
	public String getContentType() {
		return contentType;
	}
	
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public byte[] getContent() {
		return content;
	}
	
	public void setContent(byte[] content) {
		this.content = content;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FileVersion file = (FileVersion) o;
		return this.name.equals(file.name) && this.version.equals(file.version);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(this.name, this.version);
	}
}
