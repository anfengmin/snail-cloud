package com.snail.sys.service.impl;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import com.snail.common.core.constant.CacheConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.snail.common.core.constant.UserConstants;
import com.snail.common.core.excel.model.ExcelExportRequest;
import com.snail.common.core.excel.model.ExcelImportResult;
import com.snail.common.core.excel.model.ExcelImportRowResult;
import com.snail.common.core.excel.model.ExcelImportRequest;
import com.snail.common.core.excel.model.ExcelRow;
import com.snail.common.core.excel.service.ExcelExportService;
import com.snail.common.core.excel.service.ExcelImportService;
import com.snail.common.core.exception.ServiceException;
import com.snail.common.core.service.DictService;
import com.snail.common.core.exception.user.UserException;
import com.snail.common.core.utils.R;
import com.snail.common.redis.utils.RedisUtils;
import com.snail.common.satoken.utils.LoginUtils;
import com.snail.sys.api.domain.LoginUser;
import com.snail.sys.domain.SysDept;
import com.snail.sys.domain.SysOss;
import com.snail.sys.domain.SysRole;
import com.snail.sys.domain.SysUser;
import com.snail.sys.api.dto.RoleDTO;
import com.snail.sys.dao.SysUserDao;
import com.snail.sys.constants.SysConfigConstants;
import com.snail.sys.domain.SysUserPost;
import com.snail.sys.domain.SysUserRole;
import com.snail.sys.dto.SysUserPageDTO;
import com.snail.sys.dto.UserAddressUpdateDTO;
import com.snail.sys.dto.UserPasswordUpdateDTO;
import com.snail.sys.dto.UserProfileUpdateDTO;
import com.snail.sys.dto.excel.SysUserImportExcelDTO;
import com.snail.sys.service.*;
import com.snail.sys.vo.SysUserVo;
import com.snail.sys.vo.UserProfileVo;
import com.snail.sys.vo.UserVO;
import com.snail.sys.vo.excel.SysUserExportExcelVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


/**
 * 用户
 *
 * @author makejava
 * @since 2025-05-30 23:07:00
 */
@Slf4j
@RequiredArgsConstructor
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {
    private static final String[][] AVATAR_COLOR_PALETTE = {
            {"#2563EB", "#FFFFFF"},
            {"#0F766E", "#FFFFFF"},
            {"#16A34A", "#FFFFFF"},
            {"#D97706", "#FFFFFF"},
            {"#DC2626", "#FFFFFF"},
            {"#7C3AED", "#FFFFFF"},
            {"#BE185D", "#FFFFFF"},
            {"#0891B2", "#FFFFFF"},
            {"#E2E8F0", "#1E293B"}
    };


    private final SysUserRoleService sysUserRoleService;
    private final SysUserPostService sysUserPostService;
    private final SysPermissionService sysPermissionService;
    private final SysDeptService sysDeptService;
    private final SysConfigService sysConfigService;
    private final SysOssService sysOssService;
    private final ExcelExportService excelExportService;
    private final ExcelImportService excelImportService;
    private final DictService dictService;
    private final SysUserAddressService sysUserAddressService;

    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysUser> queryByPage(SysUserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysUser> result = this.page(page, buildUserQueryWrapper(dto));
        result.getRecords().forEach(user -> ensureAvatarIfAbsent(user, true));
        return result;
    }

    @Override
    public SysUser getById(Serializable id) {
        SysUser user = super.getById(id);
        return ensureAvatarIfAbsent(user, true);
    }

    /**
     * 根据用户账号获取用户信息
     *
     * @param userCode 用户账号
     * @return 用户信息
     */
    @Override
    public LoginUser getUserInfo(String userCode) {
        // 使用 MPJ 关联查询用户、部门、角色信息
//        SysUser sysUserInfo = this.lambdaQuery().eq(SysUser::getUserCode, userCode).one();
        SysUser sysUserInfo = selectUserByUserName(userCode);
        if (ObjectUtil.isEmpty(sysUserInfo)) {
            throw new UserException("user.not.exists", userCode);
        }
        if (UserConstants.USER_DISABLE.equals(sysUserInfo.getStatus())) {
            throw new UserException("user.blocked", userCode);
        }
//        LoginUser loginUser = BeanUtil.copyProperties(sysUserInfo, LoginUser.class);

        log.info("userInfo:{}", sysUserInfo);
        return buildLoginUser(sysUserInfo);
    }

    private SysUser selectUserByUserName(String userCode) {
        MPJLambdaWrapper<SysUser> wrapper = new MPJLambdaWrapper<SysUser>()
//                .distinct()
                .selectAll(SysUser.class) // 查询用户表所有字段
                // 1. 映射一对一：部门 (SysUser.dept)
                .selectAssociation(SysDept.class, SysUser::getDept)
                // 2. 映射一对多：角色 (SysUser.roles)
                .selectCollection(SysRole.class, SysUser::getRoles)
                // 连表逻辑保持不变
                .leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysUser::getUserCode, userCode);


        // 一个用户可能有多个角色，MPJ 会自动将多条记录折叠到 SysUser.roles 集合中
        List<SysUser> list = baseMapper.selectJoinList(SysUser.class, wrapper);

//        List<SysUserRole> sysUserRoles = sysUserRoleService.queryRoleListByUserId(user.getId());
//        SysDept sysDept = sysDeptService.queryDeptByDeptId(user.getDeptId());
        return CollUtil.isNotEmpty(list) ? ensureAvatarIfAbsent(list.get(0), true) : null;
    }

    /**
     * 构建登录用户信息（包含权限）- 权限从数据库动态获取，此方法保留用于兼容
     *
     * @param userCode 用户账号
     * @return 登录用户信息
     */
    @Override
    public LoginUser buildLoginUser(SysUser user) {
        // 权限信息现在通过 SysSaPermissionImpl 从数据库动态获取
        LoginUser loginUser = new LoginUser();
        loginUser.setId(user.getId());
        loginUser.setDeptId(user.getDeptId());
        loginUser.setUserCode(user.getUserCode());
        loginUser.setUserName(user.getUserName());
        loginUser.setPassWord(user.getPassWord());
        loginUser.setUserType(user.getUserType());
        loginUser.setMenuPermission(sysPermissionService.getMenuPermission(user));
        loginUser.setRolePermission(sysPermissionService.getRolePermission(user));
        loginUser.setDeptName(ObjectUtil.isNull(user.getDept()) ? StrUtil.EMPTY : user.getDept().getDeptName());
        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
        return loginUser;
    }

    /**
     * 注册用户信息
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean registerUserInfo(SysUser sysUser) {
        boolean exists = this.lambdaQuery()
                .eq(SysUser::getUserCode, sysUser.getUserCode())
                .ne(ObjectUtil.isNotNull(sysUser.getId()), SysUser::getId, sysUser.getId())
                .exists();
        if (exists) {
            throw new UserException("user.register.save.error", sysUser.getUserCode());
        }
        fillAvatarForPersist(sysUser, null);
        return this.save(sysUser);
    }

    /**
     * checkUserDataScope
     *
     * @param userId userId
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkUserDataScope(Long userId) {
        boolean flag = sysUserRoleService.checkAdminRole(userId);
        if (!flag) {
            this.lambdaQuery()
                    .eq(SysUser::getId, userId)
                    .oneOpt()
                    .orElseThrow(() -> new ServiceException("没有权限访问用户数据！"));
        }
    }

    @Override
    public SysUserVo getInfo(Long userId) {
        this.checkUserDataScope(userId);
        SysUser user = this.getById(userId);
        if (ObjectUtil.isEmpty(user)) {
            throw new ServiceException("用户不存在");
        }

        UserVO userVO = BeanUtil.copyProperties(user, UserVO.class);
        Long[] roleIds = sysUserRoleService.queryRoleListByUserId(userId)
                .stream()
                .map(SysUserRole::getRoleId)
                .toArray(Long[]::new);
        Long[] postIds = sysUserPostService.lambdaQuery()
                .eq(SysUserPost::getUserId, userId)
                .list()
                .stream()
                .map(SysUserPost::getPostId)
                .toArray(Long[]::new);

        userVO.setRoleIds(roleIds);
        userVO.setPostIds(postIds);

        return SysUserVo.builder()
                .user(userVO)
                .build();
    }

    @Override
    public UserProfileVo getProfile(Long userId) {
        SysUser user = selectUserDetailById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException("用户不存在");
        }

        UserProfileVo profile = BeanUtil.copyProperties(user, UserProfileVo.class);
        profile.setDeptName(ObjectUtil.isNotNull(user.getDept()) ? user.getDept().getDeptName() : StrUtil.EMPTY);
        profile.setRoleNames(CollUtil.isNotEmpty(user.getRoles())
                ? user.getRoles().stream().map(SysRole::getRoleName).filter(StrUtil::isNotBlank).collect(Collectors.toList())
                : new ArrayList<>());
        // 查询默认地址
        profile.setUserAddress(sysUserAddressService.getDefaultAddress(userId));
        return profile;
    }

    /**
     * insertUser
     *
     * @param user user
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertUser(SysUser user) {
        // 新增用户信息
        user.setPassWord(BCrypt.hashpw(user.getPassWord()));
        fillAvatarForPersist(user, null);
        boolean save = this.save(user);
        // 新增用户岗位关联
        sysUserPostService.insertUserPost(user);
        // 新增用户与角色管理
        sysUserRoleService.insertUserRole(user);
        return save;
    }
    /**
     * checkUserCodeUnique
     *
     * @param sysUser sysUser
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkUserCodeUnique(SysUser sysUser) {
        return this.lambdaQuery().eq(SysUser::getUserCode, sysUser.getUserCode())
                .ne(ObjectUtil.isNotNull(sysUser.getId()), SysUser::getId, sysUser.getId())
                .exists();
    }

    /**
     * edit
     *
     * @param user user
     * @return com.snail.common.core.utils.R<java.lang.Boolean>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public R<Boolean> edit(SysUser user) {
        this.checkUserAllowed(user);
        this.checkUserDataScope(user.getId());

        if (checkUserCodeUnique(user)) {
            return R.fail("修改用户:" + user.getUserCode() + "失败，登录账号已存在");
        }
        SysUser dbUser = super.getById(user.getId());
        fillAvatarForPersist(user, ObjectUtil.isNull(dbUser) ? null : dbUser.getAvatar());
        return R.ok(this.updateById(user));
    }

    /**
     * 校验用户是否允许操作
     *
     * @param user user
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public void checkUserAllowed(SysUser user) {
        if (StrUtil.isNotBlank(user.getUserCode()) && user.isAdmin()) {
            throw new ServiceException("不允许操作超级管理员用户");
        }
    }



    /**
     * 查询已分配用户角色列表
     *
     * @param dto dto
     * @return Page<SysUser>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public Page<SysUser> selectAllocatedList(SysUserPageDTO dto) {
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());

        MPJLambdaWrapper<SysUser> wrapper = getUserEq()
                .eq(ObjectUtil.isNotNull(dto.getRoleId()), SysRole::getId, dto.getRoleId())
                .like(StrUtil.isNotBlank(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .eq(ObjectUtil.isNotNull(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                .like(StrUtil.isNotBlank(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo());

        return baseMapper.selectJoinPage(page, SysUser.class, wrapper);
    }


    /**
     * 查询未分配用户角色列表
     *
     * @param dto dto
     * @return Page<SysUser>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public Page<SysUser> selectUnallocatedList(SysUserPageDTO dto) {
        List<Long> userIds = sysUserRoleService.selectUserIdsByRoleId(dto.getRoleId());
        Page<SysUser> page = new Page<>(dto.getCurrent(), dto.getSize());
        MPJLambdaWrapper<SysUser> wrapper = getUserEq()
                .and(w -> w.ne(SysRole::getId, dto.getRoleId()).or().isNull(SysRole::getId))
                .notIn(CollUtil.isNotEmpty(userIds), SysUser::getId, userIds)
                .like(StrUtil.isNotBlank(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .like(StrUtil.isNotBlank(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo());
        return baseMapper.selectJoinPage(page, SysUser.class, wrapper);

    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkPhoneExists(SysUser user) {
        return this.lambdaQuery().eq(SysUser::getPhoneNo, user.getPhoneNo())
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId())
                .exists();
    }

    /**
     * 校验邮箱是否唯一
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    public boolean checkEmailExists(SysUser user) {
        return this.lambdaQuery().eq(SysUser::getEmail,user.getEmail())
                .ne(ObjectUtil.isNotNull(user.getId()), SysUser::getId, user.getId())
                .exists();
    }

    /**
     * updateUser
     *
     * @param user user
     * @return boolean
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(SysUser user) {
        Long userId = user.getId();
        SysUser dbUser = super.getById(userId);
        fillAvatarForPersist(user, ObjectUtil.isNull(dbUser) ? null : dbUser.getAvatar());

        // 删除用户与岗位关联
        sysUserPostService.deleteUserPost(userId);
        // 新增用户岗位关联
        sysUserPostService.insertUserPost(user);
        // 删除用户与角色关联
        sysUserRoleService.deleteUserRole(userId);
        // 新增用户与角色管理
        sysUserRoleService.insertUserRole(user);

        return this.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfile(Long userId, UserProfileUpdateDTO dto) {
        SysUser dbUser = super.getById(userId);
        if (ObjectUtil.isNull(dbUser)) {
            throw new ServiceException("用户不存在");
        }

        SysUser updateUser = new SysUser();
        updateUser.setId(userId);
        updateUser.setNickName(StrUtil.nullToEmpty(StrUtil.trim(dto.getNickName())));
        updateUser.setEmail(StrUtil.nullToEmpty(StrUtil.trim(dto.getEmail())));
        updateUser.setPhoneNo(StrUtil.nullToEmpty(StrUtil.trim(dto.getPhoneNo())));
        updateUser.setSex(StrUtil.blankToDefault(StrUtil.trim(dto.getSex()), dbUser.getSex()));
        updateUser.setAvatar(StrUtil.trim(dto.getAvatar()));

        if (StrUtil.isNotBlank(updateUser.getPhoneNo()) && checkPhoneExists(updateUser)) {
            throw new ServiceException("修改个人信息失败，手机号码已存在");
        }

        if (StrUtil.isNotBlank(updateUser.getEmail()) && checkEmailExists(updateUser)) {
            throw new ServiceException("修改个人信息失败，邮箱账号已存在");
        }

        fillAvatarForPersist(updateUser, dbUser.getAvatar());
        boolean userResult = this.updateById(updateUser);

        // 更新地址信息（如果填写了收货人则保存地址）
        if (StrUtil.isNotBlank(dto.getReceiverName()) || StrUtil.isNotBlank(dto.getDetailAddress())) {
            UserAddressUpdateDTO addressDTO = new UserAddressUpdateDTO();
            addressDTO.setId(dto.getAddressId());
            addressDTO.setReceiverName(dto.getReceiverName());
            addressDTO.setReceiverPhone(dto.getReceiverPhone());
            addressDTO.setProvinceId(dto.getProvinceId());
            addressDTO.setProvinceCode(dto.getProvinceCode());
            addressDTO.setProvinceName(dto.getProvinceName());
            addressDTO.setCityId(dto.getCityId());
            addressDTO.setCityCode(dto.getCityCode());
            addressDTO.setCityName(dto.getCityName());
            addressDTO.setDistrictId(dto.getDistrictId());
            addressDTO.setDistrictCode(dto.getDistrictCode());
            addressDTO.setDistrictName(dto.getDistrictName());
            addressDTO.setStreetId(dto.getStreetId());
            addressDTO.setStreetCode(dto.getStreetCode());
            addressDTO.setStreetName(dto.getStreetName());
            addressDTO.setDetailAddress(dto.getDetailAddress());
            addressDTO.setAddressTag(dto.getAddressTag());
            addressDTO.setLongitude(dto.getLongitude());
            addressDTO.setLatitude(dto.getLatitude());
            addressDTO.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 1);
            sysUserAddressService.saveOrUpdateAddress(userId, addressDTO);
        }

        return userResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateProfilePassword(Long userId, UserPasswordUpdateDTO dto) {
        SysUser dbUser = super.getById(userId);
        if (ObjectUtil.isNull(dbUser)) {
            throw new ServiceException("用户不存在");
        }

        if (!BCrypt.checkpw(dto.getOldPassword(), dbUser.getPassWord())) {
            throw new ServiceException("原密码不正确");
        }

        if (!StrUtil.equals(dto.getNewPassword(), dto.getConfirmPassword())) {
            throw new ServiceException("两次输入的新密码不一致");
        }

        if (StrUtil.equals(dto.getOldPassword(), dto.getNewPassword())) {
            throw new ServiceException("新密码不能与原密码相同");
        }

        SysUser updateUser = new SysUser();
        updateUser.setId(userId);
        updateUser.setPassWord(BCrypt.hashpw(dto.getNewPassword()));
        boolean updated = this.updateById(updateUser);
        if (updated) {
            logoutUserSessions(dbUser);
        }
        return updated;
    }

    private void logoutUserSessions(SysUser user) {
        if (ObjectUtil.isNull(user) || ObjectUtil.isNull(user.getId())) {
            return;
        }

        List<String> keys = StpUtil.searchTokenValue("", 0, -1, false);
        if (CollUtil.isEmpty(keys)) {
            return;
        }

        keys.forEach(key -> {
            String token = StrUtil.subAfter(key, StrUtil.COLON, true);
            if (StrUtil.isBlank(token)) {
                return;
            }

            LoginUser loginUser = LoginUtils.getLoginUser(token);
            if (loginUser == null || !ObjectUtil.equal(loginUser.getId(), user.getId())) {
                return;
            }

            logoutTokenQuietly(token);
        });
    }

    private void logoutTokenQuietly(String token) {
        try {
            StpUtil.logoutByTokenValue(token);
        } catch (NotLoginException e) {
            log.debug("密码修改清理登录态时，token 已失效，跳过登出: {}", token);
        } finally {
            RedisUtils.deleteObject(CacheConstants.ONLINE_TOKEN_KEY + token);
        }
    }

    @Override
    public void exportUsers(SysUserPageDTO dto, HttpServletResponse response) {
        SysUserPageDTO exportDto = BeanUtil.copyProperties(dto, SysUserPageDTO.class);
        long totalCount = this.count(buildUserQueryWrapper(exportDto));
        ExcelExportRequest<SysUserExportExcelVO> request = ExcelExportRequest.<SysUserExportExcelVO>builder()
                .fileName("用户数据")
                .sheetName("用户列表")
                .head(SysUserExportExcelVO.class)
                .totalCount(totalCount)
                .pageSize(1000)
                .maxRowsPerSheet(50000)
                .maxSheetsPerWorkbook(10)
                .pageFetcher((current, size) -> queryUserExportPage(exportDto, current, size))
                .build();
        excelExportService.export(request, response);
    }

    @Override
    public void downloadImportTemplate(HttpServletResponse response) {
        ExcelExportRequest<SysUserImportExcelDTO> request = ExcelExportRequest.<SysUserImportExcelDTO>builder()
                .fileName("用户导入模板")
                .sheetName("用户导入")
                .head(SysUserImportExcelDTO.class)
                .totalCount(0L)
                .dropDownEnabled(Boolean.TRUE)
                .build();
        excelExportService.export(request, response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ExcelImportResult importUsers(MultipartFile file, boolean updateSupport) {
        if (ObjectUtil.isNull(file) || file.isEmpty()) {
            throw new ServiceException("导入文件不能为空");
        }
        String initPassword = StrUtil.blankToDefault(
                sysConfigService.selectConfigByKey(SysConfigConstants.SYS_USER_INIT_PASSWORD),
                SysConfigConstants.SYS_USER_INIT_PASSWORD_DEFAULT
        );
        try {
            ExcelImportRequest<SysUserImportExcelDTO> request = ExcelImportRequest.<SysUserImportExcelDTO>builder()
                    .head(SysUserImportExcelDTO.class)
                    .validate(Boolean.TRUE)
                    .batchSize(200)
                    .batchConsumer(rows -> importUserRows(rows, updateSupport, initPassword))
                    .build();
            return excelImportService.importExcel(file.getInputStream(), request);
        } catch (IOException e) {
            throw new ServiceException("导入用户失败");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String resetUserPassword(Long userId) {
        SysUser user = super.getById(userId);
        if (ObjectUtil.isNull(user)) {
            throw new ServiceException("用户不存在");
        }

        checkUserAllowed(user);
        checkUserDataScope(userId);

        String initPassword = StrUtil.blankToDefault(
                sysConfigService.selectConfigByKey(SysConfigConstants.SYS_USER_INIT_PASSWORD),
                SysConfigConstants.SYS_USER_INIT_PASSWORD_DEFAULT
        );

        boolean updated = this.lambdaUpdate()
                .eq(SysUser::getId, userId)
                .set(SysUser::getPassWord, BCrypt.hashpw(initPassword))
                .update();
        if (!updated) {
            throw new ServiceException("重置密码失败");
        }
        return initPassword;
    }

    /**
     * getUserEq
     *
     * @return com.github.yulichang.wrapper.MPJLambdaWrapper<com.snail.sys.domain.SysUser>
     * @since 1.0
     * <p>1.0 Initialization method </p>
     */
    private static MPJLambdaWrapper<SysUser> getUserEq() {
        return new MPJLambdaWrapper<SysUser>()
                .select(SysUser::getId,
                        SysUser::getDeptId,
                        SysUser::getUserName,
                        SysUser::getNickName,
                        SysUser::getEmail,
                        SysUser::getPhoneNo,
                        SysUser::getStatus,
                        SysUser::getCreateTime)
                .leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)

                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL);
    }

    private LambdaQueryWrapper<SysUser> buildUserQueryWrapper(SysUserPageDTO dto) {
        return new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(StrUtil.isNotEmpty(dto.getUserCode()), SysUser::getUserCode, dto.getUserCode())
                .like(StrUtil.isNotEmpty(dto.getUserName()), SysUser::getUserName, dto.getUserName())
                .eq(ObjectUtil.isNotEmpty(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                .like(StrUtil.isNotEmpty(dto.getPhoneNo()), SysUser::getPhoneNo, dto.getPhoneNo())
                .between(StrUtil.isNotEmpty(dto.getBeginTime()) && StrUtil.isNotEmpty(dto.getEndTime()),
                        SysUser::getCreateTime, dto.getBeginTime(), dto.getEndTime());
    }

    private List<SysUserExportExcelVO> queryUserExportPage(SysUserPageDTO dto, long current, long size) {
        Page<SysUser> page = new Page<>(current, size);
        Page<SysUser> result = this.page(page, buildUserQueryWrapper(dto).orderByAsc(SysUser::getId));
        return result.getRecords().stream().map(this::toUserExportExcelVO).collect(Collectors.toList());
    }

    private SysUserExportExcelVO toUserExportExcelVO(SysUser user) {
        SysUserExportExcelVO vo = BeanUtil.copyProperties(user, SysUserExportExcelVO.class);
        vo.setSex(dictService.getDictLabel("sys_user_sex", StrUtil.blankToDefault(user.getSex(), "2")));
        vo.setStatus(dictService.getDictLabel("sys_normal_disable", String.valueOf(ObjectUtil.defaultIfNull(user.getStatus(), UserConstants.USER_NORMAL))));
        vo.setCreateTime(ObjectUtil.isNull(user.getCreateTime()) ? StrUtil.EMPTY : DateUtil.formatDateTime(user.getCreateTime()));
        return vo;
    }

    private List<ExcelImportRowResult> importUserRows(List<ExcelRow<SysUserImportExcelDTO>> rows,
                                                      boolean updateSupport,
                                                      String initPassword) {
        List<ExcelImportRowResult> results = new ArrayList<>();
        for (ExcelRow<SysUserImportExcelDTO> row : rows) {
            try {
                importSingleUser(row.getData(), updateSupport, initPassword);
                results.add(ExcelImportRowResult.success(row.getRowNum()));
            } catch (Exception e) {
                results.add(ExcelImportRowResult.fail(row.getRowNum(), StrUtil.blankToDefault(e.getMessage(), "导入失败")));
            }
        }
        return results;
    }

    private void importSingleUser(SysUserImportExcelDTO dto, boolean updateSupport, String initPassword) {
        String userCode = StrUtil.trim(dto.getUserCode());
        SysUser existUser = this.lambdaQuery()
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysUser::getUserCode, userCode)
                .one();
        if (ObjectUtil.isNotNull(existUser)) {
            if (!updateSupport) {
                throw new ServiceException("用户账号已存在");
            }
            checkUserAllowed(existUser);
            updateImportedUser(existUser, dto);
            return;
        }
        createImportedUser(dto, initPassword);
    }

    private void createImportedUser(SysUserImportExcelDTO dto, String initPassword) {
        SysUser user = buildImportUser(dto);
        user.setUserType("sys_user");
        user.setDeleted(UserConstants.USER_NORMAL);
        user.setPassWord(BCrypt.hashpw(initPassword));

        if (checkUserCodeUnique(user)) {
            throw new ServiceException("用户账号已存在");
        }
        if (StrUtil.isNotBlank(user.getPhoneNo()) && checkPhoneExists(user)) {
            throw new ServiceException("手机号码已存在");
        }
        if (StrUtil.isNotBlank(user.getEmail()) && checkEmailExists(user)) {
            throw new ServiceException("邮箱账号已存在");
        }
        fillAvatarForPersist(user, null);
        this.save(user);
    }

    private void updateImportedUser(SysUser existUser, SysUserImportExcelDTO dto) {
        SysUser updateUser = buildImportUser(dto);
        updateUser.setId(existUser.getId());

        if (StrUtil.isNotBlank(updateUser.getPhoneNo()) && checkPhoneExists(updateUser)) {
            throw new ServiceException("手机号码已存在");
        }
        if (StrUtil.isNotBlank(updateUser.getEmail()) && checkEmailExists(updateUser)) {
            throw new ServiceException("邮箱账号已存在");
        }
        fillAvatarForPersist(updateUser, existUser.getAvatar());
        this.updateById(updateUser);
    }

    private SysUser buildImportUser(SysUserImportExcelDTO dto) {
        SysUser user = new SysUser();
        String userName = StrUtil.trim(dto.getUserName());
        user.setUserCode(StrUtil.trim(dto.getUserCode()));
        user.setUserName(userName);
        user.setNickName(StrUtil.blankToDefault(StrUtil.trim(dto.getNickName()), userName));
        user.setPhoneNo(StrUtil.nullToEmpty(StrUtil.trim(dto.getPhoneNo())));
        user.setEmail(StrUtil.nullToEmpty(StrUtil.trim(dto.getEmail())));
        user.setSex(StrUtil.blankToDefault(StrUtil.trim(dto.getSex()), "2"));
        user.setStatus(ObjectUtil.defaultIfNull(dto.getStatus(), UserConstants.USER_NORMAL));
        user.setRemark(StrUtil.nullToEmpty(StrUtil.trim(dto.getRemark())));
        return user;
    }

    private SysUser ensureAvatarIfAbsent(SysUser user, boolean persist) {
        if (ObjectUtil.isEmpty(user) || StrUtil.isNotBlank(user.getAvatar())) {
            return user;
        }

        String avatar = generateAvatarUrl(user.getUserCode(), user.getUserName());
        user.setAvatar(avatar);

        if (persist && ObjectUtil.isNotNull(user.getId())) {
            this.lambdaUpdate()
                    .eq(SysUser::getId, user.getId())
                    .set(SysUser::getAvatar, avatar)
                    .update();
        }

        return user;
    }

    private SysUser selectUserDetailById(Long userId) {
        MPJLambdaWrapper<SysUser> wrapper = new MPJLambdaWrapper<SysUser>()
                .selectAll(SysUser.class)
                .selectAssociation(SysDept.class, SysUser::getDept)
                .selectCollection(SysRole.class, SysUser::getRoles)
                .leftJoin(SysDept.class, SysDept::getId, SysUser::getDeptId)
                .leftJoin(SysUserRole.class, SysUserRole::getUserId, SysUser::getId)
                .leftJoin(SysRole.class, SysRole::getId, SysUserRole::getRoleId)
                .eq(SysUser::getDeleted, UserConstants.USER_NORMAL)
                .eq(SysUser::getId, userId);

        List<SysUser> list = baseMapper.selectJoinList(SysUser.class, wrapper);
        return CollUtil.isNotEmpty(list) ? ensureAvatarIfAbsent(list.get(0), true) : null;
    }

    private void fillAvatarForPersist(SysUser user, String currentAvatar) {
        if (StrUtil.isNotBlank(user.getAvatar())) {
            return;
        }

        if (StrUtil.isNotBlank(currentAvatar)) {
            user.setAvatar(currentAvatar);
            return;
        }

        user.setAvatar(generateAvatarUrl(user.getUserCode(), user.getUserName()));
    }

    private String generateAvatarUrl(String userCode, String userName) {
        String safeCode = StrUtil.blankToDefault(StrUtil.trim(userCode), "avatar");
        String svg = buildAvatarSvg(userName);
        try {
            SysOss sysOss = sysOssService.uploadContent(
                    svg.getBytes(StandardCharsets.UTF_8),
                    safeCode + ".svg",
                    "image/svg+xml",
                    null
            );
            return sysOss.getUrl();
        } catch (Exception e) {
            log.warn("生成用户头像并上传失败，userCode={}, userName={}", userCode, userName, e);
            return StrUtil.EMPTY;
        }
    }

    private String buildAvatarSvg(String userName) {
        String avatarText = extractAvatarText(userName);
        int hash = Math.abs(StrUtil.emptyToDefault(userName, "USER").hashCode());
        String[] palette = AVATAR_COLOR_PALETTE[hash % AVATAR_COLOR_PALETTE.length];
        String backgroundColor = palette[0];
        String textColor = palette[1];
        int fontSize = avatarText.length() <= 1 ? 72 : avatarText.length() == 2 ? 56 : 44;

        String safeText = escapeSvgText(avatarText);
        String svg =
                "<svg xmlns='http://www.w3.org/2000/svg' width='160' height='160' viewBox='0 0 160 160'>"
                        + "<rect width='160' height='160' rx='20' fill='" + backgroundColor + "'/>"
                        + "<text x='50%' y='50%' text-anchor='middle' dominant-baseline='central' "
                        + "font-family='PingFang SC, Microsoft YaHei, Helvetica Neue, Arial, sans-serif' "
                        + "font-size='" + fontSize + "' font-weight='700' fill='" + textColor + "'>"
                        + safeText
                        + "</text></svg>";
        return svg;
    }

    private String extractAvatarText(String userName) {
        String source = StrUtil.blankToDefault(StrUtil.trim(userName), "User");

        String chinese = source.replaceAll("[^\\u4e00-\\u9fa5]", "");
        if (StrUtil.isNotBlank(chinese)) {
            if (chinese.length() <= 2) {
                return chinese;
            }
            return chinese.substring(chinese.length() - 2);
        }

        String[] words = source.split("[^A-Za-z0-9]+");
        StringBuilder initials = new StringBuilder();
        for (String word : words) {
            if (StrUtil.isBlank(word)) {
                continue;
            }
            initials.append(word.substring(0, 1).toUpperCase(Locale.ROOT));
            if (initials.length() >= 3) {
                break;
            }
        }
        if (initials.length() >= 2) {
            return initials.toString();
        }

        String normalized = source.replaceAll("[^A-Za-z0-9]", "").toUpperCase(Locale.ROOT);
        if (StrUtil.isBlank(normalized)) {
            return "U";
        }
        return normalized.substring(0, Math.min(normalized.length(), 3));
    }

    private String escapeSvgText(String text) {
        return text
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
