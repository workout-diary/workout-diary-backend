package com.deukgeun.workout.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "user_authorities")
@IdClass(UserAuthority.class)
public class UserAuthority implements GrantedAuthority {

    @Id
    @Column(name = "id")
    private Long id;

    @Id
    private String authority;
}

