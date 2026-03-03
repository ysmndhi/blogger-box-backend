package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationPostRequest;
import com.dauphine.blogger.dto.UpdatePostRequest;
import com.dauphine.blogger.models.Post;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.dauphine.blogger.services.PostService;

import java.util.*;



@RestController
@RequestMapping("/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody CreationPostRequest req) {
        return postService.create(req.getTitle(), req.getContent(), req.getCategoryId());
    }

    // Update an existing post
    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable UUID id, @RequestBody UpdatePostRequest req) {
        return postService.update(id, req.getTitle(), req.getContent()); // plus de categoryId
    }

    // Delete an existing post
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable UUID id) {
        postService.deleteById(id); // ← était delete(id)
    }

    // Retrieve all posts
    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.getAll();
    }

    // Retrieve all posts per a category
    @GetMapping("/categories/{categoryId}/posts")
    public List<Post> getPostsByCategory(@PathVariable UUID categoryId) {
        return postService.getAllByCategoryId(categoryId);
    }
}
