package com.qubitabhay.observatory.controller;

import com.qubitabhay.observatory.dto.service.CreateServiceRequest;
import com.qubitabhay.observatory.dto.service.ServiceResponse;
import com.qubitabhay.observatory.service.ServiceManagementService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    private final ServiceManagementService serviceManagementService;

    public ServiceController(ServiceManagementService serviceManagementService) {
        this.serviceManagementService = serviceManagementService;
    }

    @PostMapping
    public ServiceResponse create(@Valid @RequestBody CreateServiceRequest request) {
        return serviceManagementService.create(request);
    }

    @GetMapping
    public List<ServiceResponse> list(@RequestParam(required = false) Long hostId) {
        return serviceManagementService.list(hostId);
    }

    @GetMapping("/{id}")
    public ServiceResponse get(@PathVariable Long id) {
        return serviceManagementService.get(id);
    }
}
