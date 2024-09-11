package com.boundary.boundarybackend.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.boundary.boundarybackend.domain.user.model.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User>
{
    Optional<User> findByUserId(String userId);

    Optional<User> findByRefreshToken(String refreshToken);
    boolean existsByUserId(String userId);
    boolean existsByChildId(String childId);
    Optional<User> findById(Long id);

}
