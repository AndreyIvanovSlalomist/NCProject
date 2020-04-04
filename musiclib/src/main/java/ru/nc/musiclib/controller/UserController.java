package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.repositories.RoleDao;
import ru.nc.musiclib.services.UserModel;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserModel userModel;
    @Autowired
    private RoleDao roleDao;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUsers(ModelMap model, @RequestParam(name = "user_name", required = false, defaultValue = "") String userName, @RequestParam(name = "user_name_delete", required = false, defaultValue = "") String userNameDelete) {
        if (!userName.equals("")) {
            model.addAttribute("user", userModel.findUser(userName));
            model.addAttribute("rolesFromServer", userModel.getAllRole());
            return "user";
        }
        if (!userNameDelete.equals("")) {
            userModel.delete(userNameDelete);
        }
        return "redirect:/users";
    }


    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String setRole(ModelMap model, @RequestParam(name = "id") Integer id, @RequestParam(name = "id_role") Integer idRole) {
        Optional<Role> role = roleDao.find(idRole);
        role.ifPresent(value -> userModel.setRole(userModel.findUser(id).getUserName(), value));
        return "redirect:/users";
    }

}
