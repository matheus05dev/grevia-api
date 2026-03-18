package com.projeto1cc.grevia.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("contactgrevia@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Grevia API - Recuperação de Senha");
        message.setText("Você solicitou a recuperação de sua senha.\n\n" +
                "Utilize o token a seguir para redefinir sua senha: " + resetToken + "\n\n" +
                "Este token é válido por 1 hora.\n" +
                "Caso não tenha solicitado, ignore este e-mail.");

        javaMailSender.send(message);
    }
}
