package com.coolcats.coolcats.controller;

import com.coolcats.coolcats.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    private final PostService postService;
    
    @Autowired
    public PostController(PostService postService)
    {
        this.postService = postService;
    }

    @GetMapping("/")
    public String showIndex(Model model)
    {
        model.addAttribute("posts", postService.getAllPosts());
        return "index";
    }

    @GetMapping("/unapprovedPosts")
    public String showUnapprovedPosts()
    {
        return "unapprovedPosts";
    }

    @GetMapping("/createPost")
    public String showPostCreation()
    {
        return "createPost";
    }

    @PostMapping("/createPost/submit")
    public String sendPost()
    {
        return "createPost";
    }
}
