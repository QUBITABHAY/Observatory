package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.host.CreateHostRequest;
import com.qubitabhay.observatory.dto.host.HostResponse;
import com.qubitabhay.observatory.model.Host;
import com.qubitabhay.observatory.repository.HostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HostManagementService {

    private final HostRepository hostRepository;

    public HostManagementService(HostRepository hostRepository) {
        this.hostRepository = hostRepository;
    }

    public HostResponse create(CreateHostRequest request) {
        hostRepository.findByHostname(request.hostname()).ifPresent(host -> {
            throw new IllegalArgumentException("Host already exists with hostname: " + request.hostname());
        });

        Host host = new Host();
        host.setHostname(request.hostname());
        host.setIpAddress(request.ipAddress());
        host.setEnvironment(request.environment());

        Host savedHost = hostRepository.save(host);
        return toResponse(savedHost);
    }

    public List<HostResponse> list() {
        return hostRepository.findAll().stream().map(this::toResponse).toList();
    }

    public HostResponse get(Long id) {
        Host host = hostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Host not found: " + id));

        return toResponse(host);
    }

    private HostResponse toResponse(Host host) {
        return new HostResponse(
                host.getId(),
                host.getHostname(),
                host.getIpAddress(),
                host.getEnvironment(),
                host.getCreatedAt()
        );
    }
}
