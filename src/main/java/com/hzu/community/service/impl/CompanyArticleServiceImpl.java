package com.hzu.community.service.impl;

import com.hzu.community.bean.Comment;
import com.hzu.community.bean.CompanyArticle;
import com.hzu.community.bean.Notification;
import com.hzu.community.bean.CompanyArticle;
import com.hzu.community.dto.ArticleExecution;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.enums.ArticleEnum;
import com.hzu.community.exceptions.ArticleException;
import com.hzu.community.mapper.CommentMapper;
import com.hzu.community.mapper.CompanyArticleMapper;
import com.hzu.community.mapper.NotificationMapper;
import com.hzu.community.service.CompanyArticleService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CompanyArticleServiceImpl implements CompanyArticleService {
    @Autowired
    private CompanyArticleMapper companyArticleMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    
    @Override
    @Transactional
    public ArticleExecution saveArticle(CompanyArticle article, ImageHolder imageHolder) throws ArticleException {
        if (article == null) {
            return new ArticleExecution(ArticleEnum.NULL_Article);
        }
        try {
                /*因为add方法中，开启了mybtis的useGeneratedKeys
                 所以成功插入后返回主键值到articleId
                 该值作为更新图片url的参数*/
            article.setCreateTime(new Date());
            int insertNum = companyArticleMapper.add(article);

            if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                throw new ArticleException("帖子数据插入失败");
            } else {
                if (imageHolder.getImage() != null) {
                    try {
//                            对图片进行处理，并把url设置在文章中
                        addArticleImg(article, imageHolder);
                    } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                        throw new ArticleException("addShopImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                    }
//                        更新图片url
                    int updateImg = companyArticleMapper.update(article);
                    if (updateImg<=0){
                        throw new ArticleException("更新图片地址失败");
                    }
                }



            }
        }catch(Exception e){
            throw new ArticleException(e.getMessage());

        }
        return new ArticleExecution(ArticleEnum.SUCCESS,article);
    }

    @Override
    @Transactional
    public ArticleExecution updateArticle(CompanyArticle article, ImageHolder imageHolder) throws ArticleException {
        try {
//            判断是否需要处理图片
            if(imageHolder != null){
//            若存在图片，则取出相应文章
                CompanyArticle oldArticle = companyArticleMapper.findArticleById(article.getId());
                if (oldArticle.getArticleImg()!= null){
//                      根据文章的图片url删除本地下载的图片
                    ImageUtil.deleteFileOrpath(oldArticle.getArticleImg());
                }
//             对图片进行处理，并把url设置在文章中
                addArticleImg(article, imageHolder);
            }

//           更新文章信息
            article.setCreateTime(new Date());
            int updateNum = companyArticleMapper.update(article);
            if (updateNum<=0){
                return new ArticleExecution(ArticleEnum.UPDATE_WRONG);
            }else {
//                重新赋值，并将更新后的数据封装在execution返回
                article = companyArticleMapper.findArticleById(article.getId());
                return new ArticleExecution(ArticleEnum.SUCCESS,article);
            }

        }catch (Exception e){
            throw new ArticleException(e.getMessage());
        }
    }

    @Override
    @Transactional
    public ArticleExecution deleteArticle(Integer id, Integer userId) throws ArticleException {
        try {
//            删除文章所在路径
            String userStr = userId.toString();
            String lostArticleStr = id.toString();
            String delePath = "/upload/item/"+userStr+"/companyArticle/"+lostArticleStr;
//            删除路径对应文件
            ImageUtil.deleteFileOrpath(delePath);

            try {
//                删除该文章下的信息通知
                Notification notification = new Notification();
                notification.setArticleId(id);
                notification.setArticleParCategory(6);
                notificationMapper.delNotification(notification);
            }catch (Exception e){
                throw new ArticleException("删除该文章的信息通知失败:"+e.getMessage());
            }

            try {
//                删除该文章评论
                Comment comment = new Comment();
                comment.setArticleId(id);
                comment.setArticleParCategory(6);
                commentMapper.deleArticleComment(comment);
            }catch (Exception e){
                throw new ArticleException("删除该文章的评论失败:"+e.getMessage());
            }

            try {
//                删除该文章
                companyArticleMapper.deleById(id);
            }catch (Exception e){
                throw new ArticleException("删除该文章失败:"+e.getMessage());
            }
            return new ArticleExecution(ArticleEnum.SUCCESS);

        }catch (Exception e){
            throw new ArticleException(e.getMessage());
        }
    }

    private void addArticleImg(CompanyArticle article, ImageHolder ArticleInputStream){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getCompanyArticleImagePath(article.getUserInfo(),article.getId());
        String ArticleAddr = ImageUtil.generateThumbnail(ArticleInputStream,dest);//处理图片。并返回图片地址
        article.setArticleImg(ArticleAddr);//设置图片地址

    }
}
