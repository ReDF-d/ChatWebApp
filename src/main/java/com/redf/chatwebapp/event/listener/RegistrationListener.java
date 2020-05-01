package com.redf.chatwebapp.event.listener;

import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.event.OnRegistrationCompleteEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private UserService userService;
    private JavaMailSender mailSender;


    @Autowired

    public RegistrationListener(UserService userService, JavaMailSender javaMailSender) {
        setUserService(userService);
        setMailSender(javaMailSender);
    }


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }


    private void confirmRegistration(@NotNull OnRegistrationCompleteEvent event) {
        UserEntity user = event.getUser();
        String token = UUID.randomUUID().toString();
        getUserService().createVerificationToken(user, token);
        String recipientAddress = user.getLogin();
        String subject = "Подтверждение регистрации";
        String confirmationUrl = event.getAppUrl() + "registrationConfirm?token=" + token;
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText("Ссылка для завершения процесса регистрации в BlinkTalk " + "\r\n" + "http://localhost:8080/" + confirmationUrl);
        getMailSender().send(email);
    }

    @Contract(pure = true)
    private JavaMailSender getMailSender() {
        return mailSender;
    }

    private void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }

    private void setUserService(UserService service) {
        this.userService = service;
    }
}
