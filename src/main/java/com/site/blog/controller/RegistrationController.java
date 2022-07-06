package com.site.blog.controller;

import com.site.blog.model.Role;
import com.site.blog.model.User;
import com.site.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/registration")
    public String registration() {
        return "registration";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public String saveUser(User user, Map<String, Object> message) {
        User userFromDb = userRepository.findUserByUsername(user.getUsername());
        if (userFromDb != null) {
            message.put("message", "User already exists!");
            return "registration";
        }
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }


}
