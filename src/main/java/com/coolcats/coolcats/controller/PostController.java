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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class PostController {
    private final PostService postService;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    public static String uploadPath = "src/main/resources/static/img/posts/";
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
    public String createPost(@ModelAttribute("post") PostDto postDto, BindingResult result, Model model, @RequestParam("image") MultipartFile file) throws IOException
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();
        User user = userRepository.findByUsername(currentUserName);
        postDto.setUser(user);

        String titleRegex = "^[a-zA-Z0-9 ]{3,60}$";
        if (!postDto.getTitle().matches(titleRegex)) {
            result.rejectValue("title", null, "Niepoprawny tytuł! (dozwolone litery, spacje i cyfry, maks. 60 znaków)");
        }

        if(file.isEmpty())
        {
            result.rejectValue("title", null, "Nie dodano pliku!");
        }

        if(result.hasErrors()){
            model.addAttribute("post", postDto);
            return "createPost";
        }

        Date dt = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String [] parts = Objects.requireNonNull(file.getContentType()).split("/");
        String newFilename = "post" + dateFormat.format(dt) + "." + parts[1];
        Path fileNameAndPath = Paths.get(uploadPath, newFilename);
        Files.write(fileNameAndPath, file.getBytes());
        postDto.setPicturePath("/img/posts/" + newFilename);
        postService.savePost(postDto);
        return "redirect:/unapprovedPosts";
    }

    @PostMapping("/profile/{userId}/delete/{postId}")
    public String deletePost(@PathVariable Long userId, @PathVariable Long postId)
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
