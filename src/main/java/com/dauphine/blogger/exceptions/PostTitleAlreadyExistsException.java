package com.dauphine.blogger.exceptions;

public class PostTitleAlreadyExistsException extends Exception {
    public PostTitleAlreadyExistsException(String title) {
        super("Post with title '" + title + "' already exists");
    }
}
