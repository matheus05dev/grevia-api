package com.projeto1cc.grevia.core.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username:contactgrevia@gmail.com}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Grevia API - Recuperação de Senha");
        message.setText("Você solicitou a recuperação de sua senha.\n\n" +
                "Utilize o token a seguir para redefinir sua senha: " + resetToken + "\n\n" +
                "Este token é válido por 1 hora.\n" +
                "Caso não tenha solicitado, ignore este e-mail.");

        try {
            log.info("Tentando enviar e-mail de recuperação para: {}", toEmail);
            javaMailSender.send(message);
            log.info("E-mail de recuperação enviado com sucesso para: {}", toEmail);
        } catch (MailException e) {
            log.error("Erro ao enviar e-mail de recuperação para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail de recuperação. Tente novamente mais tarde.", e);
        }
    }
}
