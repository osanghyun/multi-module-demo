package org.osh.utils;

import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Component
public class WebClientUtil {

    @PostConstruct
    public void init() {
        this.webClient = webClient_;
        log.error("init");
    }

    private final WebClient webClient_;
    private static WebClient webClient;

    public static String test() {
        return webClient.get()
                .uri("http://localhost:8082/company")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}