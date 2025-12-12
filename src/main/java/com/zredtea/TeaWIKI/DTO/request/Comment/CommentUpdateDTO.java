package com.zredtea.TeaWIKI.DTO.request.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "评论更新表单对象")
@Data
public class CommentUpdateDTO {
    @Schema(description = "评论ID", example = "100000001")
    @NotNull
    private Integer commentId;

    @Schema(description = "用户ID", example = "100001")
    @NotNull
    private Integer userId;

    @Schema(description = "评论内容", example = "现在不喜欢了")
    private String content;

    @Schema(description = "评论评分", example = "1.0")
    @Min(value = 0, message = "评分不能小于0!")
    @Max(value = 5, message = "评分不能大于5!")
    private Double rating;
}
