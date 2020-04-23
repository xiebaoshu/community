package com.hzu.community.controller.information;


import java.io.File;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.hzu.community.bean.Nav;
import com.hzu.community.dto.JobFair;
import com.hzu.community.dto.NoticeDto;

import com.hzu.community.service.InformationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.List;


@Controller
@RequestMapping("/information")
public class InformationController {
    @Autowired
    private InformationService informationService;

    @RequestMapping("/jobFair")
    public String JobFair(Model model){


        model.addAttribute("informationList",informationService.JobFairList());
        return "information/jobfair";
    }
    @RequestMapping("/notice")
    public String notice(Model model){

        model.addAttribute("informationList",informationService.NoticeDtoList());
        return "information/notice";
    }



}
