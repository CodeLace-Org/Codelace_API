package com.codelace.codelace.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.model.entity.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long>{
	Optional<Subscription> findById(Optional<Long>student);
    Optional<Subscription> findByPlan(Plan plan);
}
