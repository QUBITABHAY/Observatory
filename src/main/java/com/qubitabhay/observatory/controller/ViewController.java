package com.qubitabhay.observatory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String index() {
        return "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/alerts")
    public String alerts() {
        return "alerts";
    }

    @GetMapping("/logs")
    public String logs() {
        return "logs";
    }

    @GetMapping("/hosts")
    public String hosts() {
        return "hosts";
    }

    @GetMapping("/metrics")
    public String metrics() {
        return "metrics";
    }

    @GetMapping("/traces")
    public String traces() {
        return "traces";
    }
}
