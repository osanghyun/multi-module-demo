package org.osh.config;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;


@Validated
@ConfigurationProperties(prefix = "api")
public record ApiProperties (
        @NotNull Tracing tracing
){
    public record Tracing(
            @NotEmpty String grpcCollector
    ) {}
}
