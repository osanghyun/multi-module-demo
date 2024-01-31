package org.osh.cotroller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoreController {

    @GetMapping("/hello")
    public String hello() {
        return "CoreHello";
    }
}
