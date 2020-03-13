package com.redf.chatwebapp.config;

import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    @Qualifier(value = "messageSource")
    private MessageSource messages;
    // private LocaleResolver localeResolver;


    @Autowired
    public CustomAuthenticationFailureHandler() {
        //setLocaleResolver(localeResolver);
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
        setDefaultFailureUrl("/login.html?error=true");
        super.onAuthenticationFailure(request, response, exception);
        //Locale locale = getLocaleResolver().resolveLocale(request);
        String errorMessage = getMessages().getMessage("message.badCredentials", null, null);
        if (exception.getMessage().equalsIgnoreCase("User is disabled")) {
            errorMessage = getMessages().getMessage("auth.message.disabled", null, null);
        } else if (exception.getMessage().equalsIgnoreCase("User account has expired")) {
            errorMessage = getMessages().getMessage("auth.message.expired", null, null);
        }
        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }


    //  private void setLocaleResolver(LocaleResolver localeResolver) {
    //      this.localeResolver = localeResolver;
    //  }


    //  @Contract(pure = true)
    //  private LocaleResolver getLocaleResolver() {
    //      return localeResolver;
    //  }


    @Contract(pure = true)
    private MessageSource getMessages() {
        return messages;
    }
}
