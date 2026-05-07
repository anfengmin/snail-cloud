package com.snail.auth.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.snail.auth.form.EmailCodeBody;
import com.snail.auth.form.LoginBody;
import com.snail.auth.form.RegisterBody;
import com.snail.auth.service.SysLoginService;
import com.snail.common.core.constant.Constants;
import com.snail.common.core.enums.BusinessType;
import com.snail.common.core.utils.MessageUtils;
import com.snail.common.core.utils.R;
import com.snail.common.log.annotation.Log;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * method
 *
 * @author Anfm
 * Created time 2025/5/16
 * @since 1.0
 */
@Slf4j
@Tag(name = "登录")
@Validated
@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class OauthController {



    private final SysLoginService sysLoginService;

    @Schema(description = "登录")
    @PostMapping("login")
    @Operation(summary = "用户登录")
    public R<Map<String, Object>> login(@Validated @RequestBody LoginBody form) {
        // 用户登录
        String accessToken = sysLoginService.login(form.getUserCode(), form.getPassWord());
        // 接口返回信息
        Map<String, Object> rspMap = new HashMap<>();
//        String token = Constants.BEARER + accessToken;
        rspMap.put(Constants.ACCESS_TOKEN, accessToken);
        return R.ok(rspMap);
    }

    @Schema(description = "登出")
    @PostMapping("logout")
    @Operation(summary = "用户登出")
    public R<Void> logout() {
        sysLoginService.logout();
        return R.ok();
    }

    @GetMapping("/isLogin")
    @Operation(summary = "判断用户是否登录")
    public R<Object> isLogin(@RequestParam("userCode") String userCode) {
        LoginUser loginUser1 = LoginUtils.getLoginUser();
        boolean login = StpUtil.isLogin(userCode);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
        LoginUser loginUser = LoginUtils.getLoginUser("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiIxZ3NUdFZTa0F1aUZKZTB6Um5jb2pTdU5wVFdPdHUxVSIsInVzZXJJZCI6MX0.LGZOLO3D-cE31afzXr0lRDfVjAODWsy82h8dWio8dbM");
        return R.ok(login);
    }


    @Log(title = "用户注册", businessType = BusinessType.INSERT)
    @PostMapping("register")
    @Operation(summary = "用户注册")
    public R<Void> register(@Validated @RequestBody RegisterBody registerBody) {
        // 用户注册
        sysLoginService.register(registerBody);
        return R.ok();
    }

    @Log(title = "发送注册邮箱验证码", businessType = BusinessType.INSERT)
    @PostMapping("register/email-code")
    @Operation(summary = "发送注册邮箱验证码")
    public R<Void> sendRegisterEmailCode(@Validated @RequestBody EmailCodeBody body) {
        sysLoginService.sendRegisterEmailCode(body.getEmail());
        return R.ok();
    }


    @PostMapping("token")
    @Operation(summary = "获取token")
    public R<Map<String, Object>> token(@ModelAttribute LoginBody form) {

        log.info("登录信息：{}", form);
        JSONObject user = getUser();


        return R.ok();
    }

    private  JSONObject getUser() {
        HttpResponse response;
        JSONObject entries;
        try {
            response = HttpRequest.post("").form("", "").execute();
            int status = response.getStatus();
            if (status != 200) {
                throw new RuntimeException("登录失败");
            }
            String body = response.body();
            return JSONUtil.parseObj(body);
        } catch (Exception e) {
            log.error("登录失败信息：{}", e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/test1")
    public R<Void> test1(String code) {
        return R.ok(MessageUtils.message(code));
    }
}
