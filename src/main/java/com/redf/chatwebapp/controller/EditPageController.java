package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.UserDAOImpl;
import com.redf.chatwebapp.dao.entities.RoleEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoleEntityRepository;
import com.redf.chatwebapp.dao.repo.UserEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import com.redf.chatwebapp.dto.UserUpdateDto;
import com.redf.chatwebapp.dto.UserUpdateValidatorImpl;
import com.redf.chatwebapp.exception.AvatarTooLargeException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collection;


@Controller
@RequestMapping("/user/edit")
public class EditPageController {


    private UserDAOImpl userDAO;
    private UserService userService;
    private RoleEntityRepository roleEntityRepository;
    private UserEntityRepository userEntityRepository;
    private UserUpdateValidatorImpl userUpdateValidator;


    @Contract(pure = true)
    @Autowired
    public EditPageController(UserDAOImpl userDAO, UserService userService, RoleEntityRepository roleEntityRepository, UserUpdateValidatorImpl userUpdateValidator, UserEntityRepository userEntityRepository) {
        setUserDAO(userDAO);
        setUserService(userService);
        setRoleEntityRepository(roleEntityRepository);
        setUserUpdateValidator(userUpdateValidator);
        setUserEntityRepository(userEntityRepository);
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
        ModelAndView modelAndView = new ModelAndView("editprofile");
        modelAndView.addObject("user", userUpdateDto());
        modelAndView.addObject("tooLarge", avatarTooLargeException(""));
        return modelAndView;

    }


    @PostMapping(consumes = "multipart/form-data")
    public ModelAndView updateUserProfile(@NotNull @ModelAttribute("user") UserUpdateDto userDto, @NotNull BindingResult result) {
        UserEntity existing;
        UserDetails userDetails = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        getUserUpdateValidator().validateAllFields(result, userDto, userDetails, "USER");
        if (result.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView("editprofile");
            modelAndView.addObject("tooLarge", avatarTooLargeException(""));
            return modelAndView;
        }
        userDto.setId(userDetails.getId());
        if (!userDto.getAvatar().isEmpty())
            getUserUpdateValidator().saveAvatar(userDto);
        userDto.setRoles(getRoleList(userDetails.getAuthorities()));
        update(userDto);
        existing = getUserDAO().findByLogin(userDto.getLogin());
        reloadUserDetails(existing, userDetails);
        setAuthentication(userDetails);
        return new ModelAndView("redirect:/user/" + existing.getId());
    }


    @PostMapping(params = {"status", "userId"})
    public Object changeStatus(@RequestParam(value = "status") String status, @RequestParam(value = "userId") String userId) {
        UserEntity user = getUserDAO().findById(Long.parseLong(userId));
        if (status != null && user != null) {
            user.setStatus(status);
            getUserEntityRepository().save(user);
        }
        return null;
    }


    private void setAuthentication(UserDetails userDetails) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    private ArrayList<RoleEntity> getRoleList(@NotNull Collection<? extends GrantedAuthority> authorities) {
        ArrayList<RoleEntity> roles = new ArrayList<>();
        roles.add(getRoleEntityRepository().findByRoleName("USER"));
        if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN")))
            roles.add(getRoleEntityRepository().findByRoleName("ADMIN"));
        return roles;
    }


    private void reloadUserDetails(@NotNull UserEntity existing, @NotNull UserDetails userDetails) {
        userDetails.setUsername(existing.getUsername());
        userDetails.setLogin(existing.getLogin());
        userDetails.setPassword(existing.getPassword());
        userDetails.setRoles(existing.getRoles());
    }


    private void update(@NotNull UserUpdateDto updateDto) {
        getUserService().updateUser(updateDto);
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
    private UserUpdateValidatorImpl getUserUpdateValidator() {
        return userUpdateValidator;
    }

    private void setUserUpdateValidator(UserUpdateValidatorImpl userUpdateValidator) {
        this.userUpdateValidator = userUpdateValidator;
    }

    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }

    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}