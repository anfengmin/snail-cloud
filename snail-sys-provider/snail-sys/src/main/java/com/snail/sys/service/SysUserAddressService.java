package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysUserAddress;
import com.snail.sys.dto.UserAddressUpdateDTO;
import com.snail.sys.vo.UserAddressVo;

/**
 * 用户地址服务接口
 *
 * @author Levi
 * @since 2026-05-04
 */
public interface SysUserAddressService extends IService<SysUserAddress> {

    /**
     * 获取用户的默认地址
     *
     * @param userId 用户ID
     * @return 默认地址
     */
    UserAddressVo getDefaultAddress(Long userId);

    /**
     * 保存或更新用户地址
     *
     * @param userId 用户ID
     * @param dto  地址参数
     * @return 是否成功
     */
    boolean saveOrUpdateAddress(Long userId, UserAddressUpdateDTO dto);
}