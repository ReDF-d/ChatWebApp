package com.redf.chatwebapp.config;

import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.security.BcryptPasswordEncoder;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.session.HttpSessionEventPublisher;


@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    private UserService userService;
    private SessionTimeoutHandler sessionTimeoutHandler;


    @Autowired
    WebSecurityConfig(UserService userService, SessionTimeoutHandler sessionTimeoutHandler) {
        this.userService = userService;
        this.sessionTimeoutHandler = sessionTimeoutHandler;
    }


    @Override
    protected void configure(@NotNull AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(BcryptPasswordEncoder.passwordEncoder());
    }


    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }


    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }


    @Override
    protected void configure(@NotNull HttpSecurity http) throws Exception {
        http.sessionManagement().maximumSessions(1).sessionRegistry(sessionRegistry());
        http.headers().frameOptions().disable();
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler(sessionTimeoutHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/chats")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/home", "/signup", "/login", "/user/{user}", "/forgotPasswordEmailSent", "/forgotPassword", "/changeForgotPassword/{token}", "/registrationConfirm", "/confirmEmail")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/chat/**", "/createChat", "/chats", "/search", "/friends")
                .hasAnyAuthority("USER", "ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers("/actuator/**")
                .hasAuthority("ADMIN")
                .and()
                .authorizeRequests()
                .antMatchers("/adminpanel/**")
                .hasAuthority("ADMIN")
                .and()
                .rememberMe().key("uniqueAndSecret")
                .and()
                .sessionManagement()
                .sessionFixation().migrateSession()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authorizeRequests()
                .antMatchers("/ws/**", "/notificationHub/**")
                .permitAll()
                .and()
                .csrf()
                .ignoringAntMatchers("/ws/**", "/notificationHub/**");
    }
}