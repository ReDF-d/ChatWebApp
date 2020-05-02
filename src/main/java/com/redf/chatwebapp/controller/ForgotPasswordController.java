package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.entities.ForgotPasswordToken;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.ForgotPasswordTokenRepository;
import com.redf.chatwebapp.dao.services.UserService;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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


    @Autowired
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
    public ModelAndView resetPassword(HttpServletRequest request, @RequestParam(value = "email") String email) {
        ModelAndView modelAndView = new ModelAndView("forgotPassword");
        if (email == null || email.trim().equals("")) {
            modelAndView.addObject("message", "Введите почту");
            return modelAndView;
        }
        UserEntity user = getUserService().findByLogin(email);
        if (user == null) {
            modelAndView.addObject("message", "Пользователь не найден!");
            return modelAndView;
        } else {
            ForgotPasswordToken exists = getForgotPasswordTokenRepository().findByUser(user);
            if (exists != null) {
                getForgotPasswordTokenRepository().delete(exists);
            }
            String token = UUID.randomUUID().toString();
            ForgotPasswordToken forgotPasswordToken = new ForgotPasswordToken(token, user);
            getForgotPasswordTokenRepository().save(forgotPasswordToken);
            String recipientAddress = user.getLogin();
            String subject = "Смена пароля в Blinktalk";
            String url = "/changeForgotPassword/" + token;
            SimpleMailMessage emailMessage = new SimpleMailMessage();
            emailMessage.setTo(recipientAddress);
            emailMessage.setSubject(subject);
            String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            emailMessage.setText("Вы запросили смену пароля в Blinktalk. Для смены пароля перейдите по ссылке" + "\r\n" + path + url + "\r\n" + "Если вы не делали этого, игнорируйте это письмо");
            getMailSender().send(emailMessage);
            return new ModelAndView("redirect:/forgotPasswordEmailSent");
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
