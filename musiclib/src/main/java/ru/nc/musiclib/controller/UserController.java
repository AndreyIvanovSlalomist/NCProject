package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.RoleDao;
import ru.nc.musiclib.services.UserModel;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class UserController {
    @Autowired
    private UserModel userModel;
    @Autowired
    private RoleDao roleDao;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String getUsers(ModelMap model, @RequestParam(name = "user_name", required = false, defaultValue = "") String userName,
                           RedirectAttributes redirectAttributes ) {
        if (!userName.equals("")) {
            User user = userModel.findUser(userName);
            if(user!=null){
                model.addAttribute("user", user);
                model.addAttribute("rolesFromServer", userModel.getAllRole());
                return "user";
            }
            redirectAttributes.addFlashAttribute("type","warning");
            redirectAttributes.addFlashAttribute("message","Пользователя с таким именем не существует!");
        }
        return "redirect:/users";
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public String setRole(ModelMap model, @RequestParam(name = "id") Integer id, @RequestParam(name = "id_role") Integer idRole,RedirectAttributes redirectAttributes) {
        Optional<Role> role = roleDao.find(idRole);
        role.ifPresent(value -> userModel.setRole(userModel.findUser(id).getUserName(), value));
        redirectAttributes.addFlashAttribute("type","success");
        redirectAttributes.addFlashAttribute("message","Роль пользователя "+userModel.findUser(id).getUserName()+" успешно изменена!");
        return "redirect:/users";
    }

    @PostMapping (value="/user/{username}/delete")
    public String delete(@PathVariable("username") String username, RedirectAttributes redirectAttributes){
        if(userModel.delete(username)){
            redirectAttributes.addFlashAttribute("type","success");
            redirectAttributes.addFlashAttribute("message","Пользователь "+username+" успешно удален!");
        }
        return "redirect:/users";
    }


}
