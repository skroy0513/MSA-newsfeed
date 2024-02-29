package kr.co.reservesite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Data
@Builder
@Entity
@Table(name = "post")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseDateTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id", updatable = false)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "post_title")
    private String title;

    @Column(name = "post_content")
    private String content;

    @Column(name = "user_nickname")
    private String nickname;

    @Column(name = "post_comment_cnt")
    private int commentCnt;

    @Column(name = "post_like_cnt")
    private int likeCnt;

    @Column(name = "is_deleted")
    private boolean deleted;
}
