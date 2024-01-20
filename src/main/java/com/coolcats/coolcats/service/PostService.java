package com.coolcats.coolcats.service;

import com.coolcats.coolcats.entity.Post;
import com.coolcats.coolcats.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository)
    {
        this.postRepository = postRepository;
    }
    public List<Post> getAllPosts()
    {
        return postRepository.findAll();
    }
}
