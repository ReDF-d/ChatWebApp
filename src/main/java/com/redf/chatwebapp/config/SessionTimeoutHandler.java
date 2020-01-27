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

    private final Integer SESSION_TIMEOUT_IN_SECONDS = 60 * 300;

    @Override
    public void onAuthenticationSuccess(@NotNull HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        request.getSession().setMaxInactiveInterval(SESSION_TIMEOUT_IN_SECONDS);
    }
}
