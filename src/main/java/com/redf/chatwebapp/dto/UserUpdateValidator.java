package com.redf.chatwebapp.dto;

import com.redf.chatwebapp.dao.utils.UserDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.BindingResult;

public interface UserUpdateValidator {

    boolean validateEmail(@NotNull UserUpdateDto userUpdateDto, UserDetails userDetails);

    boolean validateUsername(@NotNull UserUpdateDto userUpdateDto, UserDetails userDetails);

    void checkPassword(BindingResult result, @NotNull UserUpdateDto userDto, UserDetails userDetails);

    void validateAllFields(BindingResult result, UserUpdateDto userUpdateDto, UserDetails userDetails, String mode);

    void saveAvatar(@NotNull UserUpdateDto userUpdateDto);
}
