package com.jcoder.script;

import com.jcoder.script.config.EngineProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 启动类
 *
 * @author Jcoder
 */
@ComponentScan(basePackages = {"com.jcoder.script"})
@EnableAsync
@Configuration
@EnableConfigurationProperties({EngineProperties.class})
@SpringBootApplication
public class EngineApplication {
    private static final Logger LOGGER = LoggerFactory.getLogger(EngineApplication.class);

    public static void main(String[] args) {
        try {
            SpringApplication.run(EngineApplication.class, args);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}


