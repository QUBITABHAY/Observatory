package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.trace.CreateTraceRequest;
import com.qubitabhay.observatory.dto.trace.TraceResponse;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.model.Trace;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import com.qubitabhay.observatory.repository.TraceRepository;
import org.springframework.stereotype.Service;

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

    public List<TraceResponse> query(Long serviceId) {
        List<Trace> results = (serviceId != null)
                ? traceRepository.findByService_Id(serviceId)
                : traceRepository.findAll();

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
