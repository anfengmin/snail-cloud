package com.snail.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.snail.auth.form.EmailCodeBody;
import com.snail.auth.form.LoginBody;
import com.snail.auth.form.RegisterBody;
import com.snail.auth.service.SysLoginService;
import com.snail.common.core.constant.Constants;
import com.snail.common.core.utils.R;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * No explanation is needed
 *
 * @author Levi.
 * Created time 2025/5/11
 * @since 1.0
 */
@Slf4j
@Tag(name = "登录")
@Validated
@RequiredArgsConstructor
@RestController
public class TokenController {

    private final SysLoginService sysLoginService;

    @Schema(description = "登录")
    @PostMapping("login")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody form) {
        // 用户登录
        String accessToken = sysLoginService.login(form.getUserCode(), form.getPassWord());
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
        String token = Constants.BEARER + accessToken;
        rspMap.put(Constants.ACCESS_TOKEN, token);
        return R.ok(rspMap);
    }

    @Schema(description = "登出")
    @PostMapping("logout")
    public R<Void> logout() {
        sysLoginService.logout();
        return R.ok();
    }

    @GetMapping("/isLogin")
    public R<Object> isLogin(@RequestParam("userCode") String userCode) {
        LoginUser loginUser1 = LoginUtils.getLoginUser();
        boolean login = StpUtil.isLogin(userCode);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginUser loginUser = LoginUtils.getLoginUser("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiIxZ3NUdFZTa0F1aUZKZTB6Um5jb2pTdU5wVFdPdHUxVSIsInVzZXJJZCI6MX0.LGZOLO3D-cE31afzXr0lRDfVjAODWsy82h8dWio8dbM");
        return R.ok(login);
    }


    /**
     * 用户注册
     */
    @PostMapping("register")
    public R<Void> register(@Validated @RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok();
    }

    @PostMapping("register/email-code")
    public R<Void> sendRegisterEmailCode(@Validated @RequestBody EmailCodeBody body) {
        sysLoginService.sendRegisterEmailCode(body.getEmail());
        return R.ok();
    }
}
