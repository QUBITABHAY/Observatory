package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.Span;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpanRepository extends JpaRepository<Span, Long> {
    List<Span> findByTrace_Id(Long traceId);
    List<Span> findByTrace_TraceId(String traceId);
    List<Span> findByService_Id(Long serviceId);
}
