package com.snail.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.snail.sys.domain.SysOss;
import com.snail.sys.dto.SysOssPageDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:29
 */
public interface SysOssService extends IService<SysOss> {

    /**
     * 分页查询
     *
     * @param dto dto
     * @return 分页结果
     * @since 1.0
     */
    Page<SysOss> queryByPage(SysOssPageDTO dto);

    /**
     * 上传文件
     *
     * @param file      文件
     * @param configKey 配置键
     * @return 上传记录
     */
    SysOss upload(MultipartFile file, String configKey);

    /**
     * 上传字节内容并落库
     *
     * @param content 文件字节
     * @param originalFilename 原始文件名
     * @param contentType 内容类型
     * @param configKey 配置键
     * @return 上传记录
     */
    SysOss uploadContent(byte[] content, String originalFilename, String contentType, String configKey);

    /**
     * 在线预览文件
     *
     * @param id 文件主键
     * @param response 响应
     */
    void preview(Long id, HttpServletResponse response);

}
