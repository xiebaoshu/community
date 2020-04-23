package com.hzu.community.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Strings;
import com.hzu.community.bean.Nav;
import com.hzu.community.controller.information.InformationController;
import com.hzu.community.dto.JobFair;
import com.hzu.community.dto.NoticeDto;
import com.hzu.community.service.InformationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class InformationServiceImpl implements InformationService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final Logger logger = LogManager.getLogger(InformationController.class);

    @Override
    public List<JobFair> JobFairList() {
        //默认导航列表，使用redis缓存
        String key = "jobFairList";
        //Jedis对缓存的操作，key-value的操作
        String json =  redisTemplate.opsForValue().get(key);
        List<JobFair> jobFairList = null;
        if (Strings.isNullOrEmpty(json)) {
            File myFile = new File("E:/workspace/community/src/main/resources/static/json/jsonzhaoping.json");
            if (!myFile.exists()) {
                throw new RuntimeException("json文件不存在");
            }else{
                String jsonStr  = readJsonFile(myFile);
                jobFairList = JSON.parseObject(jsonStr,new TypeReference<List<JobFair>>(){});

            }
            String string  = JSON.toJSONString(jobFairList);
            redisTemplate.opsForValue().set(key, string);
            //设置过期时间,10分钟
            redisTemplate.expire(key, 10, TimeUnit.MINUTES);

        }else {
            jobFairList = JSON.parseObject(json,new TypeReference<List<JobFair>>(){});
        }
        return jobFairList;


    }

    @Override
    public List<NoticeDto> NoticeDtoList() {
        //默认导航列表，使用redis缓存
        String key = "noticeDtoList";
        //Jedis对缓存的操作，key-value的操作
        String json =  redisTemplate.opsForValue().get(key);
        List<NoticeDto> noticeDtoList = null;
        if (Strings.isNullOrEmpty(json)) {
            File myFile = new File("E:/workspace/community/src/main/resources/static/json/notice.json");
            if (!myFile.exists()) {
                throw new RuntimeException("json文件不存在");
            }else{
                String jsonStr  = readJsonFile(myFile);
                noticeDtoList= JSON.parseObject(jsonStr,new TypeReference<List<NoticeDto>>(){});

            }
            String string  = JSON.toJSONString(noticeDtoList);
            redisTemplate.opsForValue().set(key, string);
            //设置过期时间,10分钟
            redisTemplate.expire(key, 10, TimeUnit.MINUTES);

        }else {
            noticeDtoList = JSON.parseObject(json,new TypeReference<List<NoticeDto>>(){});
        }
        return noticeDtoList;

    }

//    读取json文件
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

