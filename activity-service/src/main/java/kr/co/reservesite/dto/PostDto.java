package kr.co.reservesite.dto;

import jakarta.validation.constraints.NotBlank;
import kr.co.reservesite.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {

    private Long id;
    private String nickname;
    @NotBlank(message = "제목을 입력해주세요")
    private String title;
    @NotBlank(message = "내용을 입력해주세요")
    private String content;
    private int commentCnt;
    private int likeCnt;
    private Long userId;
    private LocalDateTime createdDate;

    public static PostDto convert(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .nickname(post.getNickname())
                .title(post.getTitle())
                .content(post.getContent())
                .commentCnt(post.getCommentCnt())
                .likeCnt(post.getLikeCnt())
                .createdDate(post.getCreatedDate())
                .build();
    }
}
