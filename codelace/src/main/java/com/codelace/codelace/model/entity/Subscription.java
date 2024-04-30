package com.codelace.codelace.model.entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @OneToOne
    @JoinColumn(name="students_id", nullable = false)
    private Student students;

    @ManyToOne
    @JoinColumn(name="planes_id", nullable = false)
    private Plan plans;
    
    @Column(name = "fecha_inicio", nullable = false)
    private Date beginDate;

    @Column(name = "fecha_fin", nullable = false)
    private Date endDate;

    @Column(name = "active", nullable = false)
    private boolean active;
}
