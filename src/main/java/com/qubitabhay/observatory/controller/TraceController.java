package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.trace.CreateTraceRequest;
import com.qubitabhay.observatory.dto.trace.TraceResponse;
import com.qubitabhay.observatory.service.TraceIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/traces")
public class TraceController {

    private final TraceIngestionService traceIngestionService;

    public TraceController(TraceIngestionService traceIngestionService) {
        this.traceIngestionService = traceIngestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TraceResponse ingest(@Valid @RequestBody CreateTraceRequest request) {
        return traceIngestionService.ingest(request);
    }

    @GetMapping
    public List<TraceResponse> getTraces(
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return traceIngestionService.query(serviceId, from, to, page, size);
    }
}
