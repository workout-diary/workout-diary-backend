package com.deukgeun.workout.user.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user_properties")
public class UserProperty {

    @Id
    @Column(name = "id")
    private Long id;

    @Column
    private Integer age;

    @Column
    private Double height;

    @Column
    private Double weight;

    @Column(name = "body_fat")
    private Double bodyFat;
}
