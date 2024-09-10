package com.boundary.boundarybackend.domain.sst.repository;

import com.boundary.boundarybackend.domain.sst.model.entity.Sst;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SstRepository extends JpaRepository<Sst, Long> {

    Optional<Sst> findByThreadId(String threadId);

}