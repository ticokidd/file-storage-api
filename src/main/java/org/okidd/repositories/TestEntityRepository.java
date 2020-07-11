package org.okidd.repositories;

import org.okidd.entities.TestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author octaviokidd
 */
@Repository
public interface TestEntityRepository extends CrudRepository<TestEntity, Integer>, QueryByExampleExecutor<TestEntity> {
}
