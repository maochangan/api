package com.sshy.api.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sshy.api.bean.ArCharManagement;
import com.sshy.api.bean.ArModelManagement;
import com.sshy.api.bean.ArThemeManagement;
import com.sshy.api.bean.CompanyUser;
import com.sshy.api.service.CUserService;
import com.sshy.api.utils.ARManaUtil.*;
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
import java.util.*;
import java.util.List;
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

    /*主题管理*/

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


    /*识别图管理*/

    /**
     * 获取识别图列表
     */
    @RequestMapping(value = "getARTargets", method = RequestMethod.GET)
    public JsonResult getImages() {
        logger.info("根据不同商户对应自己的AR识别图列表 数据c_user_id控制  这边未控制");
        List<ArCharManagement> list = cUserService.getAllARChart();
        if (null == list) {
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        }else{
            try {
                Iterator<ArCharManagement> integer = list.iterator();
                Map result = new TreeMap();
                while (integer.hasNext()) {
                    Map target = GetTarget.getTarget(integer.next().getArChartImageId());
                    if((Integer) target.get("statusCode") == 0){
                        result.put("target", target);
                    }
                    continue;
                }
                return JsonResult.success().add("result" , result);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(e.getMessage());
                return JsonResult.fail().add("err", "err");
            }
        }
    }

    /**
     * 添加识别图
     */
    @RequestMapping(value = "addARTarget", method = RequestMethod.POST)
    public JsonResult addARTarget(@Param("model") MultipartFile model,@Param("image")MultipartFile image , @Param("name") String name,@Param("modelName") String modelName ,
                                  @Param("size") String size, HttpServletRequest request) {
        logger.info("检查参数完整性");
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
            ArModelManagement arModelManagement = new ArModelManagement();
            arModelManagement.setArModelUrl(serverPath);
//            arModelManagement.setcUserId(1);//TODO 用户对应操作
            arModelManagement.setDeleteNum(1);
            arModelManagement.setArModelName(modelName);
            arModelManagement.setArModelCreateTime(new Timestamp(new Date().getTime()));
            boolean insertResult = cUserService.addARModel(arModelManagement);
            if(insertResult){
                //TODO
            }
            Map result = AddTarget.addTarget(Base64.getEncoder().encodeToString(image.getBytes()),name , size , serverPath);
            if((Integer) result.get("statusCode") == 0){
                logger.info("success");
                Map target = (Map) result.get("result");
                String targetId = (String) target.get("targetId");
                ArCharManagement arCharManagement = new ArCharManagement();
                arCharManagement.setDeleteNum(1);
                arCharManagement.setArChartImageId(targetId);
                arCharManagement.setArChartModelUrl(serverPath);
                arCharManagement.setArChartCreateTime(new Timestamp(new Date().getTime()));
                boolean state = cUserService.addARChart(arCharManagement);
                if (state) {
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }else {
                    return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                }
            }
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 获取识别图
     */
    @RequestMapping(value = "getARTarget", method = RequestMethod.GET)
    public JsonResult getARTarget(String targetId){
        logger.info("查询识别图信息");
        try {
            Map result = GetTarget.getTarget(targetId);
            if ((Integer) result.get("statusCode") == 0) {
                logger.info("查询成功");
                Map target = (Map) result.get("result");
                return JsonResult.success().add("target", target);
            }else{
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 修改识别图
     */
    @RequestMapping(value = "updateARTarget", method = RequestMethod.POST)
    public JsonResult updateARTarget(@Param("model") MultipartFile model,@Param("image")MultipartFile image , @Param("name") String name,
                                  @Param("size") String size, @Param("active") String active , @Param("targetId") String targetId, HttpServletRequest request) {
        logger.info("检查参数完整性");
        try {
            String baseImage = null;
            String meta = null;
            Map target = GetTarget.getTarget(targetId);
            if((Integer) target.get("statusCode") == 0){
                baseImage = (String) target.get("trackingImage");
                meta = (String) target.get("meta");
            }
            if(model.getOriginalFilename() == null && image.getOriginalFilename() == null){
                Map upTarget = UpdateTarget.updateTarget(targetId, baseImage, active, name, size, meta);
                if((Integer) upTarget.get("statusCode") == 0){
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }else if(model.getOriginalFilename() == null && image.getOriginalFilename() != null){
                Map upTarget = UpdateTarget.updateTarget(targetId, Base64.getEncoder().encodeToString(image.getBytes()), active, name, size, meta);
                if((Integer) upTarget.get("statusCode") == 0){
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            }else if(model.getOriginalFilename() != null && image.getOriginalFilename() == null){
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
                Map upTarget = UpdateTarget.updateTarget(targetId, baseImage, active, name, size, serverPath);
                if((Integer) upTarget.get("statusCode") == 0 && cUserService.updateARTarget(serverPath, targetId)){
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }else{
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
                Map upTarget = UpdateTarget.updateTarget(targetId, Base64.getEncoder().encodeToString(image.getBytes()), active, name, size, serverPath);
                if((Integer) upTarget.get("statusCode") == 0 && cUserService.updateARTarget(serverPath, targetId)){
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                }
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 识别图删除
     */
    @RequestMapping(value = "removeTarget", method = RequestMethod.GET)
    public JsonResult removeTarget(String targetId){
        logger.info("识别图删除");
        try {
            Map result = RemoveTarget.removeTarget(targetId);
            if((Integer) result.get("statusCode") == 0){
                cUserService.removeTarget(targetId);
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            }
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }


    /*展示内容管理*/




}
