package com.projeto1cc.grevia.core.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${resend.api.key}")
    private String resendApiKey;

    @Value("${resend.mail.from:onboarding@resend.dev}")
    private String fromEmail;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        Resend resend = new Resend(resendApiKey);

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from(fromEmail)
                .to(toEmail)
                .subject("Grevia API - Recuperação de Senha")
                .html("<h2>Grevia Módulo de Conta</h2>" +
                      "<p>Você solicitou a recuperação de sua senha.</p>" +
                      "<p>Utilize o token a seguir para redefinir sua senha: <strong>" + resetToken + "</strong></p>" +
                      "<br><p>Este token é válido por 1 hora.</p>" +
                      "<p>Caso não tenha solicitado, ignore este e-mail.</p>")
                .build();

        try {
            log.info("Tentando enviar e-mail de recuperação via Resend API para: {}", toEmail);
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
            log.info("E-mail de recuperação enviado com sucesso via Resend (ID: {})", data.getId());
        } catch (ResendException e) {
            log.error("Erro ao enviar e-mail de recuperação via Resend para {}: {}", toEmail, e.getMessage(), e);
            throw new RuntimeException("Falha ao enviar e-mail de recuperação. Tente novamente mais tarde.", e);
        }
    }
}
