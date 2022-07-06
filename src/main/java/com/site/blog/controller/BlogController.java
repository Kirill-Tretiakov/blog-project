package com.site.blog.controller;

import com.site.blog.model.Post;
import com.site.blog.repository.PostRepository;
import com.site.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class BlogController {
    private final PostService postService;
    private final PostRepository postRepository;


    @Autowired
    public BlogController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
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
        postService.savePost(new Post(title, anons, full_text));
        return "redirect:/blog";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/blog/{id}")
    public String blogDetails(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id);
        model.addAttribute("post", post);
        return "blog-details";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/blog/{id}/edit")
    public String blogEdit(@PathVariable(value = "id") long id, Model model) {
        Post post = postRepository.findById(id);
        model.addAttribute("post", post);
        return "blog-edit";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/blog/{id}/edit")
    public String blogUpdate(@PathVariable(value = "id") long id, @RequestParam String title, @RequestParam String anons,
                             @RequestParam String full_text, Model model) {
        Post post = postRepository.findById(id);
        post.setTitle(title);
        post.setAnons(anons);
        post.setFull_text(full_text);
        postService.savePost(post);
        return "redirect:/blog";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/blog/{id}/remove")
    public String blogDelete(@PathVariable(value = "id") long id, Model model) {
        postService.deletePost(id);
        return "redirect:/blog";
    }
}
