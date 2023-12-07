package com.demo.app.ws.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.jackson.ObjectMapperCustomizer;
import io.quarkus.arc.All;
import jakarta.inject.Singleton;
import jakarta.ws.rs.Produces;

import java.util.List;

public class CustomObjectMapper {
    // Replaces the CDI producer for ObjectMapper built into Quarkus
    @Singleton
    @Produces
    ObjectMapper objectMapper(@All List<ObjectMapperCustomizer> customizers) {
        ObjectMapper mapper = new ObjectMapper(); // Custom `ObjectMapper`

        // Apply all ObjectMapperCustomizer beans (incl. Quarkus)
        for (ObjectMapperCustomizer customizer : customizers) {
            customizer.customize(mapper);
        }

        return mapper;
    }
}
