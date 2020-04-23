package com.hzu.community.service.impl;

import com.hzu.community.bean.Comment;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int addComment(Comment comment) {
        return commentMapper.addComment(comment);
    }

    @Override
    public int deleArticleComment(Comment comment) {
        return commentMapper.deleArticleComment(comment);
    }

    @Override
    public void batchDel(Integer parId, List List) {
         commentMapper.batchDel(parId,List);

    }

    @Override
    public List<Comment> findCommentReplyById(Integer commentId) {
        return commentMapper.findCommentReplyById(commentId);
    }

    @Override
    public List<Comment> findCommentListById(Integer articleId, Integer parCategory) {
        return commentMapper.findCommentListById(articleId,parCategory);
    }

    @Override
    public Comment findCommentById(Long commentId) {
        return commentMapper.findCommentById(commentId);
    }

    @Override
    @Transactional
    public boolean deleteComment(Comment comment) {
        try {
            //一级评论，需删除二级评论列表
            if (comment.getParentComment()==null){
//            删除二级列表
                commentMapper.deleCommentList(comment);
//                删除一级评论
                commentMapper.deleComment(comment);
            }else {
                commentMapper.deleComment(comment);
            }


        }catch (Exception e){
            throw new RuntimeException(e.getMessage());

        }

        return true;
    }
}
