package com.hzu.community.service.impl;

import com.hzu.community.bean.*;
import com.hzu.community.mapper.*;
import com.hzu.community.service.*;
import com.hzu.community.util.ImageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleCategoryServiceImpl implements ArticleCategoryService {
    @Autowired
    private ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    private LostArticleMapper lostArticleMapper;
    @Autowired
    private SecondArticleMapper secondArticleMapper;
    @Autowired
    private HelpArticleMapper helpArticleMapper;
    @Autowired
    private JobArticleMapper jobArticleMapper;
    @Autowired
    private SchoolArticleMapper schoolArticleMapper;
    @Autowired
    private CompanyArticleMapper companyArticleMapper;
    @Autowired
    private NotificationMapper notificationMapper;
    @Autowired
    private CommentMapper commentMapper;
    @Override
    public List<ArticleCategory> getArticleCategories(@Param("parentId") Integer parentId){
        return articleCategoryMapper.getArticleCategory(parentId);
    }

    @Override
    public ArticleCategory findArticleCategoryById(Integer id) {
        return articleCategoryMapper.findArticleCategoryById(id);
    }

    @Override
    public ArticleCategory findByName(ArticleCategory articleCategory) {
        return articleCategoryMapper.findByName(articleCategory);
    }

    @Override
    public int add(ArticleCategory articleCategory) {
        return articleCategoryMapper.add(articleCategory);
    }

    @Override
    public int update(ArticleCategory articleCategory) {
        return articleCategoryMapper.update(articleCategory);
    }


    @Override
    public List<ArticleCategory> categoryList() {
        return articleCategoryMapper.categoryList();
    }

    @Override
    public List<ArticleCategory> articleTags() {
        return  articleCategoryMapper.articleTags();
    }

    @Override
    @Transactional
    public void del(Integer id) throws RuntimeException {
        try {
            ArticleCategory articleCategory = articleCategoryMapper.findArticleCategoryById(id);
//            根据分类的父id，判断操作哪种文章表
            if (articleCategory.getParentId().equals(1)){
                LostArticle lostArticle = new LostArticle();
                lostArticle.setArticleCategory(articleCategory);
                List<LostArticle> lostList = lostArticleMapper.getArticleList(lostArticle,null);
//                删除信息通知
                notificationMapper.batchDel(1,lostList);
//                删除相关评论
                commentMapper.batchDel(1,lostList);
//                删除文章
                lostArticleMapper.batchDel(lostList);
//                删除图片
                for (LostArticle article : lostList) {
                    //            删除文章所在路径
                    String userStr = article.getUserInfo().getUserId().toString();
                    String idStr = article.getId().toString();
                    String delePath = "/upload/item/"+userStr+"/lostArticle/"+idStr;
//                  删除路径对应文件
                    ImageUtil.deleteFileOrpath(delePath);
                }
            }else if (articleCategory.getParentId().equals(2)){
                SecondArticle secondArticle = new SecondArticle();
                secondArticle.setArticleCategory(articleCategory);
                List<SecondArticle> secondList = secondArticleMapper.getArticleList(secondArticle,null,null,null);
//                删除信息通知
                notificationMapper.batchDel(2,secondList);
//                删除相关评论
                commentMapper.batchDel(2,secondList);
//                删除文章
                secondArticleMapper.batchDel(secondList);
//                删除图片
                for (SecondArticle article : secondList) {
                    //            删除文章所在路径
                    String userStr = article.getUserInfo().getUserId().toString();
                    String idStr = article.getId().toString();
                    String delePath = "/upload/item/" + userStr + "/secondArticle/" + idStr;
//                  删除路径对应文件
                    ImageUtil.deleteFileOrpath(delePath);
                }

            }else if (articleCategory.getParentId().equals(3)){
                HelpArticle helpArticle = new HelpArticle();
                helpArticle.setArticleCategory(articleCategory);
                List<HelpArticle> helpList = helpArticleMapper.getArticleList(helpArticle,null);
//                删除信息通知
                notificationMapper.batchDel(3,helpList);
//                删除相关评论
                commentMapper.batchDel(3,helpList);
//                删除文章
                helpArticleMapper.batchDel(helpList);
//                删除图片
                for (HelpArticle article : helpList) {
                    //            删除文章所在路径
                    String userStr = article.getUserInfo().getUserId().toString();
                    String idStr = article.getId().toString();
                    String delePath = "/upload/item/"+userStr+"/helpArticle/"+idStr;
//                  删除路径对应文件
                    ImageUtil.deleteFileOrpath(delePath);
                }

            }else if (articleCategory.getParentId().equals(4)){
                JobArticle jobArticle = new JobArticle();
                jobArticle.setArticleCategory(articleCategory);
                List<JobArticle> jobList = jobArticleMapper.getArticleList(jobArticle,null);
//                删除信息通知
                notificationMapper.batchDel(4,jobList);
//                删除相关评论
                commentMapper.batchDel(4,jobList);
//                删除文章
                jobArticleMapper.batchDel(jobList);
//                删除图片
                for (JobArticle article : jobList) {
                    //            删除文章所在路径
                    String userStr = article.getUserInfo().getUserId().toString();
                    String idStr = article.getId().toString();
                    String delePath = "/upload/item/"+userStr+"/jobArticle/"+idStr;
//                  删除路径对应文件
                    ImageUtil.deleteFileOrpath(delePath);
                }

            }else if (articleCategory.getParentId().equals(5)){
                SchoolArticle schoolArticle = new SchoolArticle();
                schoolArticle.setArticleCategory(articleCategory);
                List<SchoolArticle> schoolList = schoolArticleMapper.getArticleList(schoolArticle,null);
//                删除信息通知
                notificationMapper.batchDel(5,schoolList);
//                删除相关评论
                commentMapper.batchDel(5,schoolList);
//                删除文章
                schoolArticleMapper.batchDel(schoolList);
//                删除图片
                for (SchoolArticle article : schoolList) {
                    //            删除文章所在路径
                    String userStr = article.getUserInfo().getUserId().toString();
                    String idStr = article.getId().toString();
                    String delePath = "/upload/item/"+userStr+"/schoolArticle/"+idStr;
//                  删除路径对应文件
                    ImageUtil.deleteFileOrpath(delePath);
                }

            }else if (articleCategory.getParentId().equals(6)){
                CompanyArticle companyArticle = new CompanyArticle();
                companyArticle.setArticleCategory(articleCategory);
                List<CompanyArticle> companyList = companyArticleMapper.getArticleList(companyArticle,null);
//                删除信息通知
                notificationMapper.batchDel(6,companyList);
//                删除相关评论
                commentMapper.batchDel(6,companyList);
//                删除文章
                companyArticleMapper.batchDel(companyList);
//                删除图片
                for (CompanyArticle article : companyList) {
                    //            删除文章所在路径
                    String userStr = article.getUserInfo().getUserId().toString();
                    String idStr = article.getId().toString();
                    String delePath = "/upload/item/"+userStr+"/companyArticle/"+idStr;
//                  删除路径对应文件
                    ImageUtil.deleteFileOrpath(delePath);
                }

            }
            articleCategoryMapper.del(id);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
