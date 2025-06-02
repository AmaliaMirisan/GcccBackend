package com.example.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.example.project.repository")
@EntityScan(basePackages = "com.example.project.domain.model")
@EnableWebMvc
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }
}
