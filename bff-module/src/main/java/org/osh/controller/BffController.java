package org.osh.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BffController {

    private final RestClient restClient;

    @GetMapping("/hello")
    public String hello() {

        log.info("hello() called");

        return "hello web";
    }

    @GetMapping("/core")
    public String core() {

        log.info("core() called");

        return restClient.get()
                .uri("http://localhost:8082/hello")
                .retrieve()
                .body(String.class);
    }

}
