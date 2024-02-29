package kr.co.reservesite.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@Builder
@Table(name = "cmt_thumbs")
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
public class CmtThumbs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cmt_thumbs_id")
    private Long id;

    @Column(name = "comment_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Long commentId;

    @Column(name = "user_id")
    private Long userId;
}
