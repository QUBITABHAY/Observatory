package com.qubitabhay.observatory.repository;

import com.qubitabhay.observatory.model.LogEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogEntryRepository extends JpaRepository<LogEntry, Long> {
    List<LogEntry> findByHost_Id(Long hostId);
    List<LogEntry> findByService_Id(Long serviceId);
    List<LogEntry> findByLevel(String level);
    List<LogEntry> findByHost_IdAndService_Id(Long hostId, Long serviceId);
    List<LogEntry> findByHost_IdAndLevel(Long hostId, String level);
    List<LogEntry> findByService_IdAndLevel(Long serviceId, String level);
}
