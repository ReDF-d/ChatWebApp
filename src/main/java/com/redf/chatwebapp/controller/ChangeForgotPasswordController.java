package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.entities.ForgotPasswordToken;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.ForgotPasswordTokenRepository;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import com.redf.chatwebapp.dto.ForgotPasswordDto;
import com.redf.chatwebapp.security.BcryptPasswordEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/changeForgotPassword/{token}")
public class ChangeForgotPasswordController {

    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private UserEntityRepository userEntityRepository;

    public ChangeForgotPasswordController() {
    }


    @Autowired
    public ChangeForgotPasswordController(ForgotPasswordTokenRepository forgotPasswordTokenRepository, UserEntityRepository userEntityRepository) {
        setForgotPasswordTokenRepository(forgotPasswordTokenRepository);
        setUserEntityRepository(userEntityRepository);
    }


    @ModelAttribute("changePassword")
    public ForgotPasswordDto getForgotPasswordDto() {
        return new ForgotPasswordDto();
    }


    @GetMapping
    public ModelAndView getChangePasswordPage(@PathVariable String token) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);
        ModelAndView modelAndView = new ModelAndView("changeForgotPassword");
        if (forgotPasswordToken == null) {
            modelAndView.addObject("message", "Ссылка недействительна");
            return modelAndView;
        }
        modelAndView.addObject("token", token);
        return modelAndView;
    }


    @PostMapping
    public ModelAndView changeForgotPassword(@PathVariable String token, @ModelAttribute("user") ForgotPasswordDto forgotPasswordDto) {
        ForgotPasswordToken passwordToken = getForgotPasswordTokenRepository().findByToken(token);
        UserEntity user = passwordToken.getUser();
        ModelAndView modelAndView = new ModelAndView("changeForgotPassword");
        if (!checkPassword(modelAndView, forgotPasswordDto))
            return modelAndView;
        user.setPassword(BcryptPasswordEncoder.passwordEncoder().encode(forgotPasswordDto.getPassword()).trim());
        getUserEntityRepository().save(user);
        getForgotPasswordTokenRepository().delete(passwordToken);
        return new ModelAndView("redirect:/login");
    }


    private boolean checkPassword(ModelAndView modelAndView, @NotNull ForgotPasswordDto forgotPasswordDto) {
        if (!forgotPasswordDto.getPassword().trim().equals("")) {
            if (forgotPasswordDto.getConfirmPassword().trim().equals("")) {
                modelAndView.addObject("message", "Подтвердите пароль");
                return false;
            }
            if (!forgotPasswordDto.getPassword().trim().equals(forgotPasswordDto.getConfirmPassword().trim())) {
                modelAndView.addObject("message", "Пароли не совпадают");
                return false;
            }
        } else {
            modelAndView.addObject("message", "Введите новый пароль");
            return false;
        }
        return true;
    }


    @Contract(pure = true)
    private ForgotPasswordTokenRepository getForgotPasswordTokenRepository() {
        return forgotPasswordTokenRepository;
    }


    private void setForgotPasswordTokenRepository(ForgotPasswordTokenRepository forgotPasswordTokenRepository) {
        this.forgotPasswordTokenRepository = forgotPasswordTokenRepository;
    }


    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }


    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}
