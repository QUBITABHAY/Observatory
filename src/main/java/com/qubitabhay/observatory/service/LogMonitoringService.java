package com.qubitabhay.observatory.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class LogMonitoringService {

    private final String configuredLogFile;

    public LogMonitoringService(@Value("${logging.file.name:src/main/resources/logs/application.log}") String configuredLogFile) {
        this.configuredLogFile = configuredLogFile;
    }

    public List<String> getLastLogs(int lines) throws IOException {
        Path path = resolveLogFilePath();

        if (path == null || !Files.exists(path)) {
            return List.of("Log file not found");
        }

        List<String> allLines = Files.readAllLines(path);

        int start = Math.max(0, allLines.size() - lines);

        return allLines.subList(start, allLines.size());
    }

    private Path resolveLogFilePath() {
        List<Path> candidates = new ArrayList<>();
        candidates.add(Paths.get(configuredLogFile));
        candidates.add(Paths.get("src/main/resources/logs/application.log"));
        candidates.add(Paths.get("logs/application.log"));

        for (Path candidate : candidates) {
            if (Files.exists(candidate)) {
                return candidate;
            }
        }

        return null;
    }
}
