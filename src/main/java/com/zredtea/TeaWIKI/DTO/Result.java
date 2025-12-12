package com.zredtea.TeaWIKI.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

// 统一响应类
@Schema(description = "统一响应类")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {
    @Schema(description = "响应码", example = "200")
    private Integer code;
    @Schema(description = "响应信息", example = "success")
    private String message;
    @Schema(description = "响应数据", example = "DTO")
    private T data;
    @Schema(description = "时间戳", example = "1145141919810")
    private Long timestamp;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data, System.currentTimeMillis());

    }

    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null, System.currentTimeMillis());
    }

    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null, System.currentTimeMillis());
    }
}
