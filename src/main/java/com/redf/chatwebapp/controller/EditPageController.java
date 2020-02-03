package com.redf.chatwebapp.controller;
/*

import com.redf.chatwebapp.dao.UserDAOImpl;
import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoleEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import com.redf.chatwebapp.dto.UserUpdateDto;
import com.redf.chatwebapp.dto.UserUpdateValidatorImpl;
import com.redf.chatwebapp.exception.AvatarTooLargeException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;


@Controller
@RequestMapping("/user/edit")
public class EditPageController implements HandlerExceptionResolver {


    private UserDAOImpl userDAO;
    private UserService userService;
    private RoleEntityRepository roleEntityRepository;
    private UserDetails userDetails;
    private UserUpdateValidatorImpl userUpdateValidator;


    @Contract(pure = true)
    @Autowired
    public EditPageController(UserDAOImpl userDAO, UserService userService, RoleEntityRepository roleEntityRepository, UserUpdateValidatorImpl userUpdateValidator) {
        this.userDAO = userDAO;
        this.userService = userService;
        this.roleEntityRepository = roleEntityRepository;
        this.userUpdateValidator = userUpdateValidator;
    }


    @ModelAttribute("user")
    public UserUpdateDto userUpdateDto() {
        return new UserUpdateDto();
    }


    @ModelAttribute("tooLarge")
    public AvatarTooLargeException avatarTooLargeException(String message) {
        return new AvatarTooLargeException(message);
    }

    @GetMapping
    public ModelAndView getUserPage() {
        if (isAuthenticated()) {
            ModelAndView modelAndView = new ModelAndView("editprofile");
            modelAndView.addObject("user", userUpdateDto());
            modelAndView.addObject("tooLarge", avatarTooLargeException(""));
            return modelAndView;
        }
        return new ModelAndView("redirect:/home");
    }


    @PostMapping(consumes = "multipart/form-data")
    public ModelAndView updateUserProfile(@NotNull @ModelAttribute("user") UserUpdateDto userDto, @NotNull BindingResult result) {
        UserEntity existing;
        userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userUpdateValidator.validateAllFields(result, userDto, userDetails, "USER");
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("editprofile");
            modelAndView.addObject("tooLarge", avatarTooLargeException(""));
            return modelAndView;
        }
        userDto.setId(userDetails.getId());
        if (!userDto.getAvatar().isEmpty())
            userUpdateValidator.saveAvatar(userDto);
        userDto.setRoles(getRoleList(userDetails.getAuthorities()));
        update(userDto);
        existing = userDAO.findByLogin(userDto.getLogin());
        reloadUserDetails(existing);
        setAuthentication();
        return new ModelAndView("redirect:/user/" + existing.getId());
    }


    private void setAuthentication() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private ArrayList<RoleEntity> getRoleList(@NotNull Collection<? extends GrantedAuthority> authorities) {
        ArrayList<RoleEntity> roles = new ArrayList<>();
        roles.add(roleEntityRepository.findByRoleName("USER"));
        if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN")))
            roles.add(roleEntityRepository.findByRoleName("ADMIN"));
        return roles;
    }


    private void reloadUserDetails(@NotNull UserEntity existing) {
        userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userDetails.setUsername(existing.getUsername());
        userDetails.setLogin(existing.getLogin());
        userDetails.setPassword(existing.getPassword());
        userDetails.setRoles(existing.getRoles());
    }


    private void update(@NotNull UserUpdateDto updateDto) {
        userService.updateUser(updateDto);
    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }


    @Override
    public ModelAndView resolveException(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, Object object, @NotNull Exception exc) {
        ModelAndView modelAndView = new ModelAndView("editprofile");
        if (exc instanceof MaxUploadSizeExceededException) {
            modelAndView.addObject("user", userUpdateDto());
            modelAndView.addObject("tooLarge", avatarTooLargeException("Файл слишком большой!"));
        }
        return modelAndView;
    }
}*/