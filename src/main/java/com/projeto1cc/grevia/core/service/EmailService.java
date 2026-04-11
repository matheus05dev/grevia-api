package com.projeto1cc.grevia.core.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Grevia");
            helper.setTo(toEmail);
            helper.setSubject("Grevia - Recuperação de Senha");

            String htmlContent =
                "<div style=\"font-family: Arial, sans-serif; background-color: #F4F8F5; padding: 40px 20px; color: #333333;\">" +
                "<div style=\"max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 40px; border-radius: 12px; box-shadow: 0 4px 15px rgba(0,0,0,0.05);\">" +
                "<h2 style=\"color: #2F1B3E; text-align: center; font-size: 24px; margin-bottom: 20px;\">Recuperação de Senha</h2>" +
                "<p style=\"font-size: 16px; line-height: 1.5;\">Olá,</p>" +
                "<p style=\"font-size: 16px; line-height: 1.5;\">Você solicitou a recuperação de sua senha no <strong>Grevia</strong>. " +
                "Para criar uma nova senha, utilize o código de segurança abaixo:</p>" +
                "<div style=\"text-align: center; margin: 35px 0;\">" +
                "<span style=\"display: inline-block; background-color: #55B585; color: #ffffff; padding: 15px 30px; " +
                "font-size: 24px; font-weight: bold; border-radius: 8px; letter-spacing: 4px;\">" + resetToken + "</span>" +
                "</div>" +
                "<p style=\"font-size: 16px; line-height: 1.5;\">Este código é <strong>válido por 1 hora</strong>.</p>" +
                "<p style=\"font-size: 16px; line-height: 1.5; color: #666666;\">Caso não tenha solicitado a recuperação, " +
                "fique tranquilo, sua conta está segura e você pode ignorar este e-mail.</p>" +
                "<hr style=\"border: none; border-top: 1px solid #EAEAEA; margin: 40px 0 20px 0;\">" +
                "<p style=\"font-size: 14px; color: #888888; text-align: center; margin: 0;\">Equipe Grevia<br>" +
                "<span style=\"color: #55B585;\">Tecnologia a serviço da natureza</span></p>" +
                "</div>" +
                "</div>";

            helper.setText(htmlContent, true);

            log.info("Enviando e-mail de recuperação de senha para: {}", toEmail);
            mailSender.send(message);
            log.info("E-mail de recuperação enviado com sucesso para: {}", toEmail);

        } catch (MessagingException e) {
            log.error("Erro ao enviar e-mail de recuperação para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail de recuperação. Tente novamente mais tarde.", e);
        } catch (Exception e) {
            log.error("Erro inesperado ao enviar e-mail para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail de recuperação. Tente novamente mais tarde.", e);
        }
    }
}
