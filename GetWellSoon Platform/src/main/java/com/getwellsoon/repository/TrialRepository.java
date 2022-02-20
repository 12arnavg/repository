package com.getwellsoon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import com.getwellsoon.entity.Trial;
import com.getwellsoon.model.TrialMessage;
import com.getwellsoon.model.TrialUpdateTO;
// import com.getwellsoon.repository.queries.TrialQueries;

@Repository
public interface TrialRepository extends JpaRepository<Trial, Long> {
	
	@Query(value = "SELECT t.id, t.source FROM Trial t LEFT JOIN t.source ts WHERE ts.id = :sourceId")
	Page<Object[]> findAllUrlsForUpdateBySourceId(@Param("sourceId") Long sourceId, Pageable pageable);

	@Query(value = "SELECT t.id, t.source FROM Trial t LEFT JOIN t.source ts WHERE ts.id = :sourceId AND t.enrollmentStatus != 'Completed' OR t.enrollmentStatus IS NOT NULL ORDER BY t.processedAt ASC")
	Page<Object[]> findAllUrlsForUpdateBySourceIdAndEnrollmentStatusActive(@Param("sourceId") Long sourceId, Pageable pageable);

	@Query(value = "SELECT t.id, t.source FROM Trial t LEFT JOIN t.source ts WHERE ts.id = :sourceId AND t.enrollmentStatus = 'Completed' OR t.enrollmentStatus IS NULL ORDER BY t.processedAt ASC")
	Page<Object[]> findAllUrlsForUpdateBySourceIdAndEnrollmentStatusInactive(@Param("sourceId") Long sourceId, Pageable pageable);


	@Query(value = "SELECT t.id, t.source FROM Trial t ORDER BY t.processedAt ASC")
	Page<Object[]> findAllUrlsForUpdate(Pageable pageable);

	@Query(value = "SELECT t.id, t.source FROM Trial t WHERE t.enrollmentStatus != 'Completed' OR t.enrollmentStatus IS NOT NULL ORDER BY t.processedAt ASC")
	Page<Object[]> findAllUrlsForUpdateByEnrollmentStatusActive(Pageable pageable);

	@Query(value = "SELECT t.id, t.source FROM Trial t WHERE t.enrollmentStatus = 'Completed' OR t.enrollmentStatus IS NULL ORDER BY t.processedAt ASC")
	Page<Object[]> findAllUrlsForUpdateByEnrollmentStatusInactive(Pageable pageable);

	final static String FIND_BY_ID_QUERY = "SELECT t FROM Trial t LEFT JOIN FETCH t.studySponsors LEFT JOIN FETCH t.primaryOutcomes LEFT JOIN FETCH t.secondaryOutcomes LEFT JOIN FETCH t.collaborators LEFT JOIN FETCH t.investigators LEFT JOIN FETCH t.interventions LEFT JOIN FETCH t.contacts LEFT JOIN FETCH t.responsibleParties WHERE t.id = :id";
	@Query(value = FIND_BY_ID_QUERY)
	List<Trial> findByIdWithCollections(@Param("id") Long id);

	@Query(value = "SELECT t.id, t.nctId, t.processedAt FROM Trial t")
	Page<Object[]> findIdAndNctIdAndProcessedAt(Pageable pageable);
	
	Page<TrialMessage> findByMinAgeAndMaxAge(Pageable pageable, @Param("min") Integer min, @Param("max") Integer max);

	List<Trial> findOneByNctId(@Param("nctId") String nctId);
	
//	@Query(value = "SELECT DATE_FORMAT(t.processedAt, '%M %d,%Y') as processedDate, count(t.id) as count FROM Trial t WHERE PERIOD_DIFF(DATE_FORMAT(CURRENT_TIME(), '%Y%m'), DATE_FORMAT(DATE(processed_at), '%Y%m')) < 3 GROUP BY DATE(processedAt) ORDER BY DATE(processed_at)")
	@Query(value = "SELECT DATE_FORMAT(t.processedAt, '%M %d,%Y') as processedDate, count(t.id) as count"
			+ " FROM Trial t WHERE DATEDIFF(CURRENT_TIME(),DATE(processed_at)) <= 30"
			+ "	GROUP BY DATE(processedAt)"
			+ " ORDER BY DATE(processed_at)")
	List<TrialUpdateTO> getUpdateCount();
	
	@Query(value = "SELECT t.id, t.nctId, t.processedAt, t.enrollmentStatus FROM Trial t")
	Page<Object[]> findIdAndNctIdAndProcessedAtAndEnrollmentStatus(Pageable pageable);

}
