package com.qubitabhay.observatory.service;

import oshi.SystemInfo;
import oshi.software.os.OperatingSystem;
import oshi.software.os.OSProcess;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProcessMonitoringService {

    private final SystemInfo systemInfo = new SystemInfo();

    public List<Map<String, Object>> getTopProcesses() {

        OperatingSystem os = systemInfo.getOperatingSystem();

        List<OSProcess> processes = os.getProcesses(
                p -> true, // no filter
                Comparator.comparingDouble(OSProcess::getProcessCpuLoadCumulative).reversed(),
                10 // limit
        );

        return processes.stream().map(process -> {
            Map<String, Object> map = new HashMap<>();
            map.put("pid", process.getProcessID());
            map.put("name", process.getName());
            map.put("cpuUsage", process.getProcessCpuLoadCumulative() * 100);
            map.put("memory", process.getResidentSetSize());
            return map;
        }).collect(Collectors.toList());
    }
}
