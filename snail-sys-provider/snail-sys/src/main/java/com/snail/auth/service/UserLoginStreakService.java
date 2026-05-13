package com.snail.auth.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.snail.common.core.constant.CacheConstants;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.sys.domain.SysUser;
import com.snail.sys.service.SysUserService;
import com.snail.sys.vo.UserLoginCalendarDayVo;
import com.snail.sys.vo.UserLoginCalendarVo;
import com.snail.sys.vo.UserLoginStreakSummaryVo;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBitSet;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户连续登录服务
 *
 * @author Levi
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class UserLoginStreakService {

    private static final ZoneId LOGIN_ZONE_ID = ZoneId.of("Asia/Shanghai");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final String FIELD_LAST_LOGIN_DATE = "lastLoginDate";
    private static final String FIELD_CURRENT_STREAK_DAYS = "currentStreakDays";
    private static final String FIELD_MAX_STREAK_DAYS = "maxStreakDays";
    private static final String FIELD_LAST_LOGIN_AT = "lastLoginAt";

    private final SysUserService sysUserService;

    /**
     * 记录登录成功
     *
     * @param userId      用户ID
     * @param loginIp     登录IP
     * @param loginDate   登录时间
     * @return 登录成功信息
     */
    public UserLoginStreakSummaryVo recordLoginSuccess(Long userId, String loginIp, Date loginDate) {
        LocalDate today = toLocalDate(loginDate);
        RLock lock = RedisUtils.getClient().getLock(CacheConstants.LOGIN_STREAK_LOCK_KEY + userId);
        lock.lock();
        try {
            RMap<String, String> summary = RedisUtils.getClient().getMap(CacheConstants.LOGIN_STREAK_SUMMARY_KEY + userId);
            String lastLoginDate = summary.get(FIELD_LAST_LOGIN_DATE);
            int currentStreakDays = Convert.toInt(summary.get(FIELD_CURRENT_STREAK_DAYS), 0);
            int maxStreakDays = Convert.toInt(summary.get(FIELD_MAX_STREAK_DAYS), 0);

            String todayText = DATE_FORMATTER.format(today);
            if (todayText.equals(lastLoginDate)) {
                currentStreakDays = Math.max(currentStreakDays, 1);
            } else if (DATE_FORMATTER.format(today.minusDays(1)).equals(lastLoginDate)) {
                currentStreakDays = Math.max(currentStreakDays, 0) + 1;
            } else {
                currentStreakDays = 1;
            }

            maxStreakDays = Math.max(maxStreakDays, currentStreakDays);

            summary.put(FIELD_LAST_LOGIN_DATE, todayText);
            summary.put(FIELD_CURRENT_STREAK_DAYS, String.valueOf(currentStreakDays));
            summary.put(FIELD_MAX_STREAK_DAYS, String.valueOf(maxStreakDays));
            summary.put(FIELD_LAST_LOGIN_AT, String.valueOf(loginDate.getTime()));

            markLoginCalendar(userId, today);
            updateUserLoginMeta(userId, loginIp, loginDate);

            UserLoginStreakSummaryVo vo = new UserLoginStreakSummaryVo();
            vo.setCurrentStreakDays(currentStreakDays);
            vo.setMaxStreakDays(maxStreakDays);
            vo.setLastLoginDate(todayText);
            return vo;
        } finally {
            lock.unlock();
        }
    }

    public UserLoginStreakSummaryVo getSummary(Long userId) {
        RMap<String, String> summary = RedisUtils.getClient().getMap(CacheConstants.LOGIN_STREAK_SUMMARY_KEY + userId);
        UserLoginStreakSummaryVo vo = new UserLoginStreakSummaryVo();
        vo.setCurrentStreakDays(Convert.toInt(summary.get(FIELD_CURRENT_STREAK_DAYS), 0));
        vo.setMaxStreakDays(Convert.toInt(summary.get(FIELD_MAX_STREAK_DAYS), 0));
        vo.setLastLoginDate(summary.get(FIELD_LAST_LOGIN_DATE));
        return vo;
    }

    /**
     * 获取登录日历
     *
     * @param userId 用户ID
     * @param year   年份
     * @param month  月份
     * @return 登录日历
     */
    public UserLoginCalendarVo getCalendar(Long userId, Integer year, Integer month) {
        LocalDate now = LocalDate.now(LOGIN_ZONE_ID);
        int targetYear = ObjectUtil.defaultIfNull(year, now.getYear());
        int targetMonth = ObjectUtil.defaultIfNull(month, now.getMonthValue());

        YearMonth yearMonth = YearMonth.of(targetYear, targetMonth);
        RBitSet bitSet = RedisUtils.getClient().getBitSet(buildCalendarKey(userId, targetYear));

        // 构建日历
        List<UserLoginCalendarDayVo> days = new ArrayList<>(yearMonth.lengthOfMonth());
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            UserLoginCalendarDayVo dayVo = new UserLoginCalendarDayVo();
            dayVo.setDate(DATE_FORMATTER.format(date));
            dayVo.setLoggedIn(bitSet.get(date.getDayOfYear() - 1L));
            days.add(dayVo);
        }

        UserLoginStreakSummaryVo summary = getSummary(userId);
        UserLoginCalendarVo vo = new UserLoginCalendarVo();
        vo.setYear(targetYear);
        vo.setMonth(targetMonth);
        vo.setDays(days);
        vo.setCurrentStreakDays(summary.getCurrentStreakDays());
        vo.setMaxStreakDays(summary.getMaxStreakDays());
        vo.setLastLoginDate(summary.getLastLoginDate());
        return vo;
    }

    /**
     * 记录登录日历
     *
     * @param userId 用户ID
     * @param date   登录日期
     */
    private void markLoginCalendar(Long userId, LocalDate date) {
        RBitSet bitSet = RedisUtils.getClient().getBitSet(buildCalendarKey(userId, date.getYear()));
        bitSet.set(date.getDayOfYear() - 1L, true);
    }

    /**
     * 构建日历缓存Key
     *
     * @param userId 用户ID
     * @param year   年份
     * @return 日历缓存Key
     */
    private String buildCalendarKey(Long userId, int year) {
        return CacheConstants.LOGIN_CALENDAR_KEY + userId + ":" + year;
    }

    /**
     * 将Date转换为LocalDate
     *
     * @param loginDate 登录时间
     * @return LocalDate
     */
    private LocalDate toLocalDate(Date loginDate) {
        return Instant.ofEpochMilli(loginDate.getTime()).atZone(LOGIN_ZONE_ID).toLocalDate();
    }

    /**
     * 更新用户登录信息
     *
     * @param userId   用户ID
     * @param loginIp  登录IP
     * @param loginDate 登录时间
     */
    private void updateUserLoginMeta(Long userId, String loginIp, Date loginDate) {
        LambdaUpdateWrapper<SysUser> updateWrapper = new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getId, userId)
                .set(SysUser::getLoginIp, loginIp)
                .set(SysUser::getLoginDate, loginDate);
        sysUserService.update(updateWrapper);
    }
}
