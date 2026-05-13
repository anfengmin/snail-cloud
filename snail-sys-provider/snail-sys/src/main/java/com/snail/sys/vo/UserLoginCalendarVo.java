package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 用户登录日历
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Schema(description = "用户登录日历")
public class UserLoginCalendarVo extends UserLoginStreakSummaryVo {

    @Schema(description = "年份")
    private Integer year;

    @Schema(description = "月份")
    private Integer month;

    @Schema(description = "每日登录情况")
    private List<UserLoginCalendarDayVo> days;
}
