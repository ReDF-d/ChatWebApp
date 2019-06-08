package com.redf.chatwebapp.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;


@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "Пароли не совпадают"),
})
public class UserRegistrationDto {

    @NotEmpty(message = "Имя пользователя не может быть пусто")
    private String login;


    @NotEmpty(message = "Пароль не может не может быть пуст")
    private String password;


    @NotEmpty(message = "Подтвердите пароль")
    private String confirmPassword;


    @AssertTrue(message = "Вы должны принять \"Условия пользования\"")
    private Boolean terms;


    public String getLogin() {
        return login;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }


    public Boolean getTerms() {
        return terms;
    }

    public void setTerms(Boolean terms) {
        this.terms = terms;
    }

}
