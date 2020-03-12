package com.hzu.community.service.impl;

import com.hzu.community.bean.LostArticle;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.dto.LostArticleExecution;
import com.hzu.community.enums.LostArticleEnum;
import com.hzu.community.exceptions.LostArticleException;
import com.hzu.community.mapper.LostArticleMapper;
import com.hzu.community.service.LostArticleService;
import com.hzu.community.util.ImageUtil;
import com.hzu.community.util.PathUtil;

import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LostArticleServiceImpl implements LostArticleService {
    @Autowired
    LostArticleMapper lostArticleMapper;

    @Override
    @Transactional
    public LostArticleExecution saveArticle(LostArticle lostArticle, ImageHolder imageHolder) {
        if (lostArticle == null) {
            return new LostArticleExecution(LostArticleEnum.NULL_Article);
        }
        try {
                /*因为addLostArticle方法中，开启了mybtis的useGeneratedKeys
                 所以成功插入后返回主键值到lostArticleId
                 该值作为更新图片url的参数*/
                int insertNum = lostArticleMapper.addLostArticle(lostArticle);

                if (insertNum <= 0) {
                   /* Spring事务回滚机制是这样的：当所拦截的方法有指定异常抛出，事务才会自动进行回滚！
                        手动抛出异常,使事务回滚*/
                    throw new LostArticleException("帖子数据插入失败");
                } else {
                    if (imageHolder.getImage() != null) {
                        try {
                            addArticleImg(lostArticle, imageHolder);
                        } catch (Exception e) {
//                         异常被try{}捕捉了，在Catch(){}中手动抛出运行时异常供事务管理器捕捉；
                            throw new LostArticleException("addShopImg error:" + e.getMessage());
//                            抛出的异常在上层处理。即控制器
                        }
//                        更新图片url
                        int updateImg = lostArticleMapper.updatelost(lostArticle);
                        if (updateImg<=0){
                            throw new LostArticleException("更新图片地址失败");
                        }
                    }



                }
            }catch(Exception e){
                throw new LostArticleException(e.getMessage());

            }
            return new LostArticleExecution(LostArticleEnum.SUCCESS,lostArticle);


    }

    private void addArticleImg(LostArticle lostArticle, ImageHolder lostArticleInputStream){
        //获取图片目录的相对值路径
        //通过userid,articleid划分文件地址，并传入下一个函数作为参数
        String dest = PathUtil.getLostArticleImagePath(lostArticle.getUserInfo(),lostArticle.getLostArticleId());
        String ArticleAddr = ImageUtil.generateThumbnail(lostArticleInputStream,dest);//处理图片。并返回图片地址
        lostArticle.setArticleImg(ArticleAddr);//设置图片地址

    }






}

