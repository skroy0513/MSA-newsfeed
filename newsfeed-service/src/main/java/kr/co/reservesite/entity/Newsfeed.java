package kr.co.reservesite.entity;

import jakarta.persistence.*;
import kr.co.reservesite.type.NewsfeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Builder
@Data
@Entity
@Table(name = "newsfeed")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Newsfeed extends BaseDateTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "newsfeed_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "sender_id")
    private Long senderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "newsfeed_type")
    private NewsfeedType newsfeedType;

    @Column(name = "newsfeed_type_id")
    private Long typeId;

    @Column(name = "is_read")
    private boolean read;
}
