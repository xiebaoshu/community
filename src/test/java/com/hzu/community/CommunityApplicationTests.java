package com.hzu.community;

import com.hzu.community.bean.*;
import com.hzu.community.mapper.AreaMapper;
import com.hzu.community.mapper.ArticleCategoryMapper;
import com.hzu.community.mapper.LostArticleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.List;

@SpringBootTest
class CommunityApplicationTests {
    @Autowired
    AreaMapper areaMapper;
    @Autowired
    ArticleCategoryMapper articleCategoryMapper;
    @Autowired
    LostArticleMapper lostArticleMapper;
    @Test
    void areaMapperTest(){
        List<Area> areaList = areaMapper.getArea();
        for (Area area : areaList) {
            System.out.println(area.getAreaName());
        }

    }
    @Test
    void articleCategoryMapperTest(){
//        List<ArticleCategory> articleCategoryList = articleCategoryMapper.getArticleCategory(null);
        List<ArticleCategory> articleCategoryList = articleCategoryMapper.getArticleCategory(1);
        for (ArticleCategory articleCategory : articleCategoryList) {
            System.out.println(articleCategory.getArticleCategoryName());
        }

    }
    @Test
    void lostArticleMapperTest(){
        LostArticle lostArticle = new LostArticle();
        Area area = new Area();
        area.setAreaId(1);
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleCategoryId(1);
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(1);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        lostArticle.setArea(area);
        lostArticle.setArticleCategory(articleCategory);
        lostArticle.setItemCategory(itemCategory);
        lostArticle.setUserInfo(userInfo);
        lostArticle.setArticleTitle("捡到到一部手机");
        lostArticle.setArticleContent("在旭日大楼捡到一部iphone11手机");
        lostArticle.setAreaDetail("旭日大楼103");
        lostArticle.setItemDetail("iphone11银灰色红色手机壳");

        Date date=new Date();
        lostArticle.setCreateTime(date);
        lostArticleMapper.addLostArticle(lostArticle);


    }

}
