package com.codelace.codelace.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "students_id")
    @MapsId
    private Student student;

    @ManyToOne
    @JoinColumn(name="plans_id", nullable = false)
    private Plan plan;
    
    @Column(name = "begin_date", nullable = false)
    private Date beginDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "active", nullable = false)
    private Boolean active;
}
