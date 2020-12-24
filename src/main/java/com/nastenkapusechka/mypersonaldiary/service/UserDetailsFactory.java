package com.nastenkapusechka.mypersonaldiary.service;

import com.nastenkapusechka.mypersonaldiary.entities.Role;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.service.impl.UserDetailsImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public final class UserDetailsFactory {

    public static UserDetailsImpl getUser(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getFirstName(),
                user.getLastName(),
                user.getGender(),
                user.getRegistrationDate(),
                user.getSecretList(),
                user.isActivated(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    private static List<GrantedAuthority> mapRolesToAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
