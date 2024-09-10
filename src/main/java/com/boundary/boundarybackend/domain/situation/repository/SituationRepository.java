package com.boundary.boundarybackend.domain.situation.repository;

import com.boundary.boundarybackend.domain.situation.model.entity.Situation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SituationRepository extends JpaRepository<Situation, Long> {
    List<Situation> findAllByParentId(String parentId);

    List<Situation> findAllByChildId(String childId);
}
