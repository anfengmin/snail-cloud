package com.snail.sys.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.core.exception.ServiceException;
import com.snail.sys.dao.SysUserAddressDao;
import com.snail.sys.domain.SysUserAddress;
import com.snail.sys.dto.UserAddressUpdateDTO;
import com.snail.sys.service.SysUserAddressService;
import com.snail.sys.vo.UserAddressVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户地址服务实现
 *
 * @author Levi
 * @since 2026-05-04
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SysUserAddressServiceImpl extends ServiceImpl<SysUserAddressDao, SysUserAddress>
        implements SysUserAddressService {

    /**
     * 获取用户的默认地址
     */
    @Override
    public UserAddressVo getDefaultAddress(Long userId) {
        if (ObjectUtil.isNull(userId)) {
            return null;
        }
        SysUserAddress address = this.lambdaQuery()
                .eq(SysUserAddress::getUserId, userId)
                .eq(SysUserAddress::getIsDefault, 1)
                .eq(SysUserAddress::getStatus, 0)
                .one();
        if (ObjectUtil.isNull(address)) {
            return null;
        }
        return BeanUtil.copyProperties(address, UserAddressVo.class);
    }

    /**
     * 保存或更新用户地址
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateAddress(Long userId, UserAddressUpdateDTO dto) {
        // 如果设为默认，先取消其他默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            this.lambdaUpdate()
                    .eq(SysUserAddress::getUserId, userId)
                    .eq(SysUserAddress::getIsDefault, 1)
                    .set(SysUserAddress::getIsDefault, 0)
                    .update();
        }

        SysUserAddress address = new SysUserAddress();
        address.setUserId(userId);
        address.setReceiverName(dto.getReceiverName());
        address.setReceiverPhone(dto.getReceiverPhone());
        address.setProvinceId(dto.getProvinceId());
        address.setProvinceCode(dto.getProvinceCode());
        address.setProvinceName(dto.getProvinceName());
        address.setCityId(dto.getCityId());
        address.setCityCode(dto.getCityCode());
        address.setCityName(dto.getCityName());
        address.setDistrictId(dto.getDistrictId());
        address.setDistrictCode(dto.getDistrictCode());
        address.setDistrictName(dto.getDistrictName());
        address.setStreetId(dto.getStreetId());
        address.setStreetCode(dto.getStreetCode());
        address.setStreetName(dto.getStreetName());
        address.setDetailAddress(dto.getDetailAddress());
        address.setAddressTag(dto.getAddressTag());
        address.setLongitude(dto.getLongitude());
        address.setLatitude(dto.getLatitude());
        address.setIsDefault(dto.getIsDefault() == null ? 0 : dto.getIsDefault());
        address.setStatus(0);

        // 拼接完整地址
        String fullAddress = StrUtil.join("",
                StrUtil.nullToEmpty(dto.getProvinceName()),
                StrUtil.nullToEmpty(dto.getCityName()),
                StrUtil.nullToEmpty(dto.getDistrictName()),
                StrUtil.nullToEmpty(dto.getStreetName()),
                StrUtil.nullToEmpty(dto.getDetailAddress()));
        address.setFullAddress(fullAddress);

        // 更新或新增
        if (dto.getId() != null) {
            address.setId(dto.getId());
            return this.updateById(address);
        } else {
            // 已有默认地址则更新，否则新增
            SysUserAddress existAddress = this.lambdaQuery()
                    .eq(SysUserAddress::getUserId, userId)
                    .eq(SysUserAddress::getIsDefault, 1)
                    .one();
            if (existAddress != null) {
                address.setId(existAddress.getId());
                return this.updateById(address);
            } else {
                return this.save(address);
            }
        }
    }
}
