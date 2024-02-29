package kr.co.reservesite.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "errorful-test", url = "localhost:80/newsfeed/feign")
public interface ErrorClient {

    @GetMapping("/errorful/case1")
    String case1();

    @GetMapping("/errorful/case2")
    String case2();

    @GetMapping("/errorful/case3")
    String case3();

    @GetMapping("/errorful/case4")
    String case4();
}
