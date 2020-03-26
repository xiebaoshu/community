package com.hzu.community;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hzu.community.bean.*;
import com.hzu.community.mapper.*;
import com.hzu.community.service.NavService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.validation.constraints.Null;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
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
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    NavMapper navMapper;
    @Autowired
    NavService navService;
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
       /* LostArticle lostArticle = new LostArticle();
        Area area = new Area();
        area.setAreaId(1);
        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleCategoryId(1);
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItemCategoryId(1);
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(1);
        lostArticle.setArea(area);
        lostArticle.setPhone("141231");
        lostArticle.setArticleCategory(articleCategory);
        lostArticle.setItemCategory(itemCategory);
        lostArticle.setUserInfo(userInfo);
        lostArticle.setArticleTitle("捡到到一部手机");
        lostArticle.setArticleContent("在旭日大楼捡到一部iphone11手机");
        lostArticle.setAreaDetail("旭日大楼103");
        lostArticle.setItemDetail("iphone11银灰色红色手机壳");

        Date date=new Date();
        lostArticle.setCreateTime(date);
        lostArticleMapper.addLostArticle(lostArticle);*/



//        LostArticle lostArticle = new LostArticle();
//
//        // 开启分页
//        PageHelper.startPage(1, 10);
//        List<LostArticle> list = new ArrayList<>();
//        list=lostArticleMapper.getArticleList(lostArticle,30);
//        PageInfo<LostArticle> articlePageInfo = new PageInfo<>(list);
//        for (int i = 0; i < articlePageInfo.getList().size(); i++) {
//            System.out.println(articlePageInfo.getList().get(i).getArticleTitle());
//        }

          LostArticle lostArticle = lostArticleMapper.findArticleById(102);
        System.out.println(lostArticle);













    }
    @Test
    void userMapperTest(){

        UserInfo user = userInfoMapper.findUserInfoById(1);
        System.out.println(user.getUserName());
    }
    @Test
    void CommentMapperTest(){

        Comment comment = new Comment();
        comment.setCommentId(89L);
        commentMapper.deleComment(comment);
    }
    @Test
    void NavMapperTest(){
        List<Nav> navList = navService.navDefault();
        for (Nav nav : navList) {
            System.out.println(nav);
        }

    }


}
