package org.osh.cotroller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.osh.entity.CompanyEntity;
import org.osh.service.CoreService;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/200")
    public ResponseEntity<String> get200() {
        coreService.getCompany("8a27112895604338a709495fccbaa30e");
        return ResponseEntity.ok("200");
    }

    @GetMapping("/400")
    public ResponseEntity<String> get400() {
        coreService.getCompany("8a27112895604338a709495fccbaa30e");
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/404")
    public ResponseEntity<String> get404() {
        coreService.getCompany("8a27112895604338a709495fccbaa30e");
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/500")
    public ResponseEntity<String> get500() {
        coreService.getCompany("8a27112895604338a709495fccbaa30e");
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/db-error")
    public ResponseEntity<String> getDBError() {
        coreService.getCompany("error-uid");
        return ResponseEntity.ok("");
    }
}
