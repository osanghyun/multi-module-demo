package org.osh.config;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.LinkData;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.samplers.Sampler;
import io.opentelemetry.sdk.trace.samplers.SamplingResult;
import io.opentelemetry.semconv.ResourceAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CommonConfig {

    private final ApiProperties apiProperties;

    @Bean
    public OpenTelemetry openTelemetry() {

        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.builder()
                        .put(ResourceAttributes.SERVICE_NAME, "core-service")
                        .build()));

        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder()
                .setEndpoint(apiProperties.tracing().grpcCollector())
                .build();

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .setSampler(customSampler())
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .buildAndRegisterGlobal();
    }

    private Sampler customSampler() {
        //TODO: add sampling ratio properties
        Sampler rootSampler = Sampler.traceIdRatioBased(1.0);
        return new Sampler() {
            @Override
            public SamplingResult shouldSample(Context parentContext,
                                               String traceId,
                                               String name,
                                               SpanKind spanKind,
                                               Attributes attributes,
                                               List<LinkData> parentLinks) {
                // Check if the span is of kind SERVER, indicating an incoming request
                if (spanKind == SpanKind.CLIENT) {
                    // Use the correct attribute key to identify the request path or endpoint
                    String httpRoute = attributes.get(AttributeKey.stringKey("http.url"));
                    System.out.println(parentContext.toString());
                    System.out.println(attributes);
                    // If the route is for an actuator endpoint, ignore it
                    if (httpRoute != null && httpRoute.startsWith("/actuator")) {
                        return SamplingResult.drop();
                    }
                }
                // For other spans, defer to the otelSampler
                return rootSampler.shouldSample(parentContext, traceId, name, spanKind, attributes, parentLinks);
            }

            @Override
            public String getDescription() {
                return "CustomSamplerForActuator";
            }
        };
    }

}
