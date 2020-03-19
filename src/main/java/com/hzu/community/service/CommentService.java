package com.hzu.community.service;

import com.hzu.community.bean.Comment;

public interface CommentService {
    public boolean deleteComment(Comment comment) throws RuntimeException;
}
