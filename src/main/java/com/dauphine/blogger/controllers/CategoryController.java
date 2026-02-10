package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationCategoryRequest;
import com.dauphine.blogger.dto.UpdateCategoryRequest;
import com.dauphine.blogger.models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;


@RestController
@RequestMapping("/v1/categories")
public class CategoryController {
    private final List<Category> categories;

    public CategoryController(){
        categories = new ArrayList<>();
        categories.add(new Category(UUID.randomUUID(), "Productivity"));
        categories.add(new Category(UUID.randomUUID(), "Java"));
        categories.add(new Category(UUID.randomUUID(), "Daily Routine"));
    }

    // Helper: find by id
    private Category findCategoryOrThrow(UUID id) {
        return categories.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
    }

    // Retrieve all categories
    @GetMapping
    public List<Category> getCategories(){
        return categories;
    }

    // Retrieve category by id
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable UUID id) {
        return findCategoryOrThrow(id);
    }

    // Create new category
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody CreationCategoryRequest req) {
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }

        Category c = new Category(UUID.randomUUID(), req.getName().trim());
        categories.add(c);
        return c;
    }

    // Update category name
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable UUID id, @RequestBody UpdateCategoryRequest req) {
        if (req.getName() == null || req.getName().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category name is required");
        }

        Category existing = findCategoryOrThrow(id);
        existing.setName(req.getName().trim());
        return existing;
    }

    // Delete category
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        Category existing = findCategoryOrThrow(id);
        categories.remove(existing);
    }

    // Exposé pour PostController (trouver la catégorie)
    public Category publicFindCategoryOrThrow(UUID id) {
        return findCategoryOrThrow(id);
    }



}
