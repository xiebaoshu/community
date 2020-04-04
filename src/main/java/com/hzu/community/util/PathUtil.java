package com.hzu.community.util;

import com.hzu.community.bean.UserInfo;

public class PathUtil {

    public static String getImgBasePath() {
//        根据系统选择图片存放路径
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "E:/workspace/community/src/main/resources/static/images";

        } else {
            basePath = "/Users/work/image";
        }

        return basePath;
    }
    public static String getLostArticleImagePath(UserInfo owner,long lostArticleId) {

        String imagePath = "/upload/item/" + owner.getUserId() +"/lostArticle/"+lostArticleId+"/" ;
        return imagePath;
    }

    public static String getHelpArticleImagePath(UserInfo owner,long helpArticleId) {

        String imagePath = "/upload/item/" + owner.getUserId() +"/helpArticle/"+helpArticleId+"/" ;
        return imagePath;
    }

    public static String getSecondArticleImagePath(UserInfo owner,long secondArticleId) {

        String imagePath = "/upload/item/" + owner.getUserId() +"/secondArticle/"+secondArticleId+"/" ;
        return imagePath;
    }
}
