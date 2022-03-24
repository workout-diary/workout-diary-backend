package com.deukgeun.workout.user.repository;

import com.deukgeun.workout.user.domain.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
}
