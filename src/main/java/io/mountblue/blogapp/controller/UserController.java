package io.mountblue.blogapp.controller;

import io.mountblue.blogapp.entity.User;
import io.mountblue.blogapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/loginPage")
    public String userLoginPage(){
        return "userLogin";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){
        model.addAttribute("user",new User());
        return "userRegistration";
    }

    @PostMapping("/processNewUser")
    public String processNewUser(@ModelAttribute("user") User user){
        userService.saveUser(user);

        return "redirect:/loginPage";
    }
}
