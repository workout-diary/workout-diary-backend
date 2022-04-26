package com.deukgeun.workout.workout.domain;

import com.deukgeun.workout.user.domain.BaseTimeEntity;
import com.deukgeun.workout.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_sets")
public class WorkoutSet extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private SetType setType;

    @Column
    private String title;

    @Column
    private String muscles;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workout_set_id")
    @ToString.Exclude
    private Set<WorkoutSetDetail> workoutSetDetails;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private User user;
}
