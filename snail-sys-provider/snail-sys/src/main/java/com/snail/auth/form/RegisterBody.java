package com.snail.auth.form;

import com.snail.common.core.constant.UserConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * 注册对象
 *
 * @author Anfm
 * @since 1.0
 */
@Data
@Schema(description = "注册对象")
public class RegisterBody {

    @Schema(description = "邮箱")
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @Length(max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @Schema(description = "邮箱验证码")
    @NotBlank(message = "{email.code.not.blank}")
    private String emailCode;

    @Schema(description = "用户密码")
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    private String passWord;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户类型")
    private String userType;
}
