package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.log.CreateLogRequest;
import com.qubitabhay.observatory.dto.log.LogResponse;
import com.qubitabhay.observatory.model.Host;
import com.qubitabhay.observatory.model.LogEntry;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.repository.HostRepository;
import com.qubitabhay.observatory.repository.LogEntryRepository;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LogIngestionService {

    private final LogEntryRepository logEntryRepository;
    private final HostRepository hostRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public LogIngestionService(LogEntryRepository logEntryRepository,
                               HostRepository hostRepository,
                               ServiceEntityRepository serviceEntityRepository) {
        this.logEntryRepository = logEntryRepository;
        this.hostRepository = hostRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    public LogResponse ingest(CreateLogRequest request) {
        Host host = hostRepository.findById(request.hostId())
                .orElseThrow(() -> new IllegalArgumentException("Host not found: " + request.hostId()));
        ServiceEntity service = serviceEntityRepository.findById(request.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + request.serviceId()));

        LogEntry entry = new LogEntry();
        entry.setLevel(request.level());
        entry.setMessage(request.message());
        entry.setTraceId(request.traceId());
        entry.setHost(host);
        entry.setService(service);

        return toResponse(logEntryRepository.save(entry));
    }

    public List<LogResponse> query(Long hostId,
                                   Long serviceId,
                                   String level,
                                   LocalDateTime from,
                                   LocalDateTime to,
                                   int page,
                                   int size) {
        int normalizedPage = Math.max(0, page);
        int normalizedSize = Math.min(Math.max(1, size), 500);

        Specification<LogEntry> spec = (root, query, cb) -> cb.conjunction();

        if (hostId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("host").get("id"), hostId));
        }

        if (serviceId != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("service").get("id"), serviceId));
        }

        if (level != null && !level.isBlank()) {
            spec = spec.and((root, query, cb) -> cb.equal(cb.lower(root.get("level")), level.toLowerCase()));
        }

        if (from != null) {
            spec = spec.and((root, query, cb) -> cb.greaterThanOrEqualTo(root.get("timestamp"), from));
        }

        if (to != null) {
            spec = spec.and((root, query, cb) -> cb.lessThanOrEqualTo(root.get("timestamp"), to));
        }

        List<LogEntry> results = logEntryRepository.findAll(
                spec,
                PageRequest.of(normalizedPage, normalizedSize, Sort.by(Sort.Direction.DESC, "timestamp"))
        ).getContent();

        return results.stream().map(this::toResponse).toList();
    }

    private LogResponse toResponse(LogEntry entry) {
        return new LogResponse(
                entry.getId(),
                entry.getLevel(),
                entry.getMessage(),
                entry.getTraceId(),
                entry.getHost().getId(),
                entry.getService().getId(),
                entry.getTimestamp()
        );
    }
}
