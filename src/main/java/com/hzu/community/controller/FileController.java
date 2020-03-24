package com.hzu.community.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hzu.community.bean.LostArticle;
import com.hzu.community.dto.FileDto;
import com.hzu.community.dto.ImageHolder;
import com.hzu.community.util.HttpServletRequestUtil;
import com.hzu.community.util.ImageUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class FileController {
    @RequestMapping("/file/upload")
    @ResponseBody
    public FileDto upload(HttpServletRequest request) {
        FileDto fileDto = new FileDto();
        CommonsMultipartFile articleImg = null;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){

            MultipartHttpServletRequest MultipartHttpServletRequest = (MultipartHttpServletRequest) request;
            articleImg = (CommonsMultipartFile) MultipartHttpServletRequest.getFile("editormd-image-file");
        }else{

            fileDto.setSuccess(0);
            fileDto.setMessage("上传失败");
            return fileDto;
        }

        if (articleImg!=null){
            try {
                //将文件转化为文件流，和获取文件名。方法都是CommonsMultipartFile函数包的，
                ImageHolder imageHolder = new ImageHolder(articleImg.getOriginalFilename(),articleImg.getInputStream());
//                保存到本地
                String ArticleAddr = ImageUtil.generateThumbnail(imageHolder,"/upload/textimg/");
                fileDto.setSuccess(1);
                fileDto.setUrl(ArticleAddr);
            }catch (IOException e){
//                文件转化为文件流需catch io错误
                fileDto.setSuccess(0);
                fileDto.setMessage("上传失败");
                return fileDto;
            }
            return fileDto;

        }else {
            fileDto.setSuccess(0);
            fileDto.setMessage("文件转为CommonsMultipartFile失败");
            return fileDto;
        }



    }

}
