package com.coolcats.coolcats.controller;

import com.coolcats.coolcats.dto.PostDto;
import com.coolcats.coolcats.entity.Post;
import com.coolcats.coolcats.entity.User;
import com.coolcats.coolcats.repository.PostRepository;
import com.coolcats.coolcats.repository.UserRepository;
import com.coolcats.coolcats.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Autowired
    public PostController(PostService postService, UserRepository userRepository, PostRepository postRepository)
    {

        this.postService = postService;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/")
    public String showIndex(@RequestParam(value = "search", required = false) String search, Model model)
    {
        if (search!=null)
        {
            model.addAttribute("posts", postRepository.getAllByTitleContaining(search).stream()
                    .sorted((o1, o2) -> Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime()))
                    .collect(Collectors.toList()));
        }
        else
        {
            model.addAttribute("posts", postService.getAllPosts().stream()
                    .sorted((o1, o2) -> Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime()))
                    .collect(Collectors.toList()));
        }
        return "index";
    }

    @GetMapping("/unapprovedPosts")
    public String showUnapprovedPosts(@RequestParam(value = "search", required = false) String search, Model model)
    {
        if (search!=null)
        {
            model.addAttribute("posts", postRepository.getAllByTitleContaining(search).stream()
                    .sorted((o1, o2) -> Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime()))
                    .collect(Collectors.toList()));
        }
        else
        {
            model.addAttribute("posts", postService.getAllPosts().stream()
                    .sorted((o1, o2) -> Long.compare(o2.getCreatedAt().getTime(), o1.getCreatedAt().getTime()))
                    .collect(Collectors.toList()));
        }
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
        return "unapprovedPosts";
    }

    @PostMapping("/profile/{userId}/delete/{postId}")
    public String deletePost(@PathVariable Long userId, @PathVariable Long postId, Model model)
    {
        Optional<Post> optionalPost = postRepository.findById(postId);

        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            if (post.getUser().getId().equals(userId)) {
                postRepository.deleteById(postId);
            } else {
                return "redirect:/error";
            }
        } else {
            return "redirect:/error";
        }

        return "redirect:/profile/" + userId;
    }

}
