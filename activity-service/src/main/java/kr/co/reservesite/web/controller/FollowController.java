package kr.co.reservesite.web.controller;

import kr.co.reservesite.entity.Follow;
import kr.co.reservesite.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/follow")
@Slf4j
public class FollowController {

    private final FollowService followService;

    @PostMapping("/register")
    public ResponseEntity<Long> followUser(@RequestHeader HttpHeaders headers,
                                           @RequestParam("toId") Long toUserId) {
        Long fromUserId = Long.valueOf(headers.get("userId").get(0));
        Long toUser = followService.followUser(fromUserId, toUserId);

        return ResponseEntity.ok().body(toUser);
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<Long> followCancel(@RequestHeader HttpHeaders headers,
                                             @RequestParam("toId") Long toUserId) {
        Long fromUserId = Long.valueOf(headers.get("userId").get(0));
        Long toUser = followService.followCancel(fromUserId, toUserId);

        return ResponseEntity.ok().body(toUser);
    }

    //나를 팔로우하고 있는 유저목록
    @GetMapping("/show-follower")
    public ResponseEntity<List<Follow>> showMyFollower(@RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        List<Follow> follower = followService.showMyFollower(userId);

        return ResponseEntity.ok(follower);
    }

    //내가 팔로우하고 있는 유저목록
    @GetMapping("/show-following")
    public ResponseEntity<List<Follow>> showMyFollowing(@RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        List<Follow> follower = followService.showMyFollowing(userId);

        return ResponseEntity.ok(follower);
    }

}
