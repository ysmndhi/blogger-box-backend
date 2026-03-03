package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationPostRequest;
import com.dauphine.blogger.dto.UpdatePostRequest;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.exceptions.*;
import java.net.URI;
import org.springframework.http.ResponseEntity;
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

    // Retrieve all posts
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAll(@RequestParam(required = false) String value) {
        List<Post> posts = value == null || value.isBlank()
                ? postService.getAll()
                : postService.getAllLikeValue(value);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<Post> getById(@PathVariable UUID id) throws PostNotFoundByIdException {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping("/posts")
    public ResponseEntity<Post> create(@RequestBody CreationPostRequest req)
            throws CategoryNotFoundByIdException, PostTitleAlreadyExistsException {
        Post post = postService.create(req.getTitle(), req.getContent(), req.getCategoryId());
        return ResponseEntity
                .created(URI.create("v1/posts/" + post.getId()))
                .body(post);
    }

    @PutMapping("/posts/{id}")
    public ResponseEntity<Post> update(@PathVariable UUID id, @RequestBody UpdatePostRequest req) throws PostNotFoundByIdException, CategoryNotFoundByIdException {
        return ResponseEntity.ok(postService.update(id, req.getTitle(), req.getContent()));
    }

    @DeleteMapping("/posts/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) throws PostNotFoundByIdException {
        return ResponseEntity.ok(postService.deleteById(id));
    }

    @GetMapping("/categories/{categoryId}/posts")
    public ResponseEntity<List<Post>> getByCategory(@PathVariable UUID categoryId) {
        return ResponseEntity.ok(postService.getAllByCategoryId(categoryId));
    }

}
