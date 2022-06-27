package com.site.blog.service;

import com.site.blog.model.Post;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IPostService {
    Post savePost(Post post);

    List<Post> getAllPosts();

    Post getPostById(long id);

    void deletePost(long id);

    Post updatePost(long id, Post post);

}
