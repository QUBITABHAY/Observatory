package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long>, JpaSpecificationExecutor<Alert> {
	List<Alert> findByService_Id(Long serviceId);
}