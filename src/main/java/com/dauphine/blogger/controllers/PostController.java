package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationPostRequest;
import com.dauphine.blogger.dto.UpdatePostRequest;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1")
public class PostController {

    private final List<Post> posts = new ArrayList<>();
    private final CategoryController categoryController;

    public PostController(CategoryController categoryController) {
        this.categoryController = categoryController;
    }

    private Post findPostOrThrow(UUID id) {
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    @PostMapping("/posts")
    public Post createPost(@RequestBody CreationPostRequest req){
        if (req.getTitle() == null || req.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
        }
        if (req.getContent() == null || req.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Content is required");
        }
        if (req.getCategoryId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CategoryId is required");
        }



        //find category by id
        Category category = categoryController.publicFindCategoryOrThrow(req.getCategoryId());

        Post post = new Post();
        post.setId(UUID.randomUUID());

        post.setTitle(req.getTitle().trim());
        post.setContent(req.getContent().trim());
        post.setCreatedDate(LocalDateTime.now());
        post.setCategory(category);

        posts.add(post);
        return post;
    }

    // Update an existing post
    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable UUID id, @RequestBody UpdatePostRequest req) {
        Post existing = findPostOrThrow(id);

        if (req.getTitle() != null && !req.getTitle().trim().isEmpty()) {
            existing.setTitle(req.getTitle().trim());
        }
        if (req.getContent() != null && !req.getContent().trim().isEmpty()) {
            existing.setContent(req.getContent().trim());
        }
        if (req.getCategoryId() != null) {
            Category category = categoryController.publicFindCategoryOrThrow(req.getCategoryId());
            existing.setCategory(category);
        }

        return existing;
    }

    // Delete an existing post
    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable UUID id) {
        Post existing = findPostOrThrow(id);
        posts.remove(existing);
    }

    // Retrieve all posts ordered by creation date (latest first)
    @GetMapping("/posts")
    public List<Post> getAllPostsLatestFirst() {
        return posts.stream()
                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }

    // Retrieve all posts per a category
    @GetMapping("/categories/{categoryId}/posts")
    public List<Post> getPostsByCategory(@PathVariable UUID categoryId) {
        // valide que la catégorie existe
        Category category = categoryController.publicFindCategoryOrThrow(categoryId);

        return posts.stream()
                .filter(p -> p.getCategory() != null && p.getCategory().getId().equals(category.getId()))
                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                .collect(Collectors.toList());
    }
}
