package org.osh.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequiredArgsConstructor
public class BffController {

    private final RestClient restClient;

    @GetMapping("/hello")
    public String hello() {
        return "hello web";
    }

    @GetMapping("/core")
    public String core() {
        return restClient.get()
                .uri("http://localhost:8082/hello")
                .retrieve()
                .body(String.class);
    }

}
