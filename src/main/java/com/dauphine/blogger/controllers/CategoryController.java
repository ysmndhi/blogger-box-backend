package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationCategoryRequest;
import com.dauphine.blogger.dto.UpdateCategoryRequest;
import com.dauphine.blogger.models.Category;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dauphine.blogger.services.CategoryService;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    // Retrieve all categories
    @GetMapping
    public ResponseEntity<List<Category>> getAll(@RequestParam(required = false) String name) {
        List<Category> categories = name == null || name.isBlank()
                ? categoryService.getAll()
                : categoryService.getAllLikeName(name);
        return ResponseEntity.ok(categories);
    }

    // Retrieve category by id
    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable UUID id) {
        return categoryService.getById(id);
    }

    // Create new category
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody CreationCategoryRequest req) {
        return categoryService.create(req.getName());
    }

    // Update category name
    @PutMapping("/{id}")
    public Category updateCategory(@PathVariable UUID id, @RequestBody UpdateCategoryRequest req) {
        return categoryService.updateName(id, req.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable UUID id) {
        categoryService.deleteById(id);
    }



}
