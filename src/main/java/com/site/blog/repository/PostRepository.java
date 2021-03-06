package com.site.blog.repository;

import com.site.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findById(long id);
    List<Post> findPostByTitle(String title);
}
