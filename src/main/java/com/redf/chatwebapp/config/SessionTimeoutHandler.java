package com.redf.chatwebapp.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionTimeoutHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        // int SESSION_TIMEOUT_IN_SECONDS = 60 * 3000;
        request.getSession().setMaxInactiveInterval(0);
    }
}
