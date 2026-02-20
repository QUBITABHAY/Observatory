package com.qubitabhay.observatory.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class LogMonitoringService {

    public List<String> getLastLogs(int lines) throws IOException {

        Path path = Path.of("logs/application.log");

        if (!Files.exists(path)) {
            return List.of("Log file not found");
        }

        List<String> allLines = Files.readAllLines(path);

        int start = Math.max(0, allLines.size() - lines);

        return allLines.subList(start, allLines.size());
    }
}
