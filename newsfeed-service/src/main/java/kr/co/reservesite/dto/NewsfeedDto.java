package kr.co.reservesite.dto;

import kr.co.reservesite.entity.Newsfeed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsfeedDto {

    private Long id;
    private Long senderId;
    private String content;
    private boolean read;
    private LocalDateTime createdAt;

    public static NewsfeedDto convert(Newsfeed newsfeed) {
        return NewsfeedDto.builder()
                .id(newsfeed.getId())
                .senderId(newsfeed.getSenderId())
                .content(newsfeed.getNewsfeedType().name())
                .read(newsfeed.isRead())
                .createdAt(newsfeed.getCreatedDate())
                .build();
    }
}
