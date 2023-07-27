package com.example.tfg.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.model.User;
import com.example.tfg.model.userRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private userRepository studentRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) {
        User student = studentRepository.findAllByEmail(email);
        if (student == null) {
            throw new UsernameNotFoundException(email);
        }
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        if(student.getEmail() == "teacher@teacher.com"){     
        grantedAuthorities.add(new SimpleGrantedAuthority(student.getRole()));
        } else {
            grantedAuthorities.add(new SimpleGrantedAuthority(student.getRole()));
        }

        return new org.springframework.security.core.userdetails.User(
            student.getEmail(), student.getPassword_en(), grantedAuthorities);
    }
}
