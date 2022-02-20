package com.getwellsoon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.getwellsoon.entity.Source;

@Repository
public interface SourceRepository extends JpaRepository<Source, Long> {

}
