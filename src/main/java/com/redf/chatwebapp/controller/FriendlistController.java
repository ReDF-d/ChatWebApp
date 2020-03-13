package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.FriendshipDAOImpl;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@ControllerAdvice
@RequestMapping("/friends")
public class FriendlistController {


    private FriendshipDAOImpl friendshipDAO;
    private UserEntity principal;
    private UserService userService;
    private FriendshipEntityRepository friendshipEntityRepository;


    @Autowired
    public FriendlistController(FriendshipDAOImpl friendshipDAO, UserService userService, FriendshipEntityRepository friendshipEntityRepository) {
        setFriendshipDAO(friendshipDAO);
        setUserService(userService);
        setFriendshipEntityRepository(friendshipEntityRepository);
    }


    @GetMapping
    public ModelAndView getFriendlistPage() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        setPrincipal(getUserService().findById(userDetails.getId()));
        ModelAndView modelAndView = new ModelAndView("friendlist");
        modelAndView.addObject("friendlist", getFriendshipDAO().getUserFriends(getPrincipal().getId(), getFriendshipEntityRepository().getUserFriends(getPrincipal().getId())));
        modelAndView.addObject("pending", getFriendshipDAO().getUserFriends(getPrincipal().getId(), getFriendshipEntityRepository().getUserFriendRequests(getPrincipal().getId())));
        return modelAndView;
    }

    @Contract(pure = true)
    private FriendshipDAOImpl getFriendshipDAO() {
        return friendshipDAO;
    }


    private void setFriendshipDAO(FriendshipDAOImpl friendshipDAO) {
        this.friendshipDAO = friendshipDAO;
    }


    @Contract(pure = true)
    private UserEntity getPrincipal() {
        return principal;
    }


    private void setPrincipal(UserEntity principal) {
        this.principal = principal;
    }

    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }

    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Contract(pure = true)
    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }

    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }
}
