package org.okidd.repositories;

import org.okidd.entities.FileVersion;
import org.okidd.dtos.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;

/**
 * @author octaviokidd
 */
@Repository
public interface FileRepository extends CrudRepository<FileVersion, Long>, QueryByExampleExecutor<FileVersion> {
	boolean existsByName(String name);
	FileVersion findTopByNameOrderByVersionDesc(String name);
	FileVersion findTopByNameAndVersion(String name, Long version);
	<T> Collection<T> findAllByNameOrderByVersionDesc(String name, Class<T> type);
	
	/**
	 * <p>Returns a FileInfo projection of all File records.</p>
	 *
	 * <p>The naming is wonky because findAll() is already taken, and this was the only way I managed to make the
	 * projection work without compile-time conflicts or runtime errors.</p>
	 * <p> My original intention was to have it find all projected records and order them by name asc and version desc,
	 * however it seems the algorithm that auto-generates the boilerplate code is unable to correctly parse method
	 * names like <code>findAllOrderByNameAscAndVersionDesc</code> or
	 * <code>findAllOrderByName</code>; somehow it seems it required a criterion column in order to properly parse
	 * the sorting keywords.</p>
	 *
	 * @return an unsorted list of FileInfo objects.
	 */
	Iterable<FileInfo> findBy();
	
	@Transactional
	void deleteByNameAndVersion(String name, Long version);
	@Transactional
	void deleteAllByName(String name);
}
