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
import org.jetbrains.annotations.Nullable;
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
        ModelAndView modelAndView = new ModelAndView("profilepage");
        UserDetails userDetails = null;
        if (isAuthenticated())
            userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails != null) {
            FriendshipEntity friendshipEntity = getFriendshipEntityRepository().findById(user.getId(), userDetails.getId());
            modelAndView.addObject("isFriend", isFriends(userDetails.getId(), user.getId()));
            if (friendshipEntity != null) {
                if (isFriends(userDetails.getId(), user.getId()))
                    modelAndView.addObject("friends", "Удалить " + user.getUsername() + " из друзей");
                if (isPending(userDetails.getId(), user.getId())) {
                    if (friendshipEntity.getLastAction() == userDetails.getId().intValue())
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
    public String sendMessage(@PathVariable String id) {
        Long uId = (Long.parseLong(id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = getUserService().findById(userDetails.getId());
        UserEntity owner = getUserService().findById(uId);
        if (getFriendshipEntityRepository().findById(principal.getId(), owner.getId()) != null) {
            RoomEntity room = getDialogueByIds(principal.getId(), owner.getId());
            if (room != null)
                return "redirect:/chat/" + room.getId();
            else {
                List<UserEntity> members = new ArrayList<>();
                members.add(principal);
                members.add(owner);
                getRoomDAO().createAndSave("dialogue", members, null, principal);
                room = getDialogueByIds(principal.getId(), owner.getId());
                assert room != null;
                return "redirect:/chat/" + room.getId();
            }
        }
        return "redirect:/home";
    }


    @PostMapping
    public String addFriend(@PathVariable String id) {
        UserEntity user = getUserService().findById(Long.parseLong(id));
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserEntity principal = getUserService().findById(userDetails.getId());
        FriendshipEntity friendshipEntity;
        if (isPending(principal.getId(), user.getId()) || isFriends(principal.getId(), user.getId())) {
            friendshipEntity = getFriendshipEntityRepository().findById(user.getId(), userDetails.getId());
            if (isFriends(principal.getId(), user.getId())) {
                getFriendshipEntityRepository().delete(friendshipEntity);
            }
            if (isPending(principal.getId(), user.getId()) && friendshipEntity.getLastAction() == userDetails.getId().intValue()) {
                getFriendshipEntityRepository().delete(friendshipEntity);
            }
            if (isPending(principal.getId(), user.getId()) && friendshipEntity.getLastAction() != userDetails.getId().intValue()) {
                friendshipEntity.setStatus("friends");
                getFriendshipDAO().update(friendshipEntity);
            }
        } else
            getFriendshipEntityRepository().save(new FriendshipEntity(getUserService().findById(user.getId()), getUserService().findById(userDetails.getId()), "pending", userDetails.getId().intValue()));
        return "redirect:/user/" + Long.parseLong(id);
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


    private boolean isFriends(Long id1, Long id2) {
        FriendshipEntity friendshipEntity = getFriendshipEntityRepository().findById(id1, id2);
        return (friendshipEntity != null) && (friendshipEntity.getStatus().equals("friends"));
    }


    private boolean isPending(Long id1, Long id2) {
        FriendshipEntity friendshipEntity = getFriendshipEntityRepository().findById(id1, id2);
        return (friendshipEntity != null) && (friendshipEntity.getStatus().equals("pending"));
    }


    @Nullable
    private RoomEntity getDialogueByIds(Long id1, Long id2) {
        ArrayList<RoomEntity> roomEntities = (ArrayList<RoomEntity>) getRoomEntityRepository().findDialogueByMembers(id1, id2);
        long roomId = 0;
        //  short counter = 0;
        for (RoomEntity room : roomEntities) {
            if (room.getId() == roomId)
                return room;
            else
                roomId = room.getId();
        }
        return null;
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