package org.okidd.repositories;

import org.okidd.entities.File;
import org.okidd.entities.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author octaviokidd
 */
@Repository
public interface FileRepository extends CrudRepository<File, Long>, QueryByExampleExecutor<File> {
	boolean existsByName(String name);
	File findTopByNameOrderByVersionDesc(String name);
	File findTopByNameAndVersion(String name, Long version);
	<T> Collection<T> findAllByNameOrderByVersionDesc(String name, Class<T> type);
	<T> Collection<T> findAllOrderByNameAscAndVersionDesc(Class<T> type);
	void deleteByNameAndVersion(String name, Long version);
	void deleteAllByName(String name);
}
