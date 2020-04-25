package com.redf.chatwebapp.controller;


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
        setUserDAO(userDAO);
        setUserService(userService);
        setRoleEntityRepository(roleEntityRepository);
        setUserUpdateValidator(userUpdateValidator);
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
        } else return new ModelAndView("redirect:/home");
    }


    @PostMapping(consumes = "multipart/form-data")
    public ModelAndView updateUserProfile(@NotNull @ModelAttribute("user") UserUpdateDto userDto, @NotNull BindingResult result) {
        UserEntity existing;
        setUserDetails((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        getUserUpdateValidator().validateAllFields(result, userDto, getUserDetails(), "USER");
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("editprofile");
            modelAndView.addObject("tooLarge", avatarTooLargeException(""));
            return modelAndView;
        }
        userDto.setId(getUserDetails().getId());
        if (!userDto.getAvatar().isEmpty())
            getUserUpdateValidator().saveAvatar(userDto);
        userDto.setRoles(getRoleList(getUserDetails().getAuthorities()));
        update(userDto);
        existing = getUserDAO().findByLogin(userDto.getLogin());
        reloadUserDetails(existing);
        setAuthentication();
        return new ModelAndView("redirect:/user/" + existing.getId());
    }


    private void setAuthentication() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(getUserDetails(), getUserDetails().getPassword(), getUserDetails().getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private ArrayList<RoleEntity> getRoleList(@NotNull Collection<? extends GrantedAuthority> authorities) {
        ArrayList<RoleEntity> roles = new ArrayList<>();
        roles.add(getRoleEntityRepository().findByRoleName("USER"));
        if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN")))
            roles.add(getRoleEntityRepository().findByRoleName("ADMIN"));
        return roles;
    }


    private void reloadUserDetails(@NotNull UserEntity existing) {
        setUserDetails((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        getUserDetails().setUsername(existing.getUsername());
        getUserDetails().setLogin(existing.getLogin());
        getUserDetails().setPassword(existing.getPassword());
        getUserDetails().setRoles(existing.getRoles());
    }


    private void update(@NotNull UserUpdateDto updateDto) {
        getUserService().updateUser(updateDto);
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


    @Contract(pure = true)
    private RoleEntityRepository getRoleEntityRepository() {
        return roleEntityRepository;
    }


    private void setRoleEntityRepository(RoleEntityRepository roleEntityRepository) {
        this.roleEntityRepository = roleEntityRepository;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Contract(pure = true)
    private UserDAOImpl getUserDAO() {
        return userDAO;
    }


    private void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }


    @Contract(pure = true)
    private UserDetails getUserDetails() {
        return userDetails;
    }

    private void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    @Contract(pure = true)
    private UserUpdateValidatorImpl getUserUpdateValidator() {
        return userUpdateValidator;
    }

    private void setUserUpdateValidator(UserUpdateValidatorImpl userUpdateValidator) {
        this.userUpdateValidator = userUpdateValidator;
    }
}