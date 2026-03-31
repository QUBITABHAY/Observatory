package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.trace.CreateTraceRequest;
import com.qubitabhay.observatory.dto.trace.TraceResponse;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.model.Trace;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import com.qubitabhay.observatory.repository.TraceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TraceIngestionService {

    private final TraceRepository traceRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public TraceIngestionService(TraceRepository traceRepository,
                                 ServiceEntityRepository serviceEntityRepository) {
        this.traceRepository = traceRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    public TraceResponse ingest(CreateTraceRequest request) {
        ServiceEntity service = serviceEntityRepository.findById(request.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + request.serviceId()));

        Trace trace = new Trace();
        trace.setTraceId(request.traceId());
        trace.setDuration(request.duration());
        trace.setService(service);

        return toResponse(traceRepository.save(trace));
    }

    public List<TraceResponse> query(Long serviceId,
                                     LocalDateTime from,
                                     LocalDateTime to,
                                     int page,
                                     int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = Math.min(Math.max(1, size), 500);

        Specification<Trace> spec = (root, query, cb) -> cb.conjunction();

        if (serviceId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("service").get("id"), serviceId));
        }

        if (from != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("startedAt"), from));
        }

        if (to != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("startedAt"), to));
        }

        List<Trace> results = traceRepository.findAll(
                spec,
                PageRequest.of(normalizedPage, normalizedSize, Sort.by(Sort.Direction.DESC, "startedAt"))
        ).getContent();

        return results.stream().map(this::toResponse).toList();
    }

    public List<TraceResponse> getAll() {
        return traceRepository.findAll().stream().map(this::toResponse).toList();
    }

    private TraceResponse toResponse(Trace trace) {
        return new TraceResponse(
                trace.getId(),
                trace.getTraceId(),
                trace.getDuration(),
                trace.getService().getId(),
                trace.getStartedAt()
        );
    }
}
