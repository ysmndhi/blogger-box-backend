package com.dauphine.blogger.services.impl;

import com.dauphine.blogger.exceptions.CategoryNotFoundByIdException;
import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.repositories.CategoryRepository;
import com.dauphine.blogger.services.CategoryService;
import org.springframework.stereotype.Service;
import com.dauphine.blogger.exceptions.*;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<Category> getAll() {
        return repository.findAll();
    }

    @Override
    public Category getById(UUID id) throws CategoryNotFoundByIdException {
        return repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundByIdException(id));
    }

    @Override
    public Category create(String name) throws CategoryNameAlreadyExistsException {
        boolean exists = repository.findAllLikeName(name)
                .stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
        if (exists) {
            throw new CategoryNameAlreadyExistsException(name);
        }
        return repository.save(new Category(name));
    }

    @Override
    public Category updateName(UUID id, String name) throws CategoryNotFoundByIdException, CategoryNameAlreadyExistsException {
        boolean exists = repository.findAllLikeName(name)
                .stream()
                .anyMatch(c -> c.getName().equalsIgnoreCase(name));
        if (exists) {
            throw new CategoryNameAlreadyExistsException(name);
        }
        Category category = getById(id);
        category.setName(name);
        return repository.save(category);
    }

    @Override
    public boolean deleteById(UUID id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Category> getAllLikeName(String name) {
        return repository.findAllLikeName(name);
    }
}