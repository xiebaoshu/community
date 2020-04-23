package com.hzu.community.service.impl;

import com.hzu.community.bean.Comment;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.bean.Notification;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.mapper.LostArticleMapper;
import com.hzu.community.mapper.NotificationMapper;
import com.hzu.community.service.LostArticleService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LostArticleServiceImpl implements LostArticleService {
    @Autowired
    LostArticleMapper lostArticleMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    NotificationMapper notificationMapper;

    @Override
    public List<LostArticle> getArticleList(LostArticle articleCondition, Integer dateCondition) {
        return lostArticleMapper.getArticleList(articleCondition,dateCondition);
    }

    @Override
    public Integer searchCount(LostArticle articleCondition, Integer dateCondition) {
        return lostArticleMapper.searchCount(articleCondition,dateCondition);
    }

    @Override
    public LostArticle findArticleById(Integer articleId) {
        return lostArticleMapper.findArticleById(articleId);
    }

    @Override
    public void incReadCount(LostArticle lostArticle) {
        lostArticleMapper.incReadCount(lostArticle);
    }

    @Override
    public void batchDel(List List) {
        lostArticleMapper.batchDel(List);
    }

    @Override
    @Transactional
    public ArticleEnum saveArticle(LostArticle lostArticle, ImageHolder imageHolder) {
        if (lostArticle == null) {
            return  ArticleEnum.NULL_Article;
        }
        try {
                /*因为addLostArticle方法中，开启了mybtis的useGeneratedKeys
                 所以成功插入后返回主键值到id
                 该值作为更新图片url的参数*/
                int insertNum = lostArticleMapper.addLostArticle(lostArticle);

                if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                    throw new ArticleException("帖子数据插入失败");
                } else {
                    if (imageHolder != null) {
                        try {
//                            对图片进行处理，并把url设置在文章中
                            addArticleImg(lostArticle, imageHolder);
                        } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                            throw new ArticleException("addShopImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                        }
//                        更新图片url
                        int updateImg = lostArticleMapper.updatelost(lostArticle);
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

    @Override
    @Transactional
    public ArticleEnum updateArticle(LostArticle lostArticle, ImageHolder imageHolder) {
        try {
//            判断是否需要处理图片
            if(imageHolder != null){
//            若存在图片，则取出相应文章
              LostArticle oldArticle = lostArticleMapper.findArticleById(lostArticle.getId());
              if (oldArticle.getArticleImg()!= null){
//                      根据文章的图片url删除本地下载的图片
                    ImageUtil.deleteFileOrpath(oldArticle.getArticleImg());
              }
//             对图片进行处理，并把url设置在文章中,因为图片需要userId作为路径，所以重新赋值user
                lostArticle.setUserInfo(oldArticle.getUserInfo());
              addArticleImg(lostArticle, imageHolder);
            }
//            更新文章信息
            int updateNum = lostArticleMapper.updatelost(lostArticle);
            if (updateNum<=0){
                return ArticleEnum.UPDATE_WRONG;
            }else {
                return ArticleEnum.SUCCESS;
            }

        }catch (Exception e){
            throw new ArticleException(e.getMessage());
        }

    }
    @Transactional
    @Override
    public ArticleEnum deleteArticle(Integer id, Integer userId) throws ArticleException {
        try {


            try {
//                删除该文章下的信息通知
                Notification notification = new Notification();
                notification.setArticleId(id);
                notification.setArticleParCategory(1);
                notificationMapper.delNotification(notification);
            }catch (Exception e){
                throw new ArticleException("删除该文章的信息通知失败:"+e.getMessage());
            }

            try {
//                删除该文章评论
                Comment comment = new Comment();
                comment.setArticleId(id);
                comment.setArticleParCategory(1);
                commentMapper.deleArticleComment(comment);
            }catch (Exception e){
                throw new ArticleException("删除该文章的评论失败:"+e.getMessage());
            }

            try {
//                删除该文章
                lostArticleMapper.deleById(id);
            }catch (Exception e){
                throw new ArticleException("删除该文章失败:"+e.getMessage());
            }
            //            删除文章所在路径
            String userStr = userId.toString();
            String idStr = id.toString();
            String delePath = "/upload/item/"+userStr+"/lostArticle/"+idStr;
//            删除路径对应文件
            ImageUtil.deleteFileOrpath(delePath);
            return ArticleEnum.SUCCESS;

        }catch (Exception e){
            throw new ArticleException(e.getMessage());
        }

    }

    private void addArticleImg(LostArticle lostArticle, ImageHolder lostArticleInputStream){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getLostArticleImagePath(lostArticle.getUserInfo(),lostArticle.getId());
        String ArticleAddr = ImageUtil.generateThumbnail(lostArticleInputStream,dest);//处理图片。并返回图片地址
        lostArticle.setArticleImg(ArticleAddr);//设置图片地址

    }






}

