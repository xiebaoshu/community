package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzu.community.bean.Comment;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.mapper.LostArticleMapper;
import com.hzu.community.service.CommentService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CommentController {

    @Autowired
    private LostArticleMapper lostArticleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;


//    加载文章的评论，并返回评论区域代码块，供前端局部刷新
    @GetMapping("/lost/comment/{articleId}")
    public String comments(@PathVariable Integer articleId, Model model, HttpServletRequest request) {
        model.addAttribute("commentList", lostArticleMapper.findArticleById(articleId).getCommentList());
//        用于判断当前评论是否属于自己，是否能操作

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        model.addAttribute("user",user);
        return "article-detail :: commentList";
    }



//    添加评论
    @PostMapping("/lost/comment")
    @ResponseBody
    public Map<String,Object> addComment(
            HttpServletRequest request){
        Map<String,Object> modelmap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Comment comment = null;
        String commentStr = HttpServletRequestUtil.getString(request, "commentStr");
        try {
            comment = mapper.readValue(commentStr, Comment.class);
        }catch (Exception e){

            modelmap.put("success",false);
            modelmap.put("msg",e.getMessage());
            return modelmap;
        }
//        封装数据并判断是不是博主

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        comment.setUser(user);
        Date date = new Date();
        comment.setCreateTime(date);

        Integer ownerId = lostArticleMapper.findArticleById(comment.getArticleId()).getUserInfo().getUserId();
        if (ownerId == user.getUserId()){
            comment.setAdmin(true);
        }else {
            comment.setAdmin(false);
        }
        if (ownerId == comment.getReplyUser().getUserId()){
            comment.setReplyIsAdmin(true);
        }else {
            comment.setReplyIsAdmin(false);
        }
//        将评论插入数据库中
        try {
            int num = commentMapper.addComment(comment);
            if (num>0){
                modelmap.put("success",true);
            }

        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("msg",e.getMessage());
            return modelmap;
        }

        return modelmap;

    }

//    删除评论
    @PostMapping("/lost/comment/del")
    @ResponseBody
    public Map<String,Object> delComment(
            HttpServletRequest request){
        Map<String,Object> modelmap = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        Comment comment = null;
        String commentStr = HttpServletRequestUtil.getString(request, "commentStr");
        try {
            comment = mapper.readValue(commentStr, Comment.class);
        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("msg",e.getMessage());
            return modelmap;
        }


//        尝试删除评论
        try {
            boolean b = commentService.deleteComment(comment);
            if (b){
                modelmap.put("success",true);
            }

        }catch (RuntimeException e){
            modelmap.put("success",false);
            modelmap.put("msg",e.getMessage());
            return modelmap;
        }

        return modelmap;

    }

}
