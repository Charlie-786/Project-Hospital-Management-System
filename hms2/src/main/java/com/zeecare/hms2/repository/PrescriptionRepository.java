package com.zeecare.hms2.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zeecare.hms2.entity.Prescription;

public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

	List<Prescription> findByPatientId(Long id);
//	List<Prescription> findByUserId(Long id);
    // Add any custom query methods if necessary
}