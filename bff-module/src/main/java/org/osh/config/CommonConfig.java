package org.osh.config;

import io.micrometer.observation.ObservationRegistry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.resources.Resource;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.semconv.ResourceAttributes;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.actuate.metrics.web.reactive.client.ObservationWebClientCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.ClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class                    CommonConfig {
    @Bean
    public RestClient restClient(RestClient.Builder builder) {

        return builder.build();
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

//    @Bean
//    public WebClient webClient(WebClient.Builder builder) {
//        return builder.build();
//    }

    @Bean
    public OpenTelemetry openTelemetry() {
        OtlpGrpcSpanExporter spanExporter = OtlpGrpcSpanExporter.builder().build();

        Resource resource = Resource.getDefault()
                .merge(Resource.create(Attributes.builder()
                        .put(ResourceAttributes.SERVICE_NAME, "bff-service")
                        .build()));

        SdkTracerProvider sdkTracerProvider = SdkTracerProvider.builder()
                .setResource(resource)
                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                .build();

        return OpenTelemetrySdk.builder()
                .setTracerProvider(sdkTracerProvider)
                .buildAndRegisterGlobal();
    }

//
//    @Bean
//    public WebClient.Builder webClientBuilder(ObjectProvider<WebClientCustomizer> customizerProvider) {
//        WebClient.Builder builder = WebClient.builder();
//        customizerProvider.orderedStream().forEach((customizer) -> customizer.customize(builder));
//        return builder;
//    }
//
//    @Bean
//    ObservationWebClientCustomizer observationWebClientCustomizer1(ObjectProvider<ClientRequestObservationConvention> customConvention) {
//        String name = "http.client.requests";
//        System.out.println("Customizer");
//        ClientRequestObservationConvention observationConvention = customConvention
//                .getIfAvailable(() -> new DefaultClientRequestObservationConvention(name));
//        return new ObservationWebClientCustomizer(ObservationRegistry.create(), observationConvention);
//    }
}
