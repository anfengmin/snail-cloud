package com.snail.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.snail.sys.domain.SysUserAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户地址簿(SysUserAddress)表数据库访问层
 *
 * @author Levi
 * @since 2026-05-04
 */
@Mapper
public interface SysUserAddressDao extends BaseMapper<SysUserAddress> {
}