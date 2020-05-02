package com.redf.chatwebapp.controller;

import com.redf.chatwebapp.dao.RoomDAOImpl;
import com.redf.chatwebapp.dao.UserDAOImpl;
import com.redf.chatwebapp.dao.entities.UserEntity;
import com.redf.chatwebapp.dao.repo.RoomEntityRepository;
import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.dto.UserRegistrationDto;
import com.redf.chatwebapp.event.OnRegistrationCompleteEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.*;


@Controller
@RequestMapping("/signup")
public class SignupController {

    private UserService userService;
    private UserDAOImpl userDAO;
    private RoomEntityRepository roomEntityRepository;
    private RoomDAOImpl roomDAO;
    private ApplicationEventPublisher eventPublisher;


    @Contract(pure = true)
    @Autowired
    public SignupController(UserDAOImpl userDAO, UserService userService, RoomEntityRepository roomEntityRepository, RoomDAOImpl roomDAO, ApplicationEventPublisher eventPublisher) {
        setUserDAO(userDAO);
        setUserService(userService);
        setRoomEntityRepository(roomEntityRepository);
        setRoomDAO(roomDAO);
        setEventPublisher(eventPublisher);
    }


    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }


    @GetMapping
    public String signUp() {
        return "signup";
    }


    @PostMapping
    public ModelAndView registerUserAccount(HttpServletRequest request, @NotNull @ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                            BindingResult result, RedirectAttributes redirectAttributes) {
        UserEntity existing = getUserDAO().findByLogin(userDto.getLogin());
        if (existing != null)
            result.rejectValue("login", "emailExists", "Извините, но аккаунт с такой почтой уже зарегистрирован");
        if (result.hasErrors()) {
            return new ModelAndView("signup");
        }
        register(userDto);
        createAvatar(getUserService().findByLogin(userDto.getLogin()).getId());
        UserEntity registered = getUserService().findByLogin(userDto.getLogin());
        try {
            String appUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent
                    (registered, appUrl, request.getLocale()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ModelAndView("signup");
        }
        redirectAttributes.addFlashAttribute("email", registered.getLogin());
        return new ModelAndView("redirect:/confirmEmail");
    }


    private void register(UserRegistrationDto userRegistrationDto) {
        getUserService().createAndSave(userRegistrationDto);
    }


    private void createAvatar(Long id) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(new File("./media/avatars/defaultAvatar.png"));
            os = new FileOutputStream(new File("./media/avatars/avatar" + id + ".png"));
            byte[] buffer = new byte[16384];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                assert is != null;
                is.close();
                assert os != null;
                os.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
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

    @Contract(pure = true)
    private ApplicationEventPublisher getEventPublisher() {
        return eventPublisher;
    }

    private void setEventPublisher(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
}
