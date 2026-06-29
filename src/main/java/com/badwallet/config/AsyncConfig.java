package com.badwallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Configuration du pool de threads utilisé par le seeder asynchrone
 * et par les listeners d'événements (Observer Pattern).
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "seederExecutor")
    public ThreadPoolTaskExecutor seederExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(100);
        executor.setThreadNamePrefix("seeder-");
        executor.initialize();
        return executor;
    }
}
