package kr.co.reservesite.web.controller;

import kr.co.reservesite.dto.NewsfeedDto;
import kr.co.reservesite.service.NewsfeedService;
import kr.co.reservesite.web.form.NewsfeedForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/newsfeed")
@Slf4j
public class NewsfeedController {

    private final NewsfeedService newsfeedService;

    // 내 뉴스피드를 최신 순으로 정렬하여 5개씩 나타낸다.
    // page번호 제공(0부터 시작)
    // 뉴스피드에서 해당 알림을 클릭 시 읽음 상태가 바뀐다.(프론트에서 색이나  구별 필요)
    @GetMapping("/my")
    public ResponseEntity<Page<NewsfeedDto>> showMyNewsfeed(@RequestHeader HttpHeaders headers,
                                                            @RequestParam("page") int page) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        Page<NewsfeedDto> newsfeedDtos = newsfeedService.showMyNewsfeed(userId, page).map(NewsfeedDto::convert);
        return ResponseEntity.ok(newsfeedDtos);
    }

    // 해당 뉴스피드에 id를 전달받아 읽음으로 하는 url필요
    // 페이지 이동 없이 버튼으로 읽음을 표시
    @GetMapping("/read")
    public ResponseEntity<String> readNewsfeed(@RequestHeader HttpHeaders headers,
                                               @RequestParam("id") Long id) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = newsfeedService.readNewsfeed(userId, id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/feign/save")
    public ResponseEntity<Boolean> saveNewsfeed(
            @RequestBody NewsfeedForm form) {
        newsfeedService.createNewsfeed(form);
        return ResponseEntity.ok(true);
    }
}
