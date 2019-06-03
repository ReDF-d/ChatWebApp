package com.redf.chatwebapp.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;


@FieldMatch.List({
        @FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
})
public class UserRegistrationDto {

    @NotEmpty(message = "Username field must not be empty")
    private String login;


    @NotEmpty(message = "Password field must not be empty")
    private String password;


    @NotEmpty(message = "Password confirmation field field must not be empty")
    private String confirmPassword;


    @AssertTrue(message = "You must accept our Terms of Use to create an account")
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
