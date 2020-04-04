package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.Comment;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Notification;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.enums.NotificationTypeEnum;
import com.hzu.community.mapper.*;
import com.hzu.community.service.CommentService;
import com.hzu.community.service.NotificationService;
import com.hzu.community.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class CommentController {

    @Autowired
    private LostArticleMapper lostArticleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private HelpArticleMapper helpArticleMapper;
    @Autowired
    private SecondArticleMapper secondArticleMapper;
    @Autowired
    private JobArticleMapper jobArticleMapper;

//    加载文章的评论，并返回评论区域代码块，供前端局部刷新
    @GetMapping("/{parCategory}/comment/{articleId}")
    public String comments(@PathVariable("parCategory") Integer parCategory,
                           @PathVariable("articleId") Integer articleId,
                           Model model, HttpServletRequest request) {

        if (parCategory.equals(1)){
            model.addAttribute("commentList", lostArticleMapper.findArticleById(articleId).getCommentList());
        }else if (parCategory.equals(2)){
            model.addAttribute("commentList", secondArticleMapper.findArticleById(articleId).getCommentList());

        }else if (parCategory.equals(3)){
            model.addAttribute("commentList", helpArticleMapper.findArticleById(articleId).getCommentList());

        }else if (parCategory.equals(4)){
            model.addAttribute("commentList", jobArticleMapper.findArticleById(articleId).getCommentList());
        }else if (parCategory.equals(5)){

        }else if (parCategory.equals(6)){

        }

//        用于判断当前评论是否属于自己，是否能操作

        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        model.addAttribute("user",user);

        return "fragments :: commentList";
    }



//    添加评论
    @PostMapping("/{parCategory}/comment")
    @ResponseBody
    public Map<String,Object> addComment(
            @PathVariable("parCategory") Integer parCategory,HttpServletRequest request){
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
//        设置comment数据并生成信息通知notification
         Notification notification = new Notification();
         getCandN(request,comment,notification,parCategory);

//        将评论插入数据库中,且新增信息通知
        try {
            int cNum = commentMapper.addComment(comment);
            int nNum = notificationService.add(notification);
            if (cNum>0 && nNum>0){
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
    @PostMapping("/{parCategory}/comment/del")
    @ResponseBody
    public Map<String,Object> delComment(@PathVariable("parCategory") Integer parCategory,
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


    private void getCandN(HttpServletRequest request, Comment comment,Notification notification,Integer parCategory) {
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        comment.setUser(user);
        Date date = new Date();
        comment.setCreateTime(date);
//        设置文章父类别
        comment.setArticleParCategory(parCategory);
//        与文章拥有者对比，判断是否为博主
        UserInfo owner = new UserInfo();
        if (parCategory.equals(1)){
            owner = lostArticleMapper.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(2)){
            owner = secondArticleMapper.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(3)){
            owner = helpArticleMapper.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(4)){
            owner = jobArticleMapper.findArticleById(comment.getArticleId()).getUserInfo();

        }else if (parCategory.equals(5)){

        }else if (parCategory.equals(6)){

        }


        if (owner.getUserId() == user.getUserId()){
            comment.setAdmin(true);
        }else {
            comment.setAdmin(false);
        }
        if (owner.getUserId() == comment.getReplyUser().getUserId()){
            comment.setReplyIsAdmin(true);
        }else {
            comment.setReplyIsAdmin(false);
        }
//        生成信息通知
        if (comment.getParentComment().getCommentId()==null){
//            无父评论，生成回复文章信息通知

//              信息发出者
            notification.setNotifier(comment.getUser());
//              信息接收者
            notification.setReceiver(owner);
//              文章id
            notification.setArticleId(comment.getArticleId());

            notification.setType(NotificationTypeEnum.REPLY_ARTICLE.getType());
            notification.setCreateTime(new Date());
            if (parCategory.equals(1)){
                notification.setOuterTitle(lostArticleMapper.findArticleById(comment.getArticleId()).getArticleTitle());
//                文章类型id
                notification.setArticleParCategory(1);
            }else if (parCategory.equals(2)){
                notification.setOuterTitle(secondArticleMapper.findArticleById(comment.getArticleId()).getArticleTitle());
                notification.setArticleParCategory(2);

            }else if (parCategory.equals(3)){
                notification.setOuterTitle(helpArticleMapper.findArticleById(comment.getArticleId()).getArticleTitle());
                notification.setArticleParCategory(3);

            }else if (parCategory.equals(4)){
                notification.setOuterTitle(jobArticleMapper.findArticleById(comment.getArticleId()).getArticleTitle());
                notification.setArticleParCategory(4);

            }else if (parCategory.equals(5)){

            }else if (parCategory.equals(6)){

            }

//             设置未读状态
            notification.setStatus(false);

        }else {
//            生成回复评论信息通知

            notification.setNotifier(comment.getUser());
            notification.setReceiver(comment.getReplyUser());
            notification.setArticleId(comment.getArticleId());
            if (parCategory.equals(1)){
//                文章类型id
                notification.setArticleParCategory(1);
            }else if (parCategory.equals(2)){
                notification.setArticleParCategory(2);

            }else if (parCategory.equals(3)){
                notification.setArticleParCategory(3);

            }else if (parCategory.equals(4)){
                notification.setArticleParCategory(4);

            }else if (parCategory.equals(5)){

            }else if (parCategory.equals(6)){

            }

            notification.setType(NotificationTypeEnum.REPLY_COMMENT.getType());
            notification.setCreateTime(new Date());
            notification.setOuterTitle(commentMapper.findCommentById(comment.getParentComment().getCommentId()).getContent());
            notification.setStatus(false);
        }
    }



}
