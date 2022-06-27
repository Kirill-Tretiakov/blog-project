package com.site.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public String mainPage(Model model){
        model.addAttribute("title", "Home page");
        return "main";
    }
    @RequestMapping(method = RequestMethod.GET, value = "/about")
    public String aboutPage(Model model){
        model.addAttribute("title", "About-us");
        return "about";
    }
}
