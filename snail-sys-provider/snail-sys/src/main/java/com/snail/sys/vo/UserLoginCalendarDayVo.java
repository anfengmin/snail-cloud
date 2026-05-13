package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户登录日历单日
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Schema(description = "用户登录日历单日")
public class UserLoginCalendarDayVo {

    @Schema(description = "日期")
    private String date;

    @Schema(description = "是否登录")
    private Boolean loggedIn;
}
