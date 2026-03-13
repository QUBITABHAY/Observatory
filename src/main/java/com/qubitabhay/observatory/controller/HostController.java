package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.host.CreateHostRequest;
import com.qubitabhay.observatory.dto.host.HostResponse;
import com.qubitabhay.observatory.service.HostManagementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/hosts")
public class HostController {

    private final HostManagementService hostManagementService;

    public HostController(HostManagementService hostManagementService) {
        this.hostManagementService = hostManagementService;
    }

    @PostMapping
    public HostResponse create(@Valid @RequestBody CreateHostRequest request) {
        return hostManagementService.create(request);
    }

    @GetMapping
    public List<HostResponse> list() {
        return hostManagementService.list();
    }

    @GetMapping("/{id}")
    public HostResponse get(@PathVariable Long id) {
        return hostManagementService.get(id);
    }
}
