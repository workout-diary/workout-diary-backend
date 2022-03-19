package com.deukgeun.workout.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.deukgeun.workout.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
