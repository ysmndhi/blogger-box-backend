package com.dauphine.blogger.services.impl;

import com.dauphine.blogger.models.Category;
import com.dauphine.blogger.models.Post;
import com.dauphine.blogger.repositories.PostRepository;
import com.dauphine.blogger.services.CategoryService;
import com.dauphine.blogger.services.PostService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import com.dauphine.blogger.exceptions.*;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final CategoryService categoryService;

    public PostServiceImpl(PostRepository repository, CategoryService categoryService) {
        this.repository = repository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Post> getAll() {
        return repository.findAllByOrderByCreatedDateDesc();
    }

    @Override
    public Post getById(UUID id) throws PostNotFoundByIdException {
        return repository.findById(id)
                .orElseThrow(() -> new PostNotFoundByIdException(id));
    }

    @Override
    public Post create(String title, String content, UUID categoryId) throws CategoryNotFoundByIdException, PostTitleAlreadyExistsException {
        boolean exists = repository.findAllLikeValue(title)
                .stream()
                .anyMatch(p -> p.getTitle().equalsIgnoreCase(title));
        if (exists) {
            throw new PostTitleAlreadyExistsException(title);
        }
        Category category = categoryService.getById(categoryId);
        Post post = new Post(title, content, category);
        return repository.save(post);
    }

    @Override
    public Post update(UUID id, String title, String content) throws PostNotFoundByIdException {
        Post post = getById(id); // lance PostNotFoundByIdException si non trouvé
        post.setTitle(title);
        post.setContent(content);
        return repository.save(post);
    }

    @Override
    public boolean deleteById(UUID id) {
        repository.deleteById(id);
        return true;
    }

    @Override
    public List<Post> getAllByCategoryId(UUID categoryId) {
        return repository.findAllByCategoryIdOrderByCreatedDateDesc(categoryId);
    }

    @Override
    public List<Post> getAllLikeValue(String value) {
        return repository.findAllLikeValue(value);
    }
}