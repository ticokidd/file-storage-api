package org.okidd.controllers;

import org.okidd.entities.TestEntity;
import org.okidd.repositories.TestEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

/**
 * @author octaviokidd
 */
@Controller
@RequestMapping(path = "/testentity")
public class TestEntityController {
	@Autowired
	private TestEntityRepository testEntityRepository;
	
	@PostMapping
	public @ResponseBody String addEntity(@RequestParam String testField) {
		if (TestEntity.NOT_FOUND.getTestField().equals(testField)) {
			throw new IllegalArgumentException("Entities cannot have " + TestEntity.NOT_FOUND.getTestField() + " as value for testField.");
		}
		TestEntity entity = new TestEntity();
		entity.setTestField(testField);
		testEntityRepository.save(entity);
		return "New TestEntity added.";
	}
	
	@GetMapping(path = "/full_list")
	public @ResponseBody Iterable<TestEntity> getAllEntities() {
		return testEntityRepository.findAll();
	}
	
	@GetMapping
	public @ResponseBody Iterable<TestEntity> getEntities(@RequestParam String testField) {
//		Optional<TestEntity> queryResult = testEntityRepository.findById(id);
		
		Example<TestEntity> repoExample = Example.of(new TestEntity(testField));
		return testEntityRepository.findAll(repoExample);
		
//		return queryResult.orElse(TestEntity.NOT_FOUND);
	}
}
