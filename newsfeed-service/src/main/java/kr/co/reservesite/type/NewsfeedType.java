package kr.co.reservesite.type;

import lombok.Getter;

@Getter
public enum NewsfeedType {
    POST("post"),
    POST_THUMBS_UP("postThumbsUp"),
    COMMENT("comment"),
    COMMENT_THUMBS_UP("commentThumbsUp"),
    FOLLOW("follow");

    private final String newsfeedType;

    NewsfeedType(final String newsfeedType) {
        this.newsfeedType = newsfeedType;
    }
}
