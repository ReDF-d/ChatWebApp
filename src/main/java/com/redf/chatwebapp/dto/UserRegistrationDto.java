package com.redf.chatwebapp.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "Пароли не совпадают"),
})
public class UserRegistrationDto {

    @Pattern(regexp = "^[A-Za-z0-9_-]{3,16}$", flags = Pattern.Flag.UNICODE_CASE, message = "В имени пользователя допускаются только латинские буквы и цифры, оно не должно иметь меньше 3 символов")
    @NotEmpty(message = "Имя пользователя не может быть пусто")
    private String username;


    @NotEmpty(message = "Пароль не может не может быть пуст")
    private String password;

    @NotEmpty(message = "Введите вашу электронную почту")
    @Pattern(regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])", flags = Pattern.Flag.UNICODE_CASE, message = "Почта должна соответствовать шаблону 'example@email.com'")
    private String login;

    @NotEmpty(message = "Подтвердите пароль")
    private String confirmPassword;


    @AssertTrue(message = "Вы должны принять \"Условия пользования\"")
    private Boolean terms;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

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
