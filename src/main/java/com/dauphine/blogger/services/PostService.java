package com.dauphine.blogger.services;

import com.dauphine.blogger.exceptions.*;
import com.dauphine.blogger.models.Post;
import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> getAll();
    Post getById(UUID id) throws PostNotFoundByIdException;
    Post create(String title, String content, UUID categoryId) throws CategoryNotFoundByIdException, PostTitleAlreadyExistsException;
    Post update(UUID id, String title, String content) throws PostNotFoundByIdException;
    boolean deleteById(UUID id);
    List<Post> getAllByCategoryId(UUID categoryId);
    List<Post> getAllLikeValue(String value);
}