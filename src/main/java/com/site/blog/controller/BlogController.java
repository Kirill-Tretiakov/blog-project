package com.site.blog.controller;

import com.site.blog.model.Post;
import com.site.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BlogController {
    private final PostService postService;

    @Autowired
    public BlogController(PostService postService) {
        this.postService = postService;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/blog")
    public String getPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        model.addAttribute("posts", posts);
        return "blog-main";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/blog/add")
    public String addPosts(@RequestParam String title, @RequestParam String anons,
                           @RequestParam String full_text, Model model) {
        postService.savePost(new Post(title,anons, full_text));
        return "redirect:/blog";
    }
}
