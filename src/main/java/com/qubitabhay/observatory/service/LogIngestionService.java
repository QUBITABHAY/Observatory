package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.log.CreateLogRequest;
import com.qubitabhay.observatory.dto.log.LogResponse;
import com.qubitabhay.observatory.model.Host;
import com.qubitabhay.observatory.model.LogEntry;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.repository.HostRepository;
import com.qubitabhay.observatory.repository.LogEntryRepository;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import org.springframework.stereotype.Service;

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

    public List<LogResponse> query(Long hostId, Long serviceId, String level) {
        List<LogEntry> results;

        if (hostId != null && serviceId != null && level != null) {
            results = logEntryRepository.findByHost_Id(hostId).stream()
                    .filter(l -> l.getService().getId().equals(serviceId) && l.getLevel().equalsIgnoreCase(level))
                    .toList();
        } else if (hostId != null && serviceId != null) {
            results = logEntryRepository.findByHost_IdAndService_Id(hostId, serviceId);
        } else if (hostId != null && level != null) {
            results = logEntryRepository.findByHost_IdAndLevel(hostId, level);
        } else if (serviceId != null && level != null) {
            results = logEntryRepository.findByService_IdAndLevel(serviceId, level);
        } else if (hostId != null) {
            results = logEntryRepository.findByHost_Id(hostId);
        } else if (serviceId != null) {
            results = logEntryRepository.findByService_Id(serviceId);
        } else if (level != null) {
            results = logEntryRepository.findByLevel(level);
        } else {
            results = logEntryRepository.findAll();
        }

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
