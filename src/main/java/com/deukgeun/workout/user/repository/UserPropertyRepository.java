package com.deukgeun.workout.user.repository;

import com.deukgeun.workout.user.domain.UserProperty;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPropertyRepository extends JpaRepository<UserProperty, Long> {
}
