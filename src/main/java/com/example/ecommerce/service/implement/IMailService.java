package com.example.ecommerce.service.implement;

import com.example.ecommerce.service.MailService;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class IMailService implements MailService {
    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender javaMailSender;
    public IMailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Async
    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendVerifyRegisterEmail(String toAddress, String verifyCode) {
        String fromAddress = fromEmail;
        String senderName = "CHOVN";
        String subject = "Mã xác nhận tài khoản người dùng CHOVN";
        String content = "Xin chào [[name]],<br>"
                + "Đây là mã xác nhận để kích hoạt tài khoản người dùng CHOVN của bạn, vui lòng không để lộ ra ngoài!:<br>"
                + "<h4>Mã xác nhận: [[verifyCode]]</h4><br>"
                + "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi,<br>"
                + "[[Company]]";
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", toAddress);
            content = content.replace("[[verifyCode]]", verifyCode);
            content = content.replace("[[Company]]", senderName);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendFogotPasswordMail(String fullName, String toAddress, String verifyCode) {
        String fromAddress = fromEmail;
        String senderName = "CHOVN";
        String subject = "Mã xác nhận đổi mặt khẩu tài khoản CHOVN";
        String content = "Xin chào [[name]],<br>"
                + "Đây là mã đổi mật khẩu tạm thời của bạn, vui lòng không để lộ ra ngoài!<br>"
                + "<h4>Mã xác nhận: [[verifyCode]]</h4><br>"
                + "Cảm ơn bạn,<br>"
                + "[[Company]]";
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom(fromAddress, senderName);
            helper.setTo(toAddress);
            helper.setSubject(subject);
            content = content.replace("[[name]]", fullName);
            content = content.replace("[[verifyCode]]", verifyCode);
            content = content.replace("[[Company]]", senderName);
            helper.setText(content, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
