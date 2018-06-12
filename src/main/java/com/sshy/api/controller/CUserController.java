package com.sshy.api.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;
import com.sshy.api.service.CUserService;
import com.sshy.api.utils.ARutils.ResultInfo;
import com.sshy.api.utils.ARutils.WebAR;
import com.sshy.api.utils.ConstantInterface;
import com.sshy.api.utils.JsonResult;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping(value = "")
public class CUserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CUserService cUserService;

    //商户注册
    @RequestMapping(value = "regist", method = RequestMethod.POST)
    public JsonResult regist(@Param("cUnionId") String cUnionId ,@Param("companyUser")CompanyUser companyUser , HttpSession session){
        logger.info("商户注册" + companyUser.getcName()+companyUser.getcInfo());
        if(null == cUnionId || null == companyUser.getcName() || null == companyUser.getcPassword()){
            logger.info("无数据");
            return JsonResult.fail().add("msg", ConstantInterface.DATA_UPLOAD_ERR );
        }
        logger.info("数据完整,进入插入操作");
        if(cUserService.checkCunionIdOnly(cUnionId)){//unionid是否存在重复
            logger.info("已存在数据");
            return JsonResult.fail().add("msg", ConstantInterface.DATA_NOT_ONLY);
        }
        cUserService.insertUnionId(cUnionId);
        companyUser.setcUnionidId(cUserService.getCunionId(cUnionId));
        boolean state = cUserService.insertCompanyUser(companyUser);
        if(state){
            logger.info("插入成功！" + state);
            session.setAttribute("cUser" ,cUnionId );
            return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
        }
        return JsonResult.fail().add("msg" , ConstantInterface.FAIL_MSG);
    }

    //商户登陆
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonResult userLogin(CompanyUser companyUser) {
        //TODO login
        return JsonResult.fail().add("msg", "null");
    }

    //**************************分割线**************************//

    /**
     * 获取全部主题列表
     * @param pn 页数
     * @param ps 每页条目数
     * @return pageInfo
     */
    @RequestMapping(value = "getAllARTheme", method = RequestMethod.GET)
    public JsonResult getAllARTheme(Integer pn , Integer ps){
        PageHelper.startPage(pn, ps);
        List<ArThemeManagement> list = cUserService.getAllARTheme();
        if(null == list){
            return JsonResult.fail().add("msg", ConstantInterface.NO_DATA_MESSAGE);
        }
        PageInfo<ArThemeManagement> pageInfo = new PageInfo<>(list);
        return JsonResult.success().add("pageInfo", pageInfo);
    }

    /**
     * 新增主题
     * @param file
     * @param arThemeManagement
     * @param request
     */
    @RequestMapping(value = "addARTheme", method = RequestMethod.POST)
    public JsonResult addArTheme(@Param("file") MultipartFile file, @Param("arThemeManagement") ArThemeManagement arThemeManagement, HttpServletRequest request) {
        logger.info("主题添加" + file.getOriginalFilename());
        try {
            if (null == file.getOriginalFilename() && arThemeManagement.getArThemeImgUrl() != null) {
                logger.info("从图库里获取缩略图");
                arThemeManagement.setDeleteNum(1);
                boolean state = cUserService.addARTheme(arThemeManagement);
                if(state){
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }else{
                    return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                }
            }
            logger.info("上传缩略图");
            String path = ConstantInterface.FILE_BASE_PATH + "cName/" + "ArThemeDir/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String finalPath = path + file.getOriginalFilename();
            File finalDir = new File(finalPath);
            file.transferTo(finalDir);
            String serverPath = ConstantInterface.SERVER_PATH + request.getLocalPort() +
                    request.getServletContext().getContextPath() + "/cName" + "/ArThemeDir/" +
                    file.getOriginalFilename();
            arThemeManagement.setArThemeImgUrl(serverPath);
            arThemeManagement.setDeleteNum(1);
            boolean state = cUserService.addARTheme(arThemeManagement);
            if(state){
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            }else{
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 主题更新
     * @param file 图片
     * @param arThemeManagement
     * TODO 文件路径需要修改
     */
    @RequestMapping(value = "updateARTheme", method = RequestMethod.POST)
    public JsonResult updateARTheme(@Param("file") MultipartFile file , HttpServletRequest request , @Param("arThemeManagement") ArThemeManagement arThemeManagement){
        logger.info("FileUpload" + file.getOriginalFilename());
        try {
            if (null == file.getOriginalFilename()) {
                logger.info("不修改图片");
                boolean state = cUserService.updateArTheme(arThemeManagement);
                if (state) {
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }else{
                    return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                }
            }
            String path = ConstantInterface.FILE_BASE_PATH + "cName/" + "ArThemeDir/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String finalPath = path + file.getOriginalFilename();
            File finalDir = new File(finalPath);
            file.transferTo(finalDir);
            String serverPath = ConstantInterface.SERVER_PATH + request.getLocalPort() +
                    request.getServletContext().getContextPath() + "/cName" + "/ArThemeDir/" +
                    file.getOriginalFilename();
            arThemeManagement.setArThemeImgUrl(serverPath);
            boolean state = cUserService.updateArTheme(arThemeManagement);
            if (state) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            }else{
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 主题逻辑删除
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteARTheme", method = RequestMethod.POST)
    public JsonResult deleteARTheme(Integer id) {
        logger.info("主题删除");
        ArThemeManagement arThemeManagement = cUserService.getARThemeById(id);
        if (null == arThemeManagement) {
            return JsonResult.fail().add("msg", ConstantInterface.NO_DATA_MESSAGE);
        }else{
            arThemeManagement.setDeleteNum(0);
            boolean state = cUserService.updateArTheme(arThemeManagement);
            if (state) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            }else{
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        }
    }


    @RequestMapping(value = "testARReturn", method = RequestMethod.GET)
    public JsonResult testAR(){
        String cloudKey = "<这里是云图库的Cloud Key>";
        String cloudSecret = "<这里是云图库的Cloud Secret>";
        String cloudUrl = "http://<这里是云图库的Client-end URL>/search";
        WebAR webAR = new WebAR(cloudKey, cloudSecret, cloudUrl);
        try {
            // 图片的base64数据
            String image = "/9j/4AAQSkZJRgA";
            ResultInfo info = webAR.recognize(image);

            if (info.getStatusCode() == 0) {
                // statusCode为0时，识别到目标，数据在target中
                System.out.println(info.getResult().getTarget().getTargetId());
            } else {
                // 未识别到目标
                System.out.println(info.getStatusCode());
                System.out.println(info.getResult().getMessage());
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }












}
