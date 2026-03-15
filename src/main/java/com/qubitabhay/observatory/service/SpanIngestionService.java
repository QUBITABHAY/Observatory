package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.span.CreateSpanRequest;
import com.qubitabhay.observatory.dto.span.SpanResponse;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.model.Span;
import com.qubitabhay.observatory.model.Trace;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import com.qubitabhay.observatory.repository.SpanRepository;
import com.qubitabhay.observatory.repository.TraceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpanIngestionService {

    private final SpanRepository spanRepository;
    private final TraceRepository traceRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    public SpanIngestionService(SpanRepository spanRepository,
                                TraceRepository traceRepository,
                                ServiceEntityRepository serviceEntityRepository) {
        this.spanRepository = spanRepository;
        this.traceRepository = traceRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    public SpanResponse ingest(CreateSpanRequest request) {
        Trace trace = traceRepository.findByTraceId(request.traceId())
                .orElseThrow(() -> new IllegalArgumentException("Trace not found: " + request.traceId()));
        ServiceEntity service = serviceEntityRepository.findById(request.serviceId())
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + request.serviceId()));

        Span span = new Span();
        span.setSpanId(request.spanId());
        span.setParentSpanId(request.parentSpanId());
        span.setDuration(request.duration());
        span.setTrace(trace);
        span.setService(service);

        return toResponse(spanRepository.save(span));
    }

    public List<SpanResponse> getByTraceId(String traceId) {
        return spanRepository.findByTrace_TraceId(traceId).stream().map(this::toResponse).toList();
    }

    private SpanResponse toResponse(Span span) {
        return new SpanResponse(
                span.getId(),
                span.getSpanId(),
                span.getParentSpanId(),
                span.getDuration(),
                span.getTrace().getTraceId(),
                span.getService().getId(),
                span.getTimestamp()
        );
    }
}
