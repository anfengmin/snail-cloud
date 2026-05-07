package com.snail.auth.form;

import com.snail.common.core.constant.UserConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotBlank;

/**
 * 用户登录对象
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Data
@Schema(description = "登录对象")
@NoArgsConstructor
public class LoginBody {

    /**
     * 登录账号
     */
    @Schema(description = "用户账号或邮箱")
    @NotBlank(message = "{user.username.not.blank}")
    @Size(min = 2, max = 50, message = "{user.username.length.valid}")
    private String userCode;

    /**
     * 用户密码
     */
    @Schema(description = "用户密码")
    @NotBlank(message = "{user.password.not.blank}")
    @Size(min = UserConstants.PASSWORD_MIN_LENGTH, max = UserConstants.PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    private String passWord;
}
