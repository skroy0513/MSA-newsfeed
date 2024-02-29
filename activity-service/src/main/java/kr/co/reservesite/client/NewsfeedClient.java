package kr.co.reservesite.client;

import kr.co.reservesite.web.form.NewsfeedForm;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "newsfeed-service", url = "localhost:80/newsfeed/feign")
public interface NewsfeedClient {

    @PostMapping("/save")
    boolean saveNewsfeed(@RequestBody NewsfeedForm form);
}
