package com.qubitabhay.observatory.service;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import org.springframework.stereotype.Service;

@Service
public class SystemMetricsService {
    private final SystemInfo systemInfo = new SystemInfo();

    public double getCpuUsage() {
        CentralProcessor processor = systemInfo.getHardware().getProcessor();
        long[] prevTick = processor.getSystemCpuLoadTicks();
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        return processor.getSystemCpuLoadBetweenTicks(prevTick) * 100;
    }

    public long getUsedMemory() {
        GlobalMemory memory = systemInfo.getHardware().getMemory();
        return memory.getTotal() - memory.getAvailable();
    }

    public long getTotalMemory() {
        return systemInfo.getHardware().getMemory().getTotal();
    }
}
