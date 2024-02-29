package kr.co.reservesite.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "localhost:80/user/feign")
public interface Userclient {

    @GetMapping("/get")
    String getNickname(@RequestParam Long userId);
}
