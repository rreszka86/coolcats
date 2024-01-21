package com.coolcats.coolcats.repository;

import com.coolcats.coolcats.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Long deleteById(long id);
    List<Post> getAllByTitle(String title);
    List<Post> getAllByTitleContaining(String title);
}
