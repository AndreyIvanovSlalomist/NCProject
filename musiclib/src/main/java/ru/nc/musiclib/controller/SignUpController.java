package ru.nc.musiclib.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.nc.musiclib.forms.UserForm;
import ru.nc.musiclib.services.SignUpService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SignUpController {

    @Autowired
    private SignUpService service;

    @GetMapping("/signUp")
    public String getSignUpPage(Authentication authentication, ModelMap model, HttpServletRequest request){
        if (authentication != null){
            return "redirect:/";
        }
        if (request.getParameterMap().containsKey("error")){
            model.addAttribute("error", true);
        }
        return "registration";
    }

    @PostMapping("/signUp")
    public String signUp(UserForm userForm, ModelMap model){
        if (service.signUp(userForm)) {
            return "redirect:/signIn";
        }else {

            model.addAttribute("error", true);
            return "signUp";
        }

    }


}
