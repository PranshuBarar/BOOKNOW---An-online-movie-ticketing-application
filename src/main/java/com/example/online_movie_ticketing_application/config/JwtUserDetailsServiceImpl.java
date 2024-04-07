package com.example.online_movie_ticketing_application.config;

import com.example.online_movie_ticketing_application.Entities.UserEntity;
import com.example.online_movie_ticketing_application.EntryDtos.UserRegisterEntryDto;
import com.example.online_movie_ticketing_application.Enums.Role;
import com.example.online_movie_ticketing_application.Repository.UserRepository;
import com.example.online_movie_ticketing_application.Services.Impl.UserServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserServiceImpl UserServiceImpl;

    @Autowired
    private UserRepository userRepository;


    @Override
    public CustomUserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserEmail(userEmail);
        if(userEntity == null) {
            throw new UsernameNotFoundException("User with the following email not found: "+ userEmail);
        }
        Set<GrantedAuthority> authorities = new HashSet<>();
        if(userEntity.getRole().equals(Role.USER)){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.USER.name()));
        }
        else if(userEntity.getRole().equals(Role.ADMIN)){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.ADMIN.name()));
        }
        return new CustomUserDetails(userEntity,authorities);
    }

    public String saveUser(UserRegisterEntryDto userRegisterEntryDto) throws Exception {
        userRegisterEntryDto.setRole(Role.USER);
        return UserServiceImpl.registerUser(userRegisterEntryDto);
    }

    public String saveAdmin(UserRegisterEntryDto userRegisterEntryDto) throws Exception {
        userRegisterEntryDto.setRole(Role.ADMIN);
        return UserServiceImpl.registerUser(userRegisterEntryDto);
    }
}
