package com.nastenkapusechka.mypersonaldiary.service.impl;

import com.nastenkapusechka.mypersonaldiary.entities.Role;
import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;
import com.nastenkapusechka.mypersonaldiary.repo.RoleRepository;
import com.nastenkapusechka.mypersonaldiary.repo.SecretRepository;
import com.nastenkapusechka.mypersonaldiary.repo.UserRepository;
import com.nastenkapusechka.mypersonaldiary.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final SecretRepository secretRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, SecretRepository secretRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.secretRepository = secretRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findByUsername(String username) {

        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            log.warn("IN findByUsername - user with username: {} not found", username);
            return null;
        }
        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("IN findByUsername - user with id: {} not found", id);
            return null;
        }
        return user;
    }

    @Override
    public User registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrationDate(LocalDate.now());
        user.setFirstName(user.getFirstName().trim());
        user.setLastName(user.getLastName().trim());
        user.setUsername(user.getUsername().trim());
        user.setActivationCode(UUID.randomUUID().toString());

        List<Role> roles = new ArrayList<>();
        Role role = roleRepository.findByName("ROLE_USER");
        roles.add(role);

        user.setRoles(roles);

        User result = userRepository.save(user);
        log.info("IN registerUser - user: {} successfully registered", result);
        return result;
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
        log.info("IN deleteById - user with id {} successfully deleted", id);
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = (List<User>) userRepository.findAll();
        log.info("IN getAllUsers - {} users founded", users.size());
        return users;
    }

    @Override
    public List<Secret> getUsersSecrets(String username) {
        return secretRepository.findByUserUsername(username);
    }

    @Override
    public boolean activateUser(String code) {
        User user = userRepository.findByActivationCode(code);
        if (user != null) {
            log.info("IN activateUser - user with email: {} activated!", user.getUsername());
            user.setActivationCode(null);
            user.setActivated(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
