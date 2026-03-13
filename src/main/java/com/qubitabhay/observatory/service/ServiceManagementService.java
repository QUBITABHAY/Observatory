package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.dto.service.CreateServiceRequest;
import com.qubitabhay.observatory.dto.service.ServiceResponse;
import com.qubitabhay.observatory.model.Host;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.repository.HostRepository;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceManagementService {

    private final ServiceEntityRepository serviceEntityRepository;
    private final HostRepository hostRepository;

    public ServiceManagementService(ServiceEntityRepository serviceEntityRepository, HostRepository hostRepository) {
        this.serviceEntityRepository = serviceEntityRepository;
        this.hostRepository = hostRepository;
    }

    public ServiceResponse create(CreateServiceRequest request) {
        Host host = hostRepository.findById(request.hostId())
                .orElseThrow(() -> new IllegalArgumentException("Host not found: " + request.hostId()));

        ServiceEntity serviceEntity = new ServiceEntity();
        serviceEntity.setName(request.name());
        serviceEntity.setHost(host);

        ServiceEntity savedServiceEntity = serviceEntityRepository.save(serviceEntity);
        return toResponse(savedServiceEntity);
    }

    public List<ServiceResponse> list(Long hostId) {
        List<ServiceEntity> serviceEntities = hostId == null
                ? serviceEntityRepository.findAll()
                : serviceEntityRepository.findByHost_Id(hostId);

        return serviceEntities.stream().map(this::toResponse).toList();
    }

    public ServiceResponse get(Long id) {
        ServiceEntity serviceEntity = serviceEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found: " + id));

        return toResponse(serviceEntity);
    }

    private ServiceResponse toResponse(ServiceEntity serviceEntity) {
        return new ServiceResponse(
                serviceEntity.getId(),
                serviceEntity.getName(),
                serviceEntity.getHost().getId(),
                serviceEntity.getCreatedAt()
        );
    }
}
