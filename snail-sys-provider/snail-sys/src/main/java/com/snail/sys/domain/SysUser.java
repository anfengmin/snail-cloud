package com.snail.sys.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.domain.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 用户信息表(User)实体类
 *
 * @author makejava
 * @since 2025-05-11 20:30:03
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
@Schema(description = "用户")
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = -79664786848105213L;


    @TableId(type = IdType.AUTO)
    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "部门ID")
    private Long deptId;

    @Schema(description = "用户账号")
    private String userCode;

    @NotBlank(message = "用户名称不能为空")
    @Schema(description = "用户名称")
    private String userName;

    @NotBlank(message = "用户昵称不能为空")
    @Schema(description = "用户昵称")
    private String nickName;

    @Schema(description = "用户类型（sys_user系统用户）")
    private String userType;

    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    @Schema(description = "用户邮箱")
    private String email;

    @Schema(description = "手机号码")
    private String phoneNo;

    @Schema(description = "用户性别（0男 1女 2未知）")
    private String sex;

    @Schema(description = "头像地址")
    private String avatar;

    @Schema(description = "密码")
    private String passWord;

    @Schema(description = "帐号状态（0正常 1停用）")
    private Integer status;

    @TableLogic
    @Schema(description = "删除标志（0:存在 1:删除）")
    private Integer deleted;

    @Schema(description = "最后登录IP")
    private String loginIp;

    @Schema(description = "最后登录时间")
    private Date loginDate;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建者")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新者")
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "备注")
    private String remark;

    @Schema(description = "部门对象")
    @TableField(exist = false)
    private SysDept dept;

    @Schema(description = "角色对象")
    @TableField(exist = false)
    private List<SysRole> roles;

    @Schema(description = "角色组")
    @TableField(exist = false)
    private Long[] roleIds;

    @Schema(description = "岗位组")
    @TableField(exist = false)
    private Long[] postIds;

    @TableField(exist = false)
    @Schema(description = "数据权限 当前角色ID")
    private Long roleId;

    /**
     * 是否管理员
     */
    public boolean isAdmin() {
        return UserConstants.ADMIN_ID.equals(this.id);
    }

}
