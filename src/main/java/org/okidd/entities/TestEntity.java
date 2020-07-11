package org.okidd.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author octaviokidd
 */
@Entity
public class TestEntity {
	
	public static final TestEntity NOT_FOUND = new TestEntity("Entity Not Found");

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false, updatable = false)
	private Integer id;
	
	private String testField;
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public TestEntity() {}
	
	public TestEntity(String testField) {
		this.testField = testField;
	}
	
	public String getTestField() {
		return testField;
	}
	
	public void setTestField(String testField) {
		this.testField = testField;
	}
}
