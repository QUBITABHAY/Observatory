package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.ServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceEntityRepository extends JpaRepository<ServiceEntity, Long> {
    List<ServiceEntity> findByHost_Id(Long hostId);
}
