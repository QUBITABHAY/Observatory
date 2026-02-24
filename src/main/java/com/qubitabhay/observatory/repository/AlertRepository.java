package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, Long> {
}