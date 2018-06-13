package com.sshy.api.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sshy.api.bean.ArCharManagement;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;
import com.sshy.api.service.CUserService;
import com.sshy.api.utils.ARManaUtil.AddTarget;
import com.sshy.api.utils.ARManaUtil.GetTarget;
import com.sshy.api.utils.ARManaUtil.GetTargets;
import com.sshy.api.utils.ARutils.ResultInfo;
import com.sshy.api.utils.ARutils.WebAR;
import com.sshy.api.utils.ConstantInterface;
import com.sshy.api.utils.JsonResult;
import okhttp3.*;
import org.apache.ibatis.annotations.Param;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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
        PageHelper.startPage(1, 10);
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


    /**
     * 获取识别图列表
     */
    @RequestMapping(value = "getARTargets", method = RequestMethod.GET)
    public JsonResult getImages() {
        Map result = null;
        try {
            result = GetTargets.getTargets();
            return JsonResult.success().add("result" , result);
        } catch (Exception e) {
            logger.info(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 添加识别图
     */
    @RequestMapping(value = "addARTarget", method = RequestMethod.POST)
    public JsonResult addARTarget(@Param("model") MultipartFile model,@Param("image")MultipartFile image , @Param("name") String name, @Param("type") String type,
                                  @Param("size") String size, HttpServletRequest request) {
        logger.info("检擦参数完整性");
        if (null == model.getOriginalFilename() || null == image.getOriginalFilename()) {
            return JsonResult.fail().add("msg", ConstantInterface.DATA_UPLOAD_ERR);
        }
        try {
            String path = ConstantInterface.FILE_BASE_PATH + "cName/" + "ARTargetModel/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String finalPath = path + model.getOriginalFilename();
            File finalDir = new File(finalPath);
            model.transferTo(finalDir);
            String serverPath = ConstantInterface.SERVER_PATH + request.getLocalPort() +
                    request.getServletContext().getContextPath() + "/cName" + "/ARTargetModel/" +
                    model.getOriginalFilename();
            Map result = AddTarget.addTarget(Base64.getEncoder().encodeToString(image.getBytes()),type , name , size , serverPath);
            if((Integer) result.get("statusCode") == 0){
                Map target = (Map) result.get("result");
                String targetId = (String) target.get("targetId");
                ArCharManagement arCharManagement = new ArCharManagement();
                arCharManagement.setDeleteNum(1);
                arCharManagement.setArChartImageId(targetId);
                arCharManagement.setArChartModelUrl(serverPath);
                boolean state = cUserService.addARChart(arCharManagement);
                if (state) {
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }
            }
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }




















    /**
     * 识别图添加包含模型
     * 作废(TODO)
     */
    @RequestMapping(value = "addARModel", method = RequestMethod.POST)
    public JsonResult testAR(@Param("image") MultipartFile image , @Param("model") MultipartFile model , HttpServletRequest request){
        String cloudKey = "ee9b273d2deddfd932ddca3db269ad0c";
        String cloudSecret = "j2qQBZaiZAt3hdDHEMMdzOXIy2mIGOrofIWo8HO1ZdSi0XrQ0lx9E9ntgBfTeBdmGyltt7bg8hbRW0rhwtq4yK9WEFJFiL2xB3lyZMr35BCI6DtWMvvrYLOPMV01d9E9";
        String cloudUrl = "http://ea3a961d7d77b977059d064111d9c1b0.cn1.crs.easyar.com:8080/search";
        WebAR webAR = new WebAR(cloudKey, cloudSecret, cloudUrl);
        try {
            Base64.Encoder encoder = Base64.getEncoder();
            String imageBase64 = encoder.encodeToString(image.getBytes());
            ResultInfo info = webAR.recognize(imageBase64);
            if (info.getStatusCode() == 0/* && model.getOriginalFilename() != null*/) {
                logger.info("识别到目标"+info.getResult().getTarget().getTargetId());
                String imagePath = ConstantInterface.FILE_BASE_PATH + "cName/" + "ArChartDir/img/";
                File dir = new File(imagePath);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String finalPath = imagePath + image.getOriginalFilename();
                File finalDir = new File(finalPath);
                image.transferTo(finalDir);
                String imgServerPath = ConstantInterface.SERVER_PATH + request.getLocalPort() +
                        request.getServletContext().getContextPath() + "/cName" + "/ArChartDir/img/" +
                        image.getOriginalFilename();

                String modelPath = ConstantInterface.FILE_BASE_PATH + "cName/" + "ArChartDir/model/";
                File mdir = new File(modelPath);
                if (!mdir.exists()) {
                    mdir.mkdirs();
                }
                String mfinalPath = modelPath + model.getOriginalFilename();
                File mfinalDir = new File(mfinalPath);
                image.transferTo(mfinalDir);
                String modelServerPath = ConstantInterface.SERVER_PATH + request.getLocalPort() +
                        request.getServletContext().getContextPath() + "/cName" + "/ArChartDir/model/" +
                        model.getOriginalFilename();
                ArCharManagement arCharManagement = new ArCharManagement();
                arCharManagement.setArChartCreateTime(new Timestamp(new Date().getTime()));
                arCharManagement.setArChartImageId(info.getResult().getTarget().getTargetId());
                arCharManagement.setArChartImageUrl(imgServerPath);
                arCharManagement.setArChartModelUrl(modelServerPath);
                arCharManagement.setDeleteNum(1);
                boolean state = cUserService.addARChart(arCharManagement);
                if (state) {
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }else{
                    return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                }
            } else {
                logger.info("未识别到目标");
                return JsonResult.fail().add("msg", ConstantInterface.NO_MATCHION);
            }
        } catch (IOException e) {
            return JsonResult.fail().add("err", "err");
        }
    }






}
