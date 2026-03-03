package com.dauphine.blogger.controllers;

import com.dauphine.blogger.dto.CreationCategoryRequest;
import com.dauphine.blogger.dto.UpdateCategoryRequest;
import com.dauphine.blogger.exceptions.*;
import com.dauphine.blogger.models.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.dauphine.blogger.services.CategoryService;
import java.net.URI;

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
    public ResponseEntity<Category> getById(@PathVariable UUID id) throws CategoryNotFoundByIdException {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CreationCategoryRequest req)
            throws CategoryNameAlreadyExistsException {
        Category category = categoryService.create(req.getName());
        return ResponseEntity
                .created(URI.create("v1/categories/" + category.getId()))
                .body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Category> updateName(@PathVariable UUID id, @RequestBody UpdateCategoryRequest req)
            throws CategoryNotFoundByIdException, CategoryNameAlreadyExistsException {
        return ResponseEntity.ok(categoryService.updateName(id, req.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable UUID id) throws CategoryNotFoundByIdException {
        return ResponseEntity.ok(categoryService.deleteById(id));
    }

}
