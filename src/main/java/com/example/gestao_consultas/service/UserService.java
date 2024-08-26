package com.example.gestao_consultas.service;

import com.example.gestao_consultas.model.LoginUser;
import com.example.gestao_consultas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public boolean authenticate(String username, String password) {
        Optional<LoginUser> user = userRepository.findByUsername(username);
        return user.isPresent() && user.get().getPassword().equals(password);
    }

    public void saveUser(LoginUser loginUser) {
        userRepository.save(loginUser);
    }

}
