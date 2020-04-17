package com.hzu.community.controller.information;


import java.io.File;

import com.hzu.community.dto.JobFair;
import com.hzu.community.dto.NoticeDto;
import net.sf.json.JSONArray;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.List;


@Controller
@RequestMapping("/information")
public class InformationController {

    private static final Logger logger = LogManager.getLogger(InformationController.class);

    @RequestMapping("/jobFair")
    public String JobFair(Model model){

        File myFile = new File("E:/workspace/community/src/main/resources/static/json/jsonzhaoping.json");
        if (!myFile.exists()) {
            System.out.println("json文件不存在");
        }else{
            String jsonStr  = readJsonFile(myFile);
            JSONArray json = JSONArray.fromObject(jsonStr);
            List<JobFair> informationList= (List<JobFair>)JSONArray.toCollection(json, JobFair.class);
            model.addAttribute("informationList",informationList);
        }
        return "information/jobfair";
    }
    @RequestMapping("/notice")
    public String notice(Model model){

        File myFile = new File("E:/workspace/community/src/main/resources/static/json/notice.json");
        if (!myFile.exists()) {
            System.out.println("json文件不存在");
        }else{
            String jsonStr  = readJsonFile(myFile);
            JSONArray json = JSONArray.fromObject(jsonStr);
            List<NoticeDto> informationList= (List<NoticeDto>)JSONArray.toCollection(json, JobFair.class);
            model.addAttribute("informationList",informationList);
        }
        return "information/notice";
    }


    public String readJsonFile(File jsonFile) {
        String jsonStr = "";
        logger.info("————开始读取" + jsonFile.getPath() + "文件————");
        try {
            //File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            logger.info("————读取" + jsonFile.getPath() + "文件结束!————");
            return jsonStr;
        } catch (Exception e) {
            logger.info("————读取" + jsonFile.getPath() + "文件出现异常，读取失败!————");
            e.printStackTrace();
            return null;
        }
    }
}
