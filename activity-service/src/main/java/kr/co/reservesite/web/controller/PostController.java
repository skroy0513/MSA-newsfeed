package kr.co.reservesite.web.controller;

import jakarta.validation.Valid;
import kr.co.reservesite.dto.CommentDto;
import kr.co.reservesite.dto.PostDto;
import kr.co.reservesite.service.PostService;
import kr.co.reservesite.web.form.PostForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/read")
    public ResponseEntity<PostDto> readPost(@RequestParam("id") Long postId) {
        PostDto postDto = postService.readPost(postId);
        return ResponseEntity.ok().body(postDto);
    }

    @GetMapping("/my-posts")
    public ResponseEntity<Page<PostDto>> myFollowerPost(@RequestParam("page") int page,
                                                        @RequestHeader HttpHeaders headers) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        Page<PostDto> postDtos = postService.getFollowerPost(userId, page).map(PostDto::convert);
        return ResponseEntity.ok(postDtos);
    }

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestHeader HttpHeaders headers,
                                              @RequestBody @Valid PostForm postForm) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        PostDto post = postService.createPost(userId, postForm);
        return ResponseEntity.ok().body(post);
    }

    @PutMapping("/modify")
    public ResponseEntity<PostDto> modifyPost(@RequestHeader HttpHeaders headers,
                                              @RequestBody @Valid PostDto postdto) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        PostDto post = postService.modifyPost(userId, postdto);
        return ResponseEntity.ok().body(post);
    }

    @PostMapping("/{postId}/cmt")
    public ResponseEntity<CommentDto> createComment(@RequestHeader HttpHeaders headers,
                                                    @RequestBody String content,
                                                    @PathVariable Long postId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        CommentDto cmtDto = postService.createCmt(userId, content, postId);
        return ResponseEntity.ok().body(cmtDto);
    }

    @PutMapping("/{postId}/cmt")
    public ResponseEntity<CommentDto> modifyComment(@RequestHeader HttpHeaders headers,
                                                    @RequestBody String content,
                                                    @RequestParam("cmtId") Long cmtId,
                                                    @PathVariable Long postId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        CommentDto cmtDto = postService.modifyCmt(userId, content, cmtId, postId);
        return ResponseEntity.ok(cmtDto);
    }

    @DeleteMapping("/{postId}/cmt")
    public ResponseEntity<String> deleteComment(@RequestHeader HttpHeaders headers,
                                                @RequestParam("cmtId") Long cmtId,
                                                @PathVariable Long postId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = postService.deleteCmt(userId, cmtId, postId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{postId}/thumbs-up")
    public ResponseEntity<String> thumbsUpPost(@RequestHeader HttpHeaders headers,
                                               @PathVariable Long postId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = postService.thumbsUpPost(userId, postId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{postId}/thumbs-down")
    public ResponseEntity<String> thumbsDownPost(@RequestHeader HttpHeaders headers,
                                                 @RequestParam("thumbsId") Long thumbsId,
                                                 @PathVariable Long postId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = postService.thumbsDownPost(userId, postId, thumbsId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{cmtId}/cmt-thumbs-up")
    public ResponseEntity<String> thumbsUpCmt(@RequestHeader HttpHeaders headers,
                                              @PathVariable Long cmtId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = postService.thumbsUpCmt(userId, cmtId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{cmtId}/cmt-thumbs-down")
    public ResponseEntity<String> thumbsDownCmt(@RequestHeader HttpHeaders headers,
                                                @RequestParam("cmtThumbsId") Long cmtThumbsId,
                                                @PathVariable Long cmtId) {
        Long userId = Long.valueOf(headers.get("userId").get(0));
        String result = postService.thumbsDownCmt(userId, cmtId, cmtThumbsId);
        return ResponseEntity.ok(result);
    }
}
