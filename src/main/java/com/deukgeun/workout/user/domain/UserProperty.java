package com.deukgeun.workout.user.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_properties")
public class UserProperty extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @ToString.Exclude
    private User user;

    @Column
    private Integer age;

    @Column
    private Double height;

    @Column
    private Double weight;

    @Column(name = "body_fat")
    private Double bodyFat;
}
