package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.FriendshipDAOImpl;
import com.redf.chatwebapp.dao.RoomDAOImpl;
import com.redf.chatwebapp.dao.entities.FriendshipEntity;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.FriendshipEntityRepository;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dao.utils.UserDetails;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
@ControllerAdvice
@RequestMapping("/user/{id}")
public class ProfilePageController {


    private UserService userService;
    private FriendshipDAOImpl friendshipDAO;
    private FriendshipEntityRepository friendshipEntityRepository;
    private boolean friends = false;
    private boolean pending = false;
    private UserDetails principal;
    private Long id;
    private RoomEntityRepository roomEntityRepository;
    private RoomDAOImpl roomDAO;


    private ArrayList<FriendshipEntity> userFriends;


    @Contract(pure = true)
    @Autowired
    ProfilePageController(UserService userService, FriendshipDAOImpl friendshipDAO, FriendshipEntityRepository friendshipEntityRepository, RoomEntityRepository roomEntityRepository, RoomDAOImpl roomDAO) {
        setFriendshipEntityRepository(friendshipEntityRepository);
        setFriendshipDAO(friendshipDAO);
        setUserService(userService);
        setRoomEntityRepository(roomEntityRepository);
        setRoomDAO(roomDAO);
    }


    @GetMapping
    public ModelAndView getUserPage(@PathVariable String id) {
        UserEntity user = getUserService().findById(Long.parseLong(id));
        this.id = Long.parseLong(id);
        ModelAndView modelAndView = new ModelAndView("profilepage");
        if (isAuthenticated())
            setPrincipal((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        if (getPrincipal() != null) {
            initFriendsFields(getPrincipal().getId(), user.getId());
            modelAndView.addObject("isFriend", isFriends());
            FriendshipEntity friendshipEntity = getFriendshipEntityRepository().findById(getPrincipal().getId(), user.getId());
            if (friendshipEntity != null) {
                if (isFriends())
                    modelAndView.addObject("friends", "Удалить " + user.getUsername() + " из друзей");
                if (isPending()) {
                    if (friendshipEntity.getLastAction() == getPrincipal().getId().intValue())
                        modelAndView.addObject("friends", "Вы отправили заявку в друзья");
                    else
                        modelAndView.addObject("friends", "Принять заявку в друзья");
                }
            } else
                modelAndView.addObject("friends", "Добавить " + user.getUsername() + " в друзья");
        }
        modelAndView.addObject("user", user);
        String url = getPrincipalPageUrl();
        modelAndView.addObject("url", url);
        setUserFriends((ArrayList<FriendshipEntity>) getFriendshipEntityRepository().getUserFriends(user.getId()));
        modelAndView.addObject("friendsCount", "Друзей:" + getUserFriends().size());
        return modelAndView;
    }


    @PutMapping
    public String getEditPage() {
        return "redirect:/user/edit";
    }


    @PatchMapping
    public String sendMessage() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = getUserService().findById(userDetails.getId());
        UserEntity owner = getUserService().findById(id);
        if (getFriendshipEntityRepository().findById(principal.getId(), owner.getId()) != null) {
            RoomEntity room = getRoomEntityRepository().findDialogueByMembers(principal.getId(), principal.getId());
            if (room != null)
                return "redirect:/chat/" + room.getId();
            else {
                List<UserEntity> members = new ArrayList<>();
                members.add(principal);
                members.add(owner);
                getRoomDAO().createAndSave("dialogue", members, null);
                room = getRoomEntityRepository().findDialogueByMembers(principal.getId(), principal.getId());
                return "redirect:/chat/" + room.getId();
            }
        }
        return "redirect:/home";
    }


    @PostMapping
    public String addFriend() {
        UserEntity user = getUserService().findById(this.id);
        if (isPending() || isFriends()) {
            FriendshipEntity friendshipEntity = getFriendshipEntityRepository().findById(user.getId(), getPrincipal().getId());
            if (isFriends()) {
                getFriendshipDAO().delete(friendshipEntity);
                setFriends(false);
                setPending(false);
            }
            if (isPending() && friendshipEntity.getLastAction() == getPrincipal().getId().intValue()) {
                getFriendshipDAO().delete(friendshipEntity);
                setFriends(false);
                setPending(false);
            }
            if (isPending() && friendshipEntity.getLastAction() != getPrincipal().getId().intValue()) {
                friendshipEntity.setStatus("friends");
                getFriendshipDAO().update(friendshipEntity);
                setPending(false);
                setFriends(true);
            }
        } else
            getFriendshipDAO().createAndSave(getUserService().findById(getPrincipal().getId()), getUserService().findById(this.id), "pending", getPrincipal().getId().intValue());
        return "redirect:/user/" + this.id;
    }


    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();

    }


    @NotNull
    private String getPrincipalPageUrl() {
        UserDetails userDetails;
        String url;
        if (!isAuthenticated()) {
            url = "/user/-1";
        } else {
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            url = "/user/" + userDetails.getId();
        }
        return url;
    }


    private void initFriendsFields(Long id1, Long id2) {
        FriendshipEntity friendshipEntity = getFriendshipEntityRepository().findById(id1, id2);
        if (friendshipEntity != null) {
            if (!friendshipEntity.getStatus().equals("friends"))
                setPending(true);
            else if (friendshipEntity.getStatus().equals("friends"))
                setFriends(true);
        }
    }


    @Contract(pure = true)
    private ArrayList<FriendshipEntity> getUserFriends() {
        return userFriends;
    }


    private void setUserFriends(ArrayList<FriendshipEntity> userFriends) {
        this.userFriends = userFriends;
    }


    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }


    private void setUserService(UserService userService) {
        this.userService = userService;
    }


    @Contract(pure = true)
    private FriendshipDAOImpl getFriendshipDAO() {
        return friendshipDAO;
    }


    private void setFriendshipDAO(FriendshipDAOImpl friendshipDAO) {
        this.friendshipDAO = friendshipDAO;
    }


    @Contract(pure = true)
    private FriendshipEntityRepository getFriendshipEntityRepository() {
        return friendshipEntityRepository;
    }


    private void setFriendshipEntityRepository(FriendshipEntityRepository friendshipEntityRepository) {
        this.friendshipEntityRepository = friendshipEntityRepository;
    }


    @Contract(pure = true)
    private boolean isPending() {
        return pending;
    }


    private void setPending(boolean pending) {
        this.pending = pending;
    }


    @Contract(pure = true)
    private UserDetails getPrincipal() {
        return principal;
    }


    private void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }


    @Contract(pure = true)
    private boolean isFriends() {
        return friends;
    }


    private void setFriends(boolean friends) {
        this.friends = friends;
    }


    @Contract(pure = true)
    private RoomEntityRepository getRoomEntityRepository() {
        return roomEntityRepository;
    }


    private void setRoomEntityRepository(RoomEntityRepository roomEntityRepository) {
        this.roomEntityRepository = roomEntityRepository;
    }

    @Contract(pure = true)
    private RoomDAOImpl getRoomDAO() {
        return roomDAO;
    }


    private void setRoomDAO(RoomDAOImpl roomDAO) {
        this.roomDAO = roomDAO;
    }
}