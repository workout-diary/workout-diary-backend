package com.deukgeun.workout.workout.domain;

import com.deukgeun.workout.user.domain.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "workout_set_details")
public class WorkoutSetDetail extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double weight;

    @Column
    private Long repetition;

    @Column
    private Long time;

    @Column
    private Boolean done;

    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private WorkoutSet workoutSet;
}
