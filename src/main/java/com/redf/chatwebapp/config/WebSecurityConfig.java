package com.redf.chatwebapp.config;

import com.redf.chatwebapp.dao.services.UserService;
import com.redf.chatwebapp.security.BcryptPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(UserService.getInstance()).passwordEncoder(BcryptPasswordEncoder.passwordEncoder());
        auth.authenticationProvider(authenticationProvider());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/chat")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/terms")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/home", "/signup")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/chat")
                .hasAnyAuthority("USER", "ADMIN");
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(UserService.getInstance());
        auth.setPasswordEncoder(BcryptPasswordEncoder.passwordEncoder());
        return auth;
    }


    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**").anyRequest();
    }

}