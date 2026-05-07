package com.snail.common.core.utils.mail;

import cn.hutool.core.util.StrUtil;
import com.snail.common.core.utils.SpringUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

/**
 * 邮件工具类
 *
 * @author Levi
 * @since 1.0
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MailUtils {

    private static final String DEFAULT_PERSONAL = "Snail Cloud";

    public static void sendHtml(String to, String subject, String htmlContent) {
        JavaMailSender mailSender = SpringUtils.getBean(JavaMailSender.class);
        Environment environment = SpringUtils.getBean(Environment.class);
        String from = StrUtil.blankToDefault(
                environment.getProperty("spring.mail.from"),
                environment.getProperty("spring.mail.username")
        );
        if (StrUtil.isBlank(from)) {
            throw new IllegalStateException("未配置邮件发件人，请检查 spring.mail.from 或 spring.mail.username");
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    true,
                    StandardCharsets.UTF_8.name()
            );
            helper.setFrom(from, StrUtil.blankToDefault(environment.getProperty("spring.application.name"), DEFAULT_PERSONAL));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error(
                    "邮件发送失败, host={}, port={}, from={}, to={}, subject={}",
                    environment.getProperty("spring.mail.host"),
                    environment.getProperty("spring.mail.port"),
                    from,
                    to,
                    subject,
                    e
            );
            throw new IllegalStateException("发送邮件失败", e);
        }
    }
}
