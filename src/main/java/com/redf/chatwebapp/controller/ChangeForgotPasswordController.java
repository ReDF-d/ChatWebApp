package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.entities.ForgotPasswordToken;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.ForgotPasswordTokenRepository;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import com.redf.chatwebapp.dto.ForgotPasswordDto;
import com.redf.chatwebapp.security.BcryptPasswordEncoder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/changeForgotPassword")
public class ChangeForgotPasswordController {

    private ForgotPasswordTokenRepository forgotPasswordTokenRepository;
    private UserEntityRepository userEntityRepository;

    public ChangeForgotPasswordController() {
    }

    @GetMapping
    public String get(){
        return "changeForgotPassword";
    }

    /*public ChangeForgotPasswordController(ForgotPasswordTokenRepository forgotPasswordTokenRepository, UserEntityRepository userEntityRepository) {
        setForgotPasswordTokenRepository(forgotPasswordTokenRepository);
        setUserEntityRepository(userEntityRepository);
    }

    @ModelAttribute("changePassword")
    public ForgotPasswordDto getForgotPasswordDto() {
        return new ForgotPasswordDto();
    }

    @GetMapping
    public ModelAndView getChangePasswordPage(@RequestParam("token") String token) {
        ForgotPasswordToken forgotPasswordToken = forgotPasswordTokenRepository.findByToken(token);
        if (forgotPasswordToken == null) {
            ModelAndView modelAndView = new ModelAndView("changeForgotPassword");
            modelAndView.addObject("message", "Ссылка недействительна");
            return modelAndView;
        }
        ModelAndView modelAndView = new ModelAndView("changeForgotPassword");
        modelAndView.addObject("token", forgotPasswordToken);
        return modelAndView;
    }


    @PostMapping
    public String changeForgotPassword(@Valid @ModelAttribute("user") ForgotPasswordDto forgotPasswordDto, @NotNull BindingResult result) {
        ForgotPasswordToken passwordToken = getForgotPasswordTokenRepository().findByToken(forgotPasswordDto.getToken());
        UserEntity user = passwordToken.getUser();
        checkPassword(result, forgotPasswordDto);
        if (result.hasErrors())
            return "changeForgotPassword";
        user.setPassword(BcryptPasswordEncoder.passwordEncoder().encode(forgotPasswordDto.getPassword()).trim());
        getUserEntityRepository().save(user);
        getForgotPasswordTokenRepository().delete(passwordToken);
        return "home";
    }


    private void checkPassword(BindingResult result, @NotNull ForgotPasswordDto forgotPasswordDto) {
        if (!forgotPasswordDto.getPassword().trim().equals("")) {
            if (forgotPasswordDto.getConfirmPassword().trim().equals(""))
                result.rejectValue("error", null, "Подтвердите пароль");
            if (!forgotPasswordDto.getPassword().trim().equals(forgotPasswordDto.getConfirmPassword().trim()))
                result.rejectValue("error", null, "Пароли не совпадают");
        } else {
            result.rejectValue("error", "Введите новый пароль");
        }
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
    }*/
}
