package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.Comment;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Notification;
import com.hzu.community.bean.UserInfo;
import com.hzu.community.enums.NotificationTypeEnum;
import com.hzu.community.mapper.*;
import com.hzu.community.service.*;
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
    private LostArticleService lostArticleService;

    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private HelpArticleService helpArticleService;
    @Autowired
    private SecondArticleService secondArticleService;
    @Autowired
    private JobArticleService jobArticleService;
    @Autowired
    private SchoolArticleService schoolArticleService;
    @Autowired
    private CompanyArticleService companyArticleService;

//    加载文章的评论，并返回评论区域代码块，供前端局部刷新
    @GetMapping("/{parCategory}/comment/{articleId}")
    public String comments(@PathVariable("parCategory") Integer parCategory,
                           @PathVariable("articleId") Integer articleId,
                           Model model, HttpServletRequest request) {
        List<Comment> commentList = new ArrayList<>();
        commentList = commentService.findCommentListById(articleId,parCategory);
        model.addAttribute("commentList", commentList);

//        用于判断当前评论是否属于自己，是否能操作
        UserInfo user = (UserInfo) request.getSession().getAttribute("user");
        model.addAttribute("user",user);

        UserInfo articleOwner = new UserInfo();
        if (parCategory.equals(1)){
            articleOwner = lostArticleService.findArticleById(articleId).getUserInfo();
        }else if (parCategory.equals(2)){
            articleOwner = secondArticleService.findArticleById(articleId).getUserInfo();
        }else if (parCategory.equals(3)){
            articleOwner = helpArticleService.findArticleById(articleId).getUserInfo();
        }else if (parCategory.equals(4)){
            articleOwner = jobArticleService.findArticleById(articleId).getUserInfo();
        }else if (parCategory.equals(5)){
            articleOwner = schoolArticleService.findArticleById(articleId).getUserInfo();
        }else if (parCategory.equals(6)){
            articleOwner = companyArticleService.findArticleById(articleId).getUserInfo();
        }
//        用于判断文章是否属于我，能不能使用置顶操作
        model.addAttribute("articleOwner",articleOwner);
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
            int cNum = commentService.addComment(comment);
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
//    设置评论置顶与否（前端ajax已传回top值）
    @PostMapping("/{parCategory}/comment/top")
    @ResponseBody
    public Map<String,Object> top(HttpServletRequest request){
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
        try {
            commentService.update(comment);
        }catch (Exception e){
            modelmap.put("success",false);
            modelmap.put("msg",e.getMessage());
            return modelmap;
        }
        modelmap.put("success",true);
        return modelmap;
    }



//    获取评论和信息通知
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
            owner = lostArticleService.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(2)){
            owner = secondArticleService.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(3)){
            owner = helpArticleService.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(4)){
            owner = jobArticleService.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(5)){
            owner = schoolArticleService.findArticleById(comment.getArticleId()).getUserInfo();
        }else if (parCategory.equals(6)){
            owner = companyArticleService.findArticleById(comment.getArticleId()).getUserInfo();
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
                notification.setOuterTitle(lostArticleService.findArticleById(comment.getArticleId()).getArticleTitle());
//                文章类型id

            }else if (parCategory.equals(2)){
                notification.setOuterTitle(secondArticleService.findArticleById(comment.getArticleId()).getArticleTitle());


            }else if (parCategory.equals(3)){
                notification.setOuterTitle(helpArticleService.findArticleById(comment.getArticleId()).getArticleTitle());


            }else if (parCategory.equals(4)){
                notification.setOuterTitle(jobArticleService.findArticleById(comment.getArticleId()).getArticleTitle());


            }else if (parCategory.equals(5)){
                notification.setOuterTitle(schoolArticleService.findArticleById(comment.getArticleId()).getArticleTitle());

            }else if (parCategory.equals(6)){
                notification.setOuterTitle(companyArticleService.findArticleById(comment.getArticleId()).getArticleTitle());
            }
            notification.setArticleParCategory(parCategory);

//             设置未读状态
            notification.setStatus(false);

        }else {
//            生成回复评论信息通知

            notification.setNotifier(comment.getUser());
            notification.setReceiver(comment.getReplyUser());
            notification.setArticleId(comment.getArticleId());
            notification.setArticleParCategory(parCategory);
            notification.setType(NotificationTypeEnum.REPLY_COMMENT.getType());
            notification.setCreateTime(new Date());
            notification.setOuterTitle(commentService.findCommentById(comment.getParentComment().getCommentId()).getContent());
            notification.setStatus(false);
        }
    }



}
