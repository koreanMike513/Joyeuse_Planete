package com.f_lab.joyeuse_planete.foods.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(
    basePackages = {
        "com.f_lab.joyeuse_planete.foods.repository",
        "com.f_lab.joyeuse_planete.core.kafka.repository",
        "com.f_lab.joyeuse_planete.core.domain.repository"
    }
)
@EntityScan(basePackages = { "com.f_lab.joyeuse_planete.core" })
public class JpaEntityScanConfig {
}
