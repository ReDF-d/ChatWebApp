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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


@Controller
@ControllerAdvice
@RequestMapping("/adminpanel/edituser/{id}")
public class AdminEditUserController {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    private UserDAOImpl userDAO;
    private SessionRegistry sessionRegistry;
    private UserUpdateValidatorImpl userUpdateValidator;
    private UserService userService;
    private RoleEntityRepository roleEntityRepository;
    private UserUpdateDto userUpdateDto;
    private UserEntityRepository userEntityRepository;


    @Autowired
    public AdminEditUserController(UserDAOImpl userDAO, SessionRegistry sessionRegistry, UserUpdateValidatorImpl userUpdateValidator, UserService userService, RoleEntityRepository roleEntityRepository, UserEntity user, UserUpdateDto userUpdateDto, UserEntityRepository userEntityRepository) {
        setSessionRegistry(sessionRegistry);
        setUserDAO(userDAO);
        setUserUpdateValidator(userUpdateValidator);
        setUserService(userService);
        setRoleEntityRepository(roleEntityRepository);
        setUserUpdateDto(userUpdateDto);
        setUserEntityRepository(userEntityRepository);
    }


    public AdminEditUserController() {
    }


    @Contract(pure = true)
    private static SimpleDateFormat getSdf() {
        return sdf;
    }


    @ModelAttribute("tooLarge")
    public AvatarTooLargeException avatarTooLargeException(String message) {
        return new AvatarTooLargeException(message);
    }


    @GetMapping
    public ModelAndView getUserPage(@PathVariable String id) {
        UserEntity user = getUserDAO().findById(Long.parseLong(id));
        ModelAndView modelAndView = new ModelAndView("adminedituser");
        modelAndView.addObject("userUpdating", user);
        modelAndView.addObject("userDto", getUserUpdateDto());
        modelAndView.addObject("roles", getRolesAsList(user));
        modelAndView.addObject("tooLarge", avatarTooLargeException(""));
        return modelAndView;
    }


    @NotNull
    private ArrayList<RoleEntity> getRoleList(@NotNull Collection<? extends GrantedAuthority> authorities, UserUpdateDto userUpdateDto) {
        ArrayList<RoleEntity> roles = new ArrayList<>();
        roles.add(getRoleEntityRepository().findByRoleName("USER"));
        if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ADMIN")) || userUpdateDto.isMakeAdmin())
            roles.add(getRoleEntityRepository().findByRoleName("ADMIN"));
        return roles;
    }


    private void update(UserUpdateDto updateDto) {
        getUserService().updateUser(updateDto);
    }


    private ArrayList<String> getRolesAsList(@NotNull UserEntity user) {
        return user.getRoles().stream().map(RoleEntity::getRole).collect(Collectors.toCollection(ArrayList::new));
    }


    @PostMapping
    public String updateUserProfile(@NotNull @ModelAttribute("userDto") UserUpdateDto userUpdateDto, BindingResult result, @PathVariable String id) {
        UserEntity user;
        if (getUserEntityRepository().findById(Long.parseLong(id)).isPresent()) {
            user = getUserEntityRepository().findById(Long.parseLong(id)).get();
            UserDetails userDetails = new UserDetails(user.getId(), user.getLogin(), user.getUsername(), user.getPassword(), user.getRoles(), !user.getIsLocked(), user.isEnabled());
            getUserUpdateValidator().validateAllFields(result, userUpdateDto, userDetails, "ADMIN");
            if (result.hasErrors()) {
                return "redirect:/adminedituser";
            }
            userUpdateDto.setId(userDetails.getId());
            userUpdateDto.setRoles(getRoleList(userDetails.getAuthorities(), userUpdateDto));
            if (userUpdateDto.isMarkBanned()) {
                try {
                    long millis = System.currentTimeMillis();
                    Date date = new Date(millis);
                    userUpdateDto.setStarted(new Timestamp(getSdf().parse(getSdf().format(date)).getTime()));
                    if (userUpdateDto.getDateTimeLocal() != null && !userUpdateDto.getDateTimeLocal().equals("")) {
                        Date parsedDate = getSdf().parse(userUpdateDto.getDateTimeLocal());
                        userUpdateDto.setEnds(new Timestamp(parsedDate.getTime()));
                    } else userUpdateDto.setEnds(null);
                } catch (ParseException e) {
                    System.out.println(e.getMessage());
                }
            }
            userUpdateDto.setStatus(user.getStatus());
            update(userUpdateDto);
            if (!userUpdateDto.getAvatar().isEmpty())
                getUserUpdateValidator().saveAvatar(userUpdateDto);
            return "redirect:/adminpanel";
        } else
            return "redirect:/adminpanel";
    }


    @Contract(pure = true)
    private SessionRegistry getSessionRegistry() {
        return sessionRegistry;
    }


    private void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }


    @Contract(pure = true)
    private UserDAOImpl getUserDAO() {
        return userDAO;
    }


    private void setUserDAO(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
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
    private UserUpdateValidatorImpl getUserUpdateValidator() {
        return userUpdateValidator;
    }


    private void setUserUpdateValidator(UserUpdateValidatorImpl userUpdateValidator) {
        this.userUpdateValidator = userUpdateValidator;
    }


    @Contract(pure = true)
    private UserUpdateDto getUserUpdateDto() {
        return userUpdateDto;
    }


    private void setUserUpdateDto(UserUpdateDto userUpdateDto) {
        this.userUpdateDto = userUpdateDto;
    }


    @Contract(pure = true)
    private UserEntityRepository getUserEntityRepository() {
        return userEntityRepository;
    }


    private void setUserEntityRepository(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }
}