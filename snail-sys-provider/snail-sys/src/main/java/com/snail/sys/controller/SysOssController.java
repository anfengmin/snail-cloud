package com.snail.sys.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.snail.common.core.enums.BusinessType;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.snail.common.log.annotation.Log;
import com.snail.sys.domain.SysOss;
import com.snail.sys.service.SysOssService;
import com.snail.sys.dto.SysOssPageDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.snail.common.core.utils.R;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:27
 */
@Tag(name = "OSS对象存储")
@RestController
@RequestMapping("/sysOss")
public class SysOssController {

    @Resource
    private SysOssService sysOssService;

    @SaCheckPermission("system:oss:list")
    @PostMapping("queryByPage")
    @Operation(summary = "分页查询", description = "分页查询")
    public R<Page<SysOss>> queryByPage(@RequestBody SysOssPageDTO dto) {
        return R.ok(sysOssService.queryByPage(dto));
    }

    @SaCheckPermission("system:oss:query")
    @GetMapping("{id}")
    @Operation(summary = "主键查询")
    public R<SysOss> queryById(@PathVariable("id") Long id) {
        return R.ok(sysOssService.getById(id));
    }

    @SaCheckPermission("system:oss:query")
    @GetMapping("preview/{id}")
    @Operation(summary = "在线预览文件")
    public void preview(@PathVariable("id") Long id, HttpServletResponse response) {
        sysOssService.preview(id, response);
    }

    @SaCheckPermission("system:oss:upload")
    @Log(title = "文件管理", businessType = BusinessType.IMPORT)
    @PostMapping(value = "upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "上传文件")
    public R<SysOss> upload(@RequestParam("file") MultipartFile file,
                            @RequestParam(value = "configKey", required = false) String configKey) {
        return R.ok(sysOssService.upload(file, configKey));
    }

    @SaCheckPermission("system:oss:add")
    @Log(title = "文件管理", businessType = BusinessType.INSERT)
    @PostMapping("add")
    @Operation(summary = "新增数据")
    public R<Boolean> add(@RequestBody SysOss sysOss) {
        return R.ok(sysOssService.save(sysOss));
    }

    @SaCheckPermission("system:oss:edit")
    @Log(title = "文件管理", businessType = BusinessType.UPDATE)
    @PutMapping("edit")
    @Operation(summary = "编辑数据")
    public R<Boolean> edit(@RequestBody SysOss sysOss) {
        return R.ok(sysOssService.updateById(sysOss));
    }

    @SaCheckPermission("system:oss:remove")
    @Log(title = "文件管理", businessType = BusinessType.DELETE)
    @DeleteMapping("delete")
    @Operation(summary = "删除数据")
    public R<Boolean> deleteById(@RequestBody List<Long> ids) {
        return R.ok(sysOssService.removeByIds(ids));
    }

}
