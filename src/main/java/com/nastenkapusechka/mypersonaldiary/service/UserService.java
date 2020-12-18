package com.nastenkapusechka.mypersonaldiary.service;

import com.nastenkapusechka.mypersonaldiary.entities.Secret;
import com.nastenkapusechka.mypersonaldiary.entities.User;

import java.util.List;

public interface UserService {

    User findByUsername(String username);

    User findById(Long id);

    User registerUser(User user);

    void deleteById(Long id);

    List<User> getAllUsers();

    List<Secret> getUsersSecrets(String username);
}
