package com.mc.roomlist.config;

import com.mc.roomlist.AppUserRepository;
import com.mc.roomlist.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class AppConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SSUserDetailsService userDetailsService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private AppUserRepository appUserRepository;

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetailsService userDetailsServiceBean() throws Exception{
        return new SSUserDetailsService(appUserRepository);
    }

    @Override
    protected void configure(HttpSecurity http)throws Exception{
        http.authorizeRequests()
                .antMatchers("/", "/details", "/saveroom").permitAll()
                .antMatchers("/addroom", "/updateroom").hasAnyAuthority("ADMIN", "USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)throws Exception{
        PasswordEncoder theEncoder = encoder();
        auth.inMemoryAuthentication().withUser("DaveWolf").password(encoder().encode("beastmaster")).authorities("ADMIN")
                .and()
                .withUser("auser").password(encoder().encode("apassword")).authorities("USER")
                .and().passwordEncoder(theEncoder);
        auth
                .userDetailsService(userDetailsServiceBean());
    }
}
