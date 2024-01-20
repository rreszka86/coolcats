package com.coolcats.coolcats.service;

import com.coolcats.coolcats.dto.PostDto;
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



    public void savePost(PostDto postDto)
    {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setLikes(0);
        post.setPicturePath(postDto.getPicturePath());
        post.setStatus("unverified");
        post.setUser(postDto.getUser());
        postRepository.save(post);
    }
}
