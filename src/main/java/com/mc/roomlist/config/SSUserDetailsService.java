package com.mc.roomlist.config;

import com.mc.roomlist.AppUser;
import com.mc.roomlist.AppUserRepository;
import com.mc.roomlist.RoomRepository;
import com.mc.roomlist.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

    private AppUserRepository appUserRepository;

    public SSUserDetailsService(AppUserRepository repository) {
        this.appUserRepository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            AppUser user = appUserRepository.findByUsername(username);
            if(user == null)
                return null;
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), myAuthorities(user));
        } catch(Exception e){
            throw new UsernameNotFoundException("user not found");
        }
    }

    private Set<GrantedAuthority> myAuthorities(AppUser user){
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for(UserRole role: user.getRoles()){
            GrantedAuthority granted = new SimpleGrantedAuthority(role.getRole());
            authorities.add(granted);
        }
        return authorities;
    }
}
