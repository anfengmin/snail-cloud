package com.snail.sys.service.impl;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snail.common.storage.exception.StorageException;
import com.snail.common.storage.model.UploadResult;
import com.snail.common.storage.service.StorageManager;
import com.snail.sys.dao.SysOssDao;
import com.snail.sys.domain.SysOss;
import com.snail.sys.domain.SysOssConfig;
import com.snail.sys.dto.SysOssPageDTO;
import com.snail.sys.service.SysOssConfigService;
import com.snail.sys.service.SysOssService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.ContentDisposition;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.List;


/**
 * OSS对象存储
 *
 * @author makejava
 * @since 2025-05-30 23:04:33
 */
@Service("sysOssService")
@RequiredArgsConstructor
public class SysOssServiceImpl extends ServiceImpl<SysOssDao, SysOss> implements SysOssService {

    private final StorageManager storageManager;
    private final SysOssConfigService sysOssConfigService;

    private static final int EXCEL_PREVIEW_MAX_ROWS = 200;
    private static final List<String> EXCEL_SUFFIXES = Arrays.asList("xls", "xlsx", "csv");
    private static final List<String> TEXT_SUFFIXES = Arrays.asList("txt", "md", "json", "xml", "yml", "yaml", "log", "csv");


    /**
     * 分页查询
     *
     * @param dto 筛选条件
     * @return 查询结果
     */
    @Override
    public Page<SysOss> queryByPage(SysOssPageDTO dto) {
        Page<SysOss> page = new Page<>(dto.getCurrent(), dto.getSize());
        Page<SysOss> result = this.lambdaQuery()
                .like(StrUtil.isNotBlank(dto.getOriginalName()), SysOss::getOriginalName, dto.getOriginalName())
                .like(StrUtil.isNotBlank(dto.getFileSuffix()), SysOss::getFileSuffix, dto.getFileSuffix())
                .like(StrUtil.isNotBlank(dto.getService()), SysOss::getService, dto.getService())
                .orderByDesc(SysOss::getCreateTime)
                .page(page);
        return result;
    }

    @Override
    public SysOss upload(MultipartFile file, String configKey) {
        UploadResult uploadResult = storageManager.upload(configKey, file);
        return saveUploadResult(uploadResult);
    }

    @Override
    public SysOss uploadContent(byte[] content, String originalFilename, String contentType, String configKey) {
        UploadResult uploadResult = storageManager.upload(configKey, originalFilename, contentType, content);
        return saveUploadResult(uploadResult);
    }

    @Override
    public void preview(Long id, HttpServletResponse response) {
        SysOss sysOss = this.getById(id);
        if (sysOss == null) {
            throw new StorageException("文件不存在");
        }

        String suffix = resolveSuffix(sysOss);
        String filename = StrUtil.blankToDefault(sysOss.getOriginalName(), sysOss.getFileName());

        try (InputStream inputStream = openContentStream(sysOss)) {
            if ("pdf".equals(suffix)) {
                writePdfPreview(response, inputStream, filename);
                return;
            }

            if (EXCEL_SUFFIXES.contains(suffix)) {
                writeExcelPreview(response, inputStream, filename, suffix);
                return;
            }

            if (TEXT_SUFFIXES.contains(suffix)) {
                writeTextPreview(response, inputStream, filename);
                return;
            }

            writeUnsupportedPreview(response, sysOss);
        } catch (IOException e) {
            throw new StorageException("文件预览失败", e);
        }
    }

    private SysOss saveUploadResult(UploadResult uploadResult) {
        SysOss sysOss = new SysOss();
        sysOss.setFileName(uploadResult.getObjectKey());
        sysOss.setOriginalName(uploadResult.getOriginalFilename());
        sysOss.setFileSuffix(uploadResult.getFileSuffix());
        sysOss.setUrl(uploadResult.getUrl());
        sysOss.setService(uploadResult.getService());
        this.save(sysOss);
        return sysOss;
    }

    private InputStream openContentStream(SysOss sysOss) throws IOException {
        String configKey = resolveConfigKey(sysOss);
        if (StrUtil.isNotBlank(configKey)) {
            return storageManager.getContent(configKey, sysOss.getFileName());
        }

        if (StrUtil.isBlank(sysOss.getUrl())) {
            throw new StorageException("无法定位文件来源");
        }

        return new URL(sysOss.getUrl()).openStream();
    }

    private String resolveConfigKey(SysOss sysOss) {
        if (StrUtil.isBlank(sysOss.getFileName())) {
            return null;
        }

        String expectedUrl = normalizeUrl(sysOss.getUrl());
        List<SysOssConfig> configs = sysOssConfigService.list();
        for (SysOssConfig config : configs) {
            try {
                String candidateUrl = storageManager.getObjectUrl(config.getConfigKey(), sysOss.getFileName());
                if (StrUtil.equals(expectedUrl, normalizeUrl(candidateUrl))) {
                    return config.getConfigKey();
                }
            } catch (Exception ignored) {
                // ignore invalid config and continue matching next one
            }
        }

        if (configs.size() == 1) {
            return configs.get(0).getConfigKey();
        }

        return null;
    }

    private String normalizeUrl(String url) {
        if (StrUtil.isBlank(url)) {
            return "";
        }
        int queryIndex = url.indexOf('?');
        String normalized = queryIndex >= 0 ? url.substring(0, queryIndex) : url;
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private String resolveSuffix(SysOss sysOss) {
        String suffix = StrUtil.blankToDefault(sysOss.getFileSuffix(), StrUtil.subAfter(sysOss.getOriginalName(), '.', true));
        return StrUtil.blankToDefault(suffix, "").toLowerCase(Locale.ROOT);
    }

    private void writePdfPreview(HttpServletResponse response, InputStream inputStream, String filename) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.setHeader("Content-Disposition", ContentDisposition.inline().filename(filename, StandardCharsets.UTF_8).build().toString());
        IoUtil.copy(inputStream, response.getOutputStream());
    }

    private void writeExcelPreview(HttpServletResponse response, InputStream inputStream, String filename, String suffix) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8");
        response.getWriter().write(buildExcelPreviewHtml(inputStream, filename, suffix));
    }

    private void writeTextPreview(HttpServletResponse response, InputStream inputStream, String filename) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8");
        String text = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        String html = "<!DOCTYPE html>"
                + "<html lang=\"zh-CN\">"
                + "<head>"
                + "<meta charset=\"UTF-8\" />"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
                + "<title>" + HtmlUtil.escape(filename) + "</title>"
                + "<style>"
                + "body { margin: 0; padding: 24px; background: #f8fafc; color: #0f172a; font: 14px/1.6 -apple-system, BlinkMacSystemFont, \"Segoe UI\", sans-serif; }"
                + ".container { max-width: 1120px; margin: 0 auto; }"
                + ".title { margin: 0 0 16px; font-size: 18px; font-weight: 600; }"
                + "pre { margin: 0; padding: 16px; overflow: auto; white-space: pre-wrap; word-break: break-word; background: #fff; border: 1px solid #e2e8f0; border-radius: 12px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<h1 class=\"title\">" + HtmlUtil.escape(filename) + "</h1>"
                + "<pre>" + HtmlUtil.escape(text) + "</pre>"
                + "</div>"
                + "</body>"
                + "</html>";
        response.getWriter().write(html);
    }

    private void writeUnsupportedPreview(HttpServletResponse response, SysOss sysOss) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType(MediaType.TEXT_HTML_VALUE + ";charset=UTF-8");
        String filename = StrUtil.blankToDefault(sysOss.getOriginalName(), sysOss.getFileName());
        String html = "<!DOCTYPE html>"
                + "<html lang=\"zh-CN\">"
                + "<head>"
                + "<meta charset=\"UTF-8\" />"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
                + "<title>" + HtmlUtil.escape(filename) + "</title>"
                + "<style>"
                + "body { margin: 0; display: flex; min-height: 100vh; align-items: center; justify-content: center; background: #f8fafc; color: #334155; font: 14px/1.6 -apple-system, BlinkMacSystemFont, \"Segoe UI\", sans-serif; }"
                + ".card { width: min(520px, calc(100vw - 32px)); padding: 28px; background: #fff; border: 1px solid #e2e8f0; border-radius: 16px; box-shadow: 0 10px 30px rgb(15 23 42 / 8%); }"
                + "h1 { margin: 0 0 12px; font-size: 18px; color: #0f172a; }"
                + "p { margin: 0; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"card\">"
                + "<h1>" + HtmlUtil.escape(filename) + "</h1>"
                + "<p>当前格式暂不支持在线预览，请下载后查看。</p>"
                + "</div>"
                + "</body>"
                + "</html>";
        response.getWriter().write(html);
    }

    private String buildExcelPreviewHtml(InputStream inputStream, String filename, String suffix) throws IOException {
        if ("csv".equals(suffix)) {
            return buildCsvPreviewHtml(inputStream, filename);
        }

        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
            DataFormatter formatter = new DataFormatter();
            StringBuilder content = new StringBuilder();
            int sheetCount = workbook.getNumberOfSheets();

            for (int sheetIndex = 0; sheetIndex < sheetCount; sheetIndex++) {
                Sheet sheet = workbook.getSheetAt(sheetIndex);
                content.append("<section class=\"sheet\">");
                content.append("<h2>").append(HtmlUtil.escape(sheet.getSheetName())).append("</h2>");
                content.append("<div class=\"table-wrap\"><table><tbody>");

                int startRow = Math.max(sheet.getFirstRowNum(), 0);
                int endRow = Math.min(sheet.getLastRowNum(), startRow + EXCEL_PREVIEW_MAX_ROWS - 1);

                for (int rowIndex = startRow; rowIndex <= endRow; rowIndex++) {
                    Row row = sheet.getRow(rowIndex);
                    content.append("<tr>");

                    short lastCellNum = row != null ? row.getLastCellNum() : 0;
                    int cellCount = Math.max(lastCellNum, (short) 1);

                    for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                        Cell cell = row == null ? null : row.getCell(cellIndex);
                        String cellText = cell == null ? "" : formatter.formatCellValue(cell);
                        content.append("<td>").append(HtmlUtil.escape(cellText)).append("</td>");
                    }

                    content.append("</tr>");
                }

                if (sheet.getLastRowNum() - startRow + 1 > EXCEL_PREVIEW_MAX_ROWS) {
                    content.append("<tr><td class=\"sheet-tip\" colspan=\"999\">仅预览前 ")
                            .append(EXCEL_PREVIEW_MAX_ROWS)
                            .append(" 行，请下载文件查看完整内容。</td></tr>");
                }

                content.append("</tbody></table></div></section>");
            }

            return wrapPreviewHtml(filename, "Excel 预览", content.toString());
        } catch (Exception e) {
            throw new IOException("解析 Excel 失败", e);
        }
    }

    private String buildCsvPreviewHtml(InputStream inputStream, String filename) {
        String text = IoUtil.read(inputStream, StandardCharsets.UTF_8);
        List<String> rows = new ArrayList<>();
        for (String line : text.split("\\R")) {
            if (rows.size() >= EXCEL_PREVIEW_MAX_ROWS) {
                break;
            }
            rows.add(line);
        }

        StringBuilder content = new StringBuilder("<section class=\"sheet\"><h2>CSV 预览</h2><div class=\"table-wrap\"><table><tbody>");
        for (String row : rows) {
            content.append("<tr>");
            String[] columns = row.split(",", -1);
            for (String column : columns) {
                content.append("<td>").append(HtmlUtil.escape(column)).append("</td>");
            }
            content.append("</tr>");
        }
        if (text.split("\\R").length > EXCEL_PREVIEW_MAX_ROWS) {
            content.append("<tr><td class=\"sheet-tip\" colspan=\"999\">仅预览前 ")
                    .append(EXCEL_PREVIEW_MAX_ROWS)
                    .append(" 行，请下载文件查看完整内容。</td></tr>");
        }
        content.append("</tbody></table></div></section>");
        return wrapPreviewHtml(filename, "CSV 预览", content.toString());
    }

    private String wrapPreviewHtml(String filename, String subtitle, String body) {
        return "<!DOCTYPE html>"
                + "<html lang=\"zh-CN\">"
                + "<head>"
                + "<meta charset=\"UTF-8\" />"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />"
                + "<title>" + HtmlUtil.escape(filename) + "</title>"
                + "<style>"
                + "body { margin: 0; padding: 24px; background: #f8fafc; color: #0f172a; font: 14px/1.6 -apple-system, BlinkMacSystemFont, \"Segoe UI\", sans-serif; }"
                + ".container { max-width: 1320px; margin: 0 auto; }"
                + ".header { margin-bottom: 20px; }"
                + ".title { margin: 0 0 6px; font-size: 20px; font-weight: 600; }"
                + ".subtitle { color: #64748b; }"
                + ".sheet { margin-bottom: 20px; padding: 18px; background: #fff; border: 1px solid #e2e8f0; border-radius: 14px; box-shadow: 0 8px 24px rgb(15 23 42 / 6%); }"
                + ".sheet h2 { margin: 0 0 12px; font-size: 16px; }"
                + ".table-wrap { overflow: auto; border: 1px solid #e2e8f0; border-radius: 10px; }"
                + "table { width: max-content; min-width: 100%; border-collapse: collapse; background: #fff; }"
                + "td { min-width: 120px; padding: 10px 12px; border-bottom: 1px solid #e2e8f0; border-right: 1px solid #e2e8f0; vertical-align: top; white-space: pre-wrap; word-break: break-word; }"
                + "tr:nth-child(odd) td { background: #fcfdff; }"
                + ".sheet-tip { color: #64748b; text-align: center; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class=\"container\">"
                + "<div class=\"header\">"
                + "<h1 class=\"title\">" + HtmlUtil.escape(filename) + "</h1>"
                + "<div class=\"subtitle\">" + HtmlUtil.escape(subtitle) + "</div>"
                + "</div>"
                + body
                + "</div>"
                + "</body>"
                + "</html>";
    }
}
