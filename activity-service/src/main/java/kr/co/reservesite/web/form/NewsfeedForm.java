package kr.co.reservesite.web.form;

import kr.co.reservesite.web.type.NewsfeedType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsfeedForm {
    private Long userId;
    private Long senderId;
    private NewsfeedType type;
    private Long typeId;
}
