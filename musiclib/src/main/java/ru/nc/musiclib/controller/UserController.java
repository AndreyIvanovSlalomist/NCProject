package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nc.musiclib.services.UserModel;

@Controller
public class UserController {
    @Autowired
    private UserModel userModel;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUsers(ModelMap model, @RequestParam(name = "user_name", required = false, defaultValue = "") String userName, @RequestParam(name = "user_name_delete", required = false, defaultValue = "") String userNameDelete) {
        if (!userName.equals("")) {
            model.addAttribute("user", userModel.findUser(userName));
            return "user";
        }
        if (!userNameDelete.equals("")) {
            userModel.delete(userNameDelete);
        }
        return "redirect:/users";
    }
}
