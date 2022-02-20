package com.getwellsoon.repository;

import java.util.List;

import com.getwellsoon.entity.SiteURL;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SiteUrlRepository extends JpaRepository<SiteURL, Long> {
	@Query(value = "SELECT s.url FROM SiteURL s WHERE s.trial.id = (:trialId)")
	String findOneByTrialId(@Param("trialId") Long trialId);

	Page<SiteURL> findAllByTrialId(@Param("trialId") Long trialId, Pageable pageable);
	
	@Query(value = "FROM SiteURL su WHERE su.url IN (:urls)")
	List<SiteURL> findAllByUrls(@Param("urls") String [] urls);

	@Query(value = "SELECT su.url FROM SiteURL su WHERE su.url IN (:urls) AND su.url IS NOT NULL")
	List<String> getUrlsByUrls(@Param("urls") String [] urls);

	// @Query(value = "SELECT su.refNctId FROM SiteURL su WHERE refNctId = :nctId")
    List<SiteURL> findByRefNctId(@Param("nctId") String nctId);

	// @Query(value = "SELECT su FROM SiteURL su WHERE url LIKE ':urlNctId%'")
	List<SiteURL> findByUrlEndingWith(@Param("urlNctId") String urlNctId);

//	@Query(value = "SELECT su FROM SiteURL su WHERE url = :url")
	SiteURL findOneByUrl(@Param("url") String url);
}
