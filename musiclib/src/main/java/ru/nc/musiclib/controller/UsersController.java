package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.nc.musiclib.services.UserModel;

@Controller
public class UsersController {
    @Autowired
    private UserModel userModel;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String getAllUsers(ModelMap model){
        model.addAttribute("usersFromServer", userModel.getAllUser());
        return "users";
    }
}
