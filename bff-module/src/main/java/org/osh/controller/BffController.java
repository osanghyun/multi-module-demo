package org.osh.controller;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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

    @GetMapping("/200")
    public String get200() {

        log.info("200() called");

        return restClient.get()
                .uri("http://localhost:8082/200")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/400")
    public String get400() {

        log.info("400() called");

        return restClient.get()
                .uri("http://localhost:8082/400")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/404")
    public String get404() {

        log.info("404() called");

        return restClient.get()
                .uri("http://localhost:8082/404")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/500")
    public String get500() {

        log.info("500() called");

        return restClient.get()
                .uri("http://localhost:8082/500")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/db-error")
    public String getDBError() {

        log.info("getDBError() called");

        return restClient.get()
                .uri("http://localhost:8082/db-error")
                .retrieve()
                .body(String.class);
    }

    @GetMapping("/catch-error")
    public String catchError() {
        log.info("catchError() called");

        try {
            return restClient.get()
                    .uri("http://localhost:8082/400")
                    .retrieve()
                    .body(String.class);
        }
        catch (RuntimeException e) {
            return "catched";
        }

    }

}
