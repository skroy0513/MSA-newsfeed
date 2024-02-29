package kr.co.reservesite.web.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import kr.co.reservesite.client.ErrorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@ResponseBody
@RequiredArgsConstructor
@RequestMapping("/test")
@Slf4j
public class CircuitTestController {

    private final ErrorClient errorClient;

    @CircuitBreaker(name = "error1", fallbackMethod = "fallBackMethod")
    @GetMapping("/one")
    public ResponseEntity<String> test1() {
        return ResponseEntity.ok(errorClient.case1());
    }

    @Retry(name = "error2", fallbackMethod = "retry")
    @GetMapping("/two")
    public ResponseEntity<String> test2() {
        return ResponseEntity.ok(errorClient.case2());
    }

    @CircuitBreaker(name = "error3", fallbackMethod = "fallBackMethod")
    @GetMapping("/three")
    public ResponseEntity<String> test3() {
        return ResponseEntity.ok(errorClient.case3());
    }

    @GetMapping("/four")
    public ResponseEntity<String> test4() {
        return ResponseEntity.ok(errorClient.case4());
    }


    private ResponseEntity<String> fallBackMethod(Throwable throwable) {
        log.info("circuitbreaker open!!!");
        return ResponseEntity.ok(throwable.getMessage());
    }

    private ResponseEntity<String> retry(Throwable throwable) {
        log.info("retry함수 실행!!");
        return ResponseEntity.ok(throwable.getMessage());
    }
}
