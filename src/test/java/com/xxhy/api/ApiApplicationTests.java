package com.xxhy.api;

import com.sshy.api.App;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;
import com.sshy.api.controller.CUserController;
import com.sshy.api.controller.MenuController;
import com.sshy.api.service.CUserService;
import com.sshy.api.utils.COSUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ApiApplicationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CUserService cUserService;

    @Test
    public void contextLoads() {
        List<ArThemeManagement> list = cUserService.getAllARTheme();
        System.out.println(list.size());
    }

    @Test
    public void insertCUnionId(){
        MenuController menuController = testRestTemplate.getForObject("/menu/spaceSize", MenuController.class);
        Assert.assertTrue("test" , true);

    }

    @Test
    public void testSelect(){
        String cUnionId = "teatasdffw56141";
        CompanyUser companyUser = new CompanyUser();
        companyUser.setcName("testa");
        companyUser.setcPassword("123456aa");
        companyUser.setcCreateData(new Timestamp(new Date().getTime()));
        companyUser.setcInfo("test");
        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("companyUser", companyUser);
        multiValueMap.add("cUnionId",cUnionId);
        CUserController cUserController = testRestTemplate.postForObject("/regist",multiValueMap, CUserController.class);
    }

    @Test
    public void testDelete(){
        String key = "/test/image/i888.png";
        boolean state = COSUtil.deleteFile(key);
        System.out.println(state);

    }



}
