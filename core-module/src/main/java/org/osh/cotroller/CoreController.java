package org.osh.cotroller;

import io.micrometer.observation.annotation.Observed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CoreController {

    @GetMapping("/hello")
    public String hello() {

        log.info("hello() called");

        return "CoreHello";
    }
}
