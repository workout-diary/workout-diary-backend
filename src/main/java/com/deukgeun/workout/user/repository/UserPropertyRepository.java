package com.deukgeun.workout.user.repository;

import com.deukgeun.workout.user.domain.UserProperty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPropertyRepository extends JpaRepository<UserProperty, Long> {

    Optional<UserProperty> findByUserId(Long id);
}
