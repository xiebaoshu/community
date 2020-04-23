package com.hzu.community.service.impl;

import com.hzu.community.bean.Comment;
import com.hzu.community.bean.HelpArticle;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Notification;

import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.mapper.HelpArticleMapper;

import com.hzu.community.mapper.NotificationMapper;
import com.hzu.community.service.HelpArticleService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class HelpArticleServiceImpl implements HelpArticleService {
    @Autowired
    private HelpArticleMapper helpArticleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public List<HelpArticle> getArticleList(HelpArticle articleCondition, Integer dateCondition) {
       return helpArticleMapper.getArticleList(articleCondition,dateCondition);
    }

    @Override
    public Integer searchCount(HelpArticle articleCondition, Integer dateCondition) {
       return   helpArticleMapper.searchCount(articleCondition,dateCondition);
    }

    @Override
    public HelpArticle findArticleById(Integer articleId) {
       return helpArticleMapper.findArticleById(articleId);
    }

    @Override
    public void incReadCount(HelpArticle article) {
        helpArticleMapper.incReadCount(article);
    }

    @Override
    public void batchDel(List List) {
        helpArticleMapper.batchDel(List);
    }

    @Override
    @Transactional
    public ArticleEnum saveArticle(HelpArticle article, ImageHolder imageHolder) throws ArticleException {
        if (article == null) {
            return ArticleEnum.NULL_Article;
        }
        try {
                /*因为add方法中，开启了mybtis的useGeneratedKeys
                 所以成功插入后返回主键值到articleId
                 该值作为更新图片url的参数*/
            article.setCreateTime(new Date());
            article.setEditTime(new Date());
            int insertNum = helpArticleMapper.add(article);

            if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                throw new ArticleException("帖子数据插入失败");
            } else {
                if (imageHolder != null) {
                    try {
//                            对图片进行处理，并把url设置在文章中
                        addArticleImg(article, imageHolder);
                    } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                        throw new ArticleException("addShopImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                    }
//                        更新图片url
                    int updateImg = helpArticleMapper.update(article);
                    if (updateImg<=0){
                        throw new ArticleException("更新图片地址失败");
                    }
                }



            }
        }catch(Exception e){
            throw new ArticleException(e.getMessage());

        }
        return ArticleEnum.SUCCESS;

    }
    @Transactional
    @Override
    public ArticleEnum updateArticle(HelpArticle article, ImageHolder imageHolder) throws ArticleException {
        try {
//            判断是否需要处理图片
            if(imageHolder != null){
//            若存在图片，则取出相应文章
                HelpArticle oldArticle = helpArticleMapper.findArticleById(article.getId());
                if (oldArticle.getArticleImg()!= null){
//                      根据文章的图片url删除本地下载的图片
                    ImageUtil.deleteFileOrpath(oldArticle.getArticleImg());
                }
//             对图片进行处理，并把url设置在文章中,因为图片需要userId作为路径，所以重新赋值user
                article.setUserInfo(oldArticle.getUserInfo());
                addArticleImg(article, imageHolder);
            }

//           更新文章信息
            article.setEditTime(new Date());
            int updateNum = helpArticleMapper.update(article);
            if (updateNum<=0){
                return ArticleEnum.UPDATE_WRONG;
            }else {
//
                return ArticleEnum.SUCCESS;
            }

        }catch (Exception e){
            throw new ArticleException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ArticleEnum deleteArticle(Integer id, Integer userId) throws ArticleException {
        try {


            try {
//                删除该文章下的信息通知
                Notification notification = new Notification();
                notification.setArticleId(id);
                notification.setArticleParCategory(3);
                notificationMapper.delNotification(notification);
            }catch (Exception e){
                throw new ArticleException("删除该文章的信息通知失败:"+e.getMessage());
            }

            try {
//                删除该文章评论
                Comment comment = new Comment();
                comment.setArticleId(id);
                comment.setArticleParCategory(3);
                commentMapper.deleArticleComment(comment);
            }catch (Exception e){
                throw new ArticleException("删除该文章的评论失败:"+e.getMessage());
            }

            try {
//                删除该文章
                helpArticleMapper.deleById(id);
            }catch (Exception e){
                throw new ArticleException("删除该文章失败:"+e.getMessage());
            }
            //            删除文章所在路径
            String userStr = userId.toString();
            String idStr = id.toString();
            String delePath = "/upload/item/"+userStr+"/helpArticle/"+idStr;
//            删除路径对应文件
            ImageUtil.deleteFileOrpath(delePath);
            return ArticleEnum.SUCCESS;

        }catch (Exception e){
            throw new ArticleException(e.getMessage());
        }
    }

    private void addArticleImg(HelpArticle article, ImageHolder ArticleInputStream){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getHelpArticleImagePath(article.getUserInfo(),article.getId());
        String ArticleAddr = ImageUtil.generateThumbnail(ArticleInputStream,dest);//处理图片。并返回图片地址
        article.setArticleImg(ArticleAddr);//设置图片地址

    }
}
