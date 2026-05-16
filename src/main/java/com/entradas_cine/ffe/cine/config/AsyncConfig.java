package com.entradas_cine.ffe.cine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Executor dedicado para las traducciones DeepL.
 * Un solo hilo secuencial + cola ilimitada evita superar el rate-limit
 * de la API gratuita cuando se traducen varias películas a la vez.
 */
@Configuration
public class AsyncConfig {

    @Bean(name = "traduccionExecutor")
    public Executor traduccionExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("traduccion-");
        executor.initialize();
        return executor;
    }
}
