package org.osh.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BffController {

    private final RestClient restClient;
    private final WebClient webClient;
    private final RestTemplate restTemplate;

    @GetMapping("/bff")
    public String bff() {

        log.info("bff() called");

        return "hello bff";
    }

    @GetMapping("/restclient")
    public String restclient() {

        log.info("restclient() called");

        return restClient.get()
                .uri("http://localhost:8082/company")
                .retrieve()
                .body(String.class);
    }


    @GetMapping("/webclient")
    public String webclient() {

        log.info("webclient() called");

        return webClient.get()
                .uri("http://localhost:8082/company")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @GetMapping("/resttemplate")
    public String resttemplate() {

        log.info("resttemplate() called");

        return restTemplate.getForObject("http://localhost:8082/company", String.class);
    }

}
