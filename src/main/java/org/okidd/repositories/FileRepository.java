package org.okidd.repositories;

import org.okidd.entities.File;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

/**
 * @author octaviokidd
 */
@Repository
public interface FileRepository extends CrudRepository<File, Long>, QueryByExampleExecutor<File> {
}
