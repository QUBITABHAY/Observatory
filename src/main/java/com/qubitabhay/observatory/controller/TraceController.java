package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.trace.CreateTraceRequest;
import com.qubitabhay.observatory.dto.trace.TraceResponse;
import com.qubitabhay.observatory.service.TraceIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<TraceResponse> getTraces(@RequestParam(required = false) Long serviceId) {
        return traceIngestionService.query(serviceId);
    }
}
