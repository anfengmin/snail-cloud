package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户连续登录摘要
 *
 * @author Levi
 * @since 1.0
 */
@Data
@Schema(description = "用户连续登录摘要")
public class UserLoginStreakSummaryVo {

    @Schema(description = "当前连续登录天数")
    private Integer currentStreakDays;

    @Schema(description = "历史最长连续登录天数")
    private Integer maxStreakDays;

    @Schema(description = "最后一次登录自然日")
    private String lastLoginDate;
}
