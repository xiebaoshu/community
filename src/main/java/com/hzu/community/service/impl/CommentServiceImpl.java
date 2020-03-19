package com.hzu.community.service.impl;

import com.hzu.community.bean.Comment;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;


    @Override
    @Transactional
    public boolean deleteComment(Comment comment) {
        try {
            //            一级评论，需删除二级评论列表
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
