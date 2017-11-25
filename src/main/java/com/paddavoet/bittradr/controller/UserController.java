package com.paddavoet.bittradr.controller;

import com.paddavoet.bittradr.entity.UserEntity;
import com.paddavoet.bittradr.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @RequestMapping("add")
    public String addUser(@QueryParam("email")String email, @QueryParam("apiKey")String apiKey, @QueryParam("apiSecret")String apiSecret) {
        UserEntity user = userRepository.findOne(email);

        if (user != null) {
            return "User already exists";
        }

        user = new UserEntity(email, apiKey, apiSecret);
        userRepository.save(user);
        return "User added";
    }

    //TODO: An unsecured endpoint should never return API Keys
    @RequestMapping("list")
    public List<UserEntity> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users;
    }
}
