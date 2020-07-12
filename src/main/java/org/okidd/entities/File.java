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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author octaviokidd
 */
@Entity
@Table(name = "file",
		indexes = {
			@Index(columnList = "name", name = "name_idx"),
			@Index(columnList = "name,version", name = "name_version_idx")
		}
)
public class File {
	
	public File() {}
	
	public File(String name, Long version, byte[] content) {
		this.name = name;
		this.version = version;
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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created")
	private Timestamp created;
	
	@Column(name = "content", columnDefinition = "LONGBLOB")
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
		File file = (File) o;
		return getName().equals(file.getName()) && Arrays.equals(getContent(), file.getContent());
	}
	
	@Override
	public int hashCode() {
		int result = Objects.hash(getName());
		result = 31 * result + Arrays.hashCode(getContent());
		return result;
	}
}
