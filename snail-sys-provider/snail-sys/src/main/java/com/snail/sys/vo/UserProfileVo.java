package com.snail.sys.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import com.snail.sys.vo.UserAddressVo;

import java.util.Date;
import java.util.List;

/**
 * 当前登录用户个人资料
 */
@Data
@Schema(description = "个人资料")
public class UserProfileVo {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户账号")
    private String userCode;

    @Schema(description = "用户名称")
    private String userName;

    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNo;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "所属部门名称")
    private String deptName;

    @Schema(description = "角色名称列表")
    private List<String> roleNames;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "默认地址信息")
    private UserAddressVo userAddress;
}
