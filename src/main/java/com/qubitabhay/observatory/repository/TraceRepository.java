package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.Trace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TraceRepository extends JpaRepository<Trace, Long> {
    List<Trace> findByService_Id(Long serviceId);
    Optional<Trace> findByTraceId(String traceId);
}
