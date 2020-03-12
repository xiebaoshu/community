package com.hzu.community.util;

import com.hzu.community.bean.UserInfo;

public class PathUtil {
    //private static String seperator = System.getProperty("file.separator");
    public static String getImgBasePath() {
        String os = System.getProperty("os.name");
        String basePath = "";
        if (os.toLowerCase().startsWith("win")) {
            basePath = "E:/workspace/community/src/main/resources/static/images";

        } else {
            basePath = "/Users/work/image";
        }
        //basePath = basePath.replace("/", seperator);
        return basePath;
    }
    public static String getLostArticleImagePath(UserInfo owner,long lostArticleId) {

        String imagePath = "/upload/item/" + owner.getUserId() +"/lostArticle/"+lostArticleId+"/" ;
        return imagePath;
    }
}
