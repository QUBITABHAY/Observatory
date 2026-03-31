package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.log.CreateLogRequest;
import com.qubitabhay.observatory.dto.log.LogResponse;
import com.qubitabhay.observatory.service.LogIngestionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogController {

    private final LogIngestionService logIngestionService;

    public LogController(LogIngestionService logIngestionService) {
        this.logIngestionService = logIngestionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LogResponse ingest(@Valid @RequestBody CreateLogRequest request) {
        return logIngestionService.ingest(request);
    }

    @GetMapping
    public List<LogResponse> getLogs(
            @RequestParam(required = false) Long hostId,
            @RequestParam(required = false) Long serviceId,
            @RequestParam(required = false) String level,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return logIngestionService.query(hostId, serviceId, level, from, to, page, size);
    }
}
