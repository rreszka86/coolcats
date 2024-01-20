package com.coolcats.coolcats.controller;

import com.coolcats.coolcats.dto.PostDto;
import com.coolcats.coolcats.entity.User;
import com.coolcats.coolcats.repository.UserRepository;
import com.coolcats.coolcats.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;

    @Autowired
    public PostController(PostService postService, UserRepository userRepository)
    {

        this.postService = postService;
        this.userRepository = userRepository;
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
    public String showPostCreation(Model model)
    {
        PostDto post = new PostDto();
        model.addAttribute("post",post);
        return "createPost";
    }

    @PostMapping("/createPost/submit")
    public String createPost(@ModelAttribute("post") PostDto postDto, BindingResult result, Model model)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findByUsername(currentUserName);
        postDto.setUser(user);

        if(result.hasErrors()){
            model.addAttribute("post", postDto);
            return "createPost";
        }

        postService.savePost(postDto);
        return "createPost";
    }
}
