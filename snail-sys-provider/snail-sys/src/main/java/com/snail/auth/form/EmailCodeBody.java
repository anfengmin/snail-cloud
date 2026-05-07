package com.snail.auth.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 邮箱验证码发送请求
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Schema(description = "邮箱验证码发送请求")
public class EmailCodeBody {

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Length(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;
}
