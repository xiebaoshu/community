package com.hzu.community.service.impl;

import com.hzu.community.bean.*;
import com.hzu.community.mapper.*;
import com.hzu.community.service.LostArticleService;
import com.hzu.community.service.ModifyUserService;
import com.hzu.community.util.ImageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ModifyUserServiceImpl implements ModifyUserService {
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
    @Transactional
    public Boolean delAll(Integer userId) throws RuntimeException {
        try {
//            获取用户
            UserInfo user = new UserInfo();
            user.setUserId(userId);
            String userStr = userId.toString();
//            查询条件
            LostArticle lostArticle = new LostArticle();
            lostArticle.setUserInfo(user);
            SecondArticle secondArticle = new SecondArticle();
            secondArticle.setUserInfo(user);
            HelpArticle helpArticle = new HelpArticle();
            helpArticle.setUserInfo(user);
            JobArticle jobArticle = new JobArticle();
            jobArticle.setUserInfo(user);
            SchoolArticle schoolArticle = new SchoolArticle();
            schoolArticle.setUserInfo(user);
            CompanyArticle companyArticle = new CompanyArticle();
            companyArticle.setUserInfo(user);
//            获取各类型文章list
            List<LostArticle> lostList = lostArticleMapper.getArticleList(lostArticle,null);
            List<SecondArticle> secondList = secondArticleMapper.getArticleList(secondArticle,null,null,null);
            List<HelpArticle> helpList = helpArticleMapper.getArticleList(helpArticle,null);
            List<JobArticle> jobList = jobArticleMapper.getArticleList(jobArticle,null);
            List<SchoolArticle> schoolList = schoolArticleMapper.getArticleList(schoolArticle,null);
            List<CompanyArticle> companyList = companyArticleMapper.getArticleList(companyArticle,null);

            if (lostList.size()>0){
                //            删除信息通知
                notificationMapper.batchDel(1,lostList);
                //            删除评论信息
                commentMapper.batchDel(1,lostList);
                //            删除文章
                lostArticleMapper.batchDel(lostList);
                //              删除文件夹
                String delePath1 = "/upload/item/"+userStr+"/lostArticle";
                ImageUtil.deleteFileOrpath(delePath1);
            }
            if (secondList.size()>0){
                //            删除信息通知
                notificationMapper.batchDel(2,secondList);
                //            删除评论信息
                commentMapper.batchDel(2,secondList);
                //            删除文章
                secondArticleMapper.batchDel(secondList);
                //              删除文件夹
                String delePath2 = "/upload/item/"+userStr+"/secondArticle";
                ImageUtil.deleteFileOrpath(delePath2);
            }
            if (helpList.size()>0){
                //            删除信息通知
                notificationMapper.batchDel(3,helpList);
                //            删除评论信息
                commentMapper.batchDel(3,helpList);
                //            删除文章
                helpArticleMapper.batchDel(helpList);
                //              删除文件夹
                String delePath3 = "/upload/item/"+userStr+"/helpArticle";
                ImageUtil.deleteFileOrpath(delePath3);
            }
            if (jobList.size()>0){
                //            删除信息通知
                notificationMapper.batchDel(4,jobList);
                //            删除评论信息
                commentMapper.batchDel(4,jobList);
                //            删除文章
                jobArticleMapper.batchDel(jobList);
                //              删除文件夹
                String delePath4 = "/upload/item/"+userStr+"/jobArticle";
                ImageUtil.deleteFileOrpath(delePath4);
            }
            if (schoolList.size()>0){
                //            删除信息通知
                notificationMapper.batchDel(5,schoolList);
                //            删除评论信息
                commentMapper.batchDel(5,schoolList);
                //            删除文章
                schoolArticleMapper.batchDel(schoolList);
                //              删除文件夹
                String delePath5 = "/upload/item/"+userStr+"/schoolArticle";
                ImageUtil.deleteFileOrpath(delePath5);
            }
            if (companyList.size()>0){
                //            删除信息通知
                notificationMapper.batchDel(6,companyList);
                //            删除评论信息
                commentMapper.batchDel(6,companyList);
                //            删除文章
                companyArticleMapper.batchDel(companyList);
                //              删除文件夹
                String delePath6 = "/upload/item/"+userStr+"/companyArticle";
                ImageUtil.deleteFileOrpath(delePath6);
            }

            return true;
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());

        }

    }
}
