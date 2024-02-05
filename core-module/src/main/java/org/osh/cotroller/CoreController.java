package org.osh.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osh.entity.CompanyEntity;
import org.osh.service.CoreService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CoreController {

    private final CoreService coreService;

    @GetMapping("/hello")
    public String hello() {

        log.info("hello() called");

        return "CoreHello";
    }

    @GetMapping("/company")
    public CompanyEntity getCompany() {
        return coreService.getCompany("8a27112895604338a709495fccbaa30e");
    }
}
