package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.entities.ForgotPasswordToken;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.ForgotPasswordTokenRepository;
import com.redf.chatwebapp.dao.services.UserService;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;


@Controller
@RequestMapping("/forgotPassword")
public class ForgotPasswordController {

    private UserService userService;
    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private JavaMailSender mailSender;

    ForgotPasswordController() {
    }

    ForgotPasswordController(UserService userService, ForgotPasswordTokenRepository forgotPasswordTokenRepository, JavaMailSender javaMailSender) {
        setUserService(userService);
        setForgotPasswordTokenRepository(forgotPasswordTokenRepository);
        setMailSender(javaMailSender);
    }

    @GetMapping
    public String getForgotPasswordPage() {
        return "forgotPassword";
    }

    @PostMapping
    public String resetPassword(HttpServletRequest request, @RequestParam(value = "email") String email, @NotNull BindingResult result) {
        UserEntity user = getUserService().findByLogin(email);
        if (user == null) {
            result.rejectValue("error", "Пользователь не найден!");
            return "forgotPassword";
        } else {
            String token = UUID.randomUUID().toString();
            ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(token, user);
            getForgotPasswordTokenRepository().save(forgotPasswordToken);
            String recipientAddress = user.getLogin();
            String subject = "Смена пароля в Blinktalk";
            String url = "/changePassword?token=" + token;
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(recipientAddress);
            emailMessage.setSubject(subject);
            emailMessage.setText("Вы запросили смену пароля в Blinktalk. Для смены пароля перейдите по ссылке" + "\r\n" + request.getContextPath() + url + "\r\n" + "Если вы не делали этого, игнорируйте это письмо");
            getMailSender().send(emailMessage);
            return "forgotPasswordEmailSent";
        }
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

    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Contract(pure = true)
    private ForgotPasswordTokenRepository getForgotPasswordTokenRepository() {
        return forgotPasswordTokenRepository;
    }


    private void setForgotPasswordTokenRepository(ForgotPasswordTokenRepository forgotPasswordTokenRepository) {
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
    }
}
