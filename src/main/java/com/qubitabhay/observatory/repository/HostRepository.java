package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findByHostname(String hostname);
}
