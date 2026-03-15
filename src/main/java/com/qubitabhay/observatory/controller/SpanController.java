package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.span.CreateSpanRequest;
import com.qubitabhay.observatory.dto.span.SpanResponse;
import com.qubitabhay.observatory.service.SpanIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spans")
public class SpanController {

    private final SpanIngestionService spanIngestionService;

    public SpanController(SpanIngestionService spanIngestionService) {
        this.spanIngestionService = spanIngestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SpanResponse ingest(@Valid @RequestBody CreateSpanRequest request) {
        return spanIngestionService.ingest(request);
    }

    @GetMapping
    public List<SpanResponse> getSpans(@RequestParam String traceId) {
        return spanIngestionService.getByTraceId(traceId);
    }
}
