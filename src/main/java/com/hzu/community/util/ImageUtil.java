package com.hzu.community.util;

import com.hzu.community.dto.ImageHolder;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;


public class ImageUtil {
    private static String basePath = "/Users/work/image";
    private static final SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static final Random r = new Random();
    public static String generateThumbnail(ImageHolder thumbnail, String targetAddr)
    //缩略图
    {

        String realFileName = getRandomFileName();//获取5位随机数，并在后面添加时间，作为文件名
        String extension = getFileExtension(thumbnail.getImageName());//通过文件名获取文件后缀
        makeDirPath(targetAddr);//根据地址创建文件目录
        String relativeAddr = targetAddr + realFileName + extension;//将地址和文件名和文件后缀合起来

        File dest = new File(PathUtil.getImgBasePath()+relativeAddr);//创建文件
        try{ //图片处理
//            size图片大小
//            outputQuality图片品质，1最高
            Thumbnails.of(thumbnail.getImage()).size(200, 200)

                    .outputQuality(0.8f).toFile(dest);
        }catch(IOException e){
            e.printStackTrace();
        }
        return relativeAddr;//返回图片地址

    }



    private static void makeDirPath(String targetAddr){
        String realFileParentPath = PathUtil.getImgBasePath() + targetAddr;
        File dirPath = new File(realFileParentPath);
        if(!dirPath.exists()){
            dirPath.mkdirs();
        }
    }


    private static String getFileExtension(String fileName){

        return fileName.substring(fileName.lastIndexOf("."));
    }


    public static String getRandomFileName(){
        int rannum = r.nextInt(89999) + 10000 ;
        String nowTimeStr = sDateFormat.format(new Date());
        return nowTimeStr + rannum;
    }



    /*
     * storePath是文件的路径还是目录的路径
     * 如果storePath是文件路径则删除该文件
     * 如果storepath是目录路径则删除该目录下的所以文件
     * @paranm storePath
     */
    public static void deleteFileOrpath(String storepath){
        File fileOrPath = new File(PathUtil.getImgBasePath() + storepath);
        if(fileOrPath.exists()){
            if(fileOrPath.isDirectory()){
                File files[] = fileOrPath.listFiles();
                for(int i=0;i<files.length;i++){
                    files[i].delete();
                }
            }
            fileOrPath.delete();
        }
    }

}