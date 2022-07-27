package com.site.blog.controller;

import com.site.blog.model.Post;
import com.site.blog.model.User;
import com.site.blog.repository.PostRepository;
import com.site.blog.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;


@Controller
public class BlogController {
    private final PostService postService;
    private final PostRepository postRepository;

    @Value("${upload.path}")
    private String uploadPath;


    @Autowired
    public BlogController(PostService postService, PostRepository postRepository) {
        this.postService = postService;
        this.postRepository = postRepository;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/blog/add")
    public String blogAdd(Model model) {
        return "blog-add";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/blog/add")
    public String addPosts(@AuthenticationPrincipal User user,
                           @RequestParam String title, @RequestParam String anons,
                           @RequestParam String full_text, Map<String, Object> model,
                           @RequestParam("file") MultipartFile file,
                           RedirectAttributes redirectAttributes) {
        Post post = new Post(title, anons, full_text, user);
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);
            if (uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();
            try {
                file.transferTo(new File(uploadPath + "/" + resultFileName));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            post.setFileName(resultFileName);
        }
        postService.savePost(post);
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

    @RequestMapping(method = RequestMethod.GET, value = "/blog")
    public String getPostsViaFilter(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
        Iterable<Post> posts = postRepository.findAll();
        if (filter != null && !filter.isEmpty()) {
            posts = postRepository.findPostByTitle(filter);
        } else {
            posts = postRepository.findAll();
        }
        model.addAttribute("posts", posts);
        model.addAttribute("filter", filter);
        return "blog-main";
    }
}
