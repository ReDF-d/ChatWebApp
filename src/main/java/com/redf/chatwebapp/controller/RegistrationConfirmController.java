package com.redf.chatwebapp.controller;


import com.redf.chatwebapp.dao.RoomDAOImpl;
import com.redf.chatwebapp.dao.entities.EmailVerificationToken;
import com.redf.chatwebapp.dao.entities.RoomEntity;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/registrationConfirm")
public class RegistrationConfirmController {


    private UserService userService;
    private RoomDAOImpl roomDAO;
    private RoomEntityRepository roomEntityRepository;


    @Autowired
    public RegistrationConfirmController(UserService userService, RoomDAOImpl roomDAO, RoomEntityRepository roomEntityRepository) {
        setUserService(userService);
        setRoomDAO(roomDAO);
        setRoomEntityRepository(roomEntityRepository);
    }


    @GetMapping
    public ModelAndView confirmRegistration(@RequestParam("token") String token) {
        EmailVerificationToken verificationToken = getUserService().getVerificationToken(token);
        if (verificationToken == null) {
            ModelAndView modelAndView = new ModelAndView("registrationConfirmSuccess");
            modelAndView.addObject("message", "Ссылка недействительна");
            return modelAndView;
        }
        UserEntity user = verificationToken.getUser();
        user.setEnabled(true);
        getUserService().updateUser(user);
        addUserToGlobalChat(user);
        return new ModelAndView("registrationConfirmSuccess");
    }

    @Contract(pure = true)
    private UserService getUserService() {
        return userService;
    }

    private void setUserService(UserService userService) {
        this.userService = userService;
    }

    private void addUserToGlobalChat(UserEntity user) {
        getRoomDAO().update(getGlobalChatRoom(user));
    }


    private RoomEntity getGlobalChatRoom(UserEntity user) {
        return getRoomEntityRepository().findRoomById(1).addRoomMember(user);
    }

    @Contract(pure = true)
    private RoomDAOImpl getRoomDAO() {
        return roomDAO;
    }

    private void setRoomDAO(RoomDAOImpl roomDAO) {
        this.roomDAO = roomDAO;
    }

    @Contract(pure = true)
    private RoomEntityRepository getRoomEntityRepository() {
        return roomEntityRepository;
    }

    private void setRoomEntityRepository(RoomEntityRepository roomEntityRepository) {
        this.roomEntityRepository = roomEntityRepository;
    }
}
