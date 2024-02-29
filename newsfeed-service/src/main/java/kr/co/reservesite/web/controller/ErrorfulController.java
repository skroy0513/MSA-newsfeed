package kr.co.reservesite.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalTime;
import java.util.Random;

@RestController
@RequestMapping("/newsfeed/feign")
@Slf4j
public class ErrorfulController {

    @GetMapping("/errorful/case1")
    public ResponseEntity<String> case1() {
        // Simulate 10% chance of 500 error
        if (new Random().nextInt(100) < 10) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }

        return ResponseEntity.ok("Normal response");
    }

    @GetMapping("/errorful/case2")
    public ResponseEntity<String> case2() {
        // Simulate blocking requests every first 10 seconds
        LocalTime currentTime = LocalTime.now();
        int currentSecond = currentTime.getSecond();

        log.info("현재 초 -> {}", currentSecond);

        if (currentSecond < 10) {
            log.info("try");
            // Simulate a delay (block) for 10 seconds
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return ResponseEntity.status(503).body("Service Unavailable");
        }
        return ResponseEntity.ok("Normal response");
    }

    @GetMapping("/errorful/case3")
    public ResponseEntity<String> case3() {
        // Simulate 500 error every first 10 seconds
        LocalTime currentTime = LocalTime.now();
        int currentSecond = currentTime.getSecond();

        log.info("현재 초 -> {}", currentSecond);

        if (currentSecond < 10) {
            return ResponseEntity.status(500).body("Internal Server Error");
        }
        return ResponseEntity.ok("case3 Normal response");
    }

    @GetMapping("/errorful/case4")
    public ResponseEntity<String> case4() {
        return ResponseEntity.ok("hello");
    }
}
