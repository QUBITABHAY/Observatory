package com.qubitabhay.observatory.service;

import com.qubitabhay.observatory.model.Host;
import com.qubitabhay.observatory.model.Metric;
import com.qubitabhay.observatory.model.ServiceEntity;
import com.qubitabhay.observatory.repository.HostRepository;
import com.qubitabhay.observatory.repository.ServiceEntityRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
public class LocalMetricsCollector {

    private static final Logger log = LoggerFactory.getLogger(LocalMetricsCollector.class);
    private static final String LOCAL_SERVICE_NAME = "observatory";

    private final SystemMetricsService systemMetricsService;
    private final MetricService metricService;
    private final HostRepository hostRepository;
    private final ServiceEntityRepository serviceEntityRepository;

    private Host localHost;
    private ServiceEntity localService;

    public LocalMetricsCollector(SystemMetricsService systemMetricsService,
                                 MetricService metricService,
                                 HostRepository hostRepository,
                                 ServiceEntityRepository serviceEntityRepository) {
        this.systemMetricsService = systemMetricsService;
        this.metricService = metricService;
        this.hostRepository = hostRepository;
        this.serviceEntityRepository = serviceEntityRepository;
    }

    @PostConstruct
    public void init() {
        try {
            String hostname = InetAddress.getLocalHost().getHostName();
            String ip = InetAddress.getLocalHost().getHostAddress();

            localHost = hostRepository.findByHostname(hostname).orElseGet(() -> {
                Host h = new Host();
                h.setHostname(hostname);
                h.setIpAddress(ip);
                h.setEnvironment("local");
                Host saved = hostRepository.save(h);
                log.info("Auto-registered local host: {} ({})", hostname, ip);
                return saved;
            });

            localService = serviceEntityRepository
                    .findByHost_Id(localHost.getId()).stream()
                    .filter(s -> s.getName().equals(LOCAL_SERVICE_NAME))
                    .findFirst()
                    .orElseGet(() -> {
                        ServiceEntity s = new ServiceEntity();
                        s.setName(LOCAL_SERVICE_NAME);
                        s.setHost(localHost);
                        ServiceEntity saved = serviceEntityRepository.save(s);
                        log.info("Auto-registered local service: {}", LOCAL_SERVICE_NAME);
                        return saved;
                    });

        } catch (Exception e) {
            log.error("Failed to auto-register local host/service", e);
        }
    }

    @Scheduled(fixedRateString = "${observatory.collector.interval-ms:30000}")
    public void collect() {
        if (localHost == null || localService == null) {
            log.warn("Local host/service not registered, skipping metric collection");
            return;
        }

        try {
            double cpu = systemMetricsService.getCpuUsage();
            long usedMemory = systemMetricsService.getUsedMemory();
            long totalMemory = systemMetricsService.getTotalMemory();

            saveMetric("cpu_usage_percent", cpu);
            saveMetric("memory_used_bytes", (double) usedMemory);
            saveMetric("memory_total_bytes", (double) totalMemory);
            saveMetric("memory_usage_percent", totalMemory > 0 ? (usedMemory * 100.0 / totalMemory) : 0);

            log.debug("Collected local metrics — CPU: {:.1f}%, MEM: {}/{}", cpu, usedMemory, totalMemory);
        } catch (Exception e) {
            log.error("Error collecting local metrics", e);
        }
    }

    private void saveMetric(String name, double value) {
        Metric m = new Metric();
        m.setMetricName(name);
        m.setMetricType("gauge");
        m.setValue(value);
        m.setHost(localHost);
        m.setService(localService);
        metricService.saveMetric(m);
    }
}
