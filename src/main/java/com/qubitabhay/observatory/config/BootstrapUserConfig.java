package com.qubitabhay.observatory.config;

import com.qubitabhay.observatory.service.AppUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BootstrapUserConfig {

    @Bean
    public CommandLineRunner bootstrapAdmin(
            AppUserService appUserService,
            @Value("${observatory.security.bootstrap-admin-username}") String username,
            @Value("${observatory.security.bootstrap-admin-password}") String password
    ) {
        return args -> appUserService.bootstrapAdminIfMissing(username, password);
    }
}
