package com.redf.chatwebapp.config;

import com.redf.chatwebapp.dao.services.UserService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@ComponentScan("com.redf.chatwebapp.dao.services")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {


    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(UserService.getInstance()).passwordEncoder(passwordEncoder());
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest()
                .hasAnyRole("ADMIN", "USER")
                .and()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginAction")
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/chat")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/home")
                .permitAll()
                .and()
                .authorizeRequests()
                .antMatchers("/", "/home", "/signup")
                .permitAll()
                .and()
                .csrf()
                .disable();
    }
}