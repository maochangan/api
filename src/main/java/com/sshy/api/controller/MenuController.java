package com.sshy.api.controller;


import com.sshy.api.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping(value = "menu")
public class MenuController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Map getMenu(){
        logger.info("获取菜单  未设置权限");
        Map<String, Object> topMenu = new HashMap<>();
        Map<String, Object> secMenu1 = new HashMap<>();
        List<Object> list1 = new ArrayList<>();
        list1.add("AR主题管理");
        list1.add("AR识别图管理");
        list1.add("AR展示内容管理");
        secMenu1.put("senMenu", list1);
        topMenu.put("topMun", secMenu1);
        topMenu.put("neMeun" , "AR素材管理");
        return topMenu;
    }

    @RequestMapping(value = "spaceSize", method = RequestMethod.GET)
    public JsonResult getSpaceSize(){
        logger.info("获取储存空间 ， 假定");
        Integer spaceSize = 1000;
        return JsonResult.success().add("spaceSize", spaceSize);
    }


    @RequestMapping(value = "flowSize", method = RequestMethod.GET)
    public JsonResult getFlowSize(){
        logger.info("获取浏览, 假定");
        Integer flowSize = 1000;
        return JsonResult.success().add("flowSize", flowSize);
    }


}
