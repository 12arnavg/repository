package com.getwellsoon.repository;

import java.util.List;
import java.util.Set;

import com.getwellsoon.entity.Condition;
import com.getwellsoon.entity.Trial;
import com.getwellsoon.model.ConditionCountTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConditionRepository extends JpaRepository<Condition, Long> {
	Set<Trial> findByName(String name);

	@Query("FROM Condition c WHERE c.name IN (:conditions)")
	List<Condition> findByNameIn(@Param("conditions") List<String> conditions);

	@Query(value = "SELECT c.name FROM Condition c WHERE c.name LIKE %:name%")
	List<String> getNamesLike(@Param("name") String name);

	@Query(value = "SELECT c FROM Condition c JOIN FETCH c.trials WHERE c.name = :name")
	Condition findOneByName(@Param("name") String name);

	// @Query(value = "SELECT c FROM Condition c JOIN FETCH c.trials", countQuery = "SELECT COUNT(t.id) FROM Conditions c JOIN FETCH c.trials t WHERE t.enrollmentStatus IN :status")
	@Query(value = "SELECT c FROM Condition c JOIN FETCH c.trials", countQuery = "SELECT count(c.id) FROM Condition c")
	Page<Condition> findAllConditions(Pageable batch, @Param("statusList") String statusList []);

	@Query(value = "SELECT cc.condition_id as conditionId, con.name as conditionName, con.slug as slug, "
			+ "cc.trial_count as trialCount FROM condition_count cc "
			+ "INNER JOIN conditions con on con.id = cc.condition_id "
			+ "ORDER BY cc.trial_count DESC LIMIT 500", nativeQuery = true)
	List<ConditionCountTO> getConditionWithActiveTrials();

	@Query(value = "SELECT c FROM Condition c WHERE slug = :slug")
	List<Condition> findBySlug(@Param("slug") String slug);

	@Query(value = "SELECT c.slug FROM Condition c INNER JOIN c.trials t WHERE t.enrollmentStatus IN ('Not yet recruiting', 'Recruiting', 'Enrolling by invitation', 'Active, not recruiting')")
    Page<String> findSlug(Pageable pageable);
}
