package kr.co.reservesite.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostForm {

    @NotBlank(message = "제목을 입력해주세요")
    private String title;

    @NotBlank(message = "내용을 입력해주세요")
    private String content;
}
