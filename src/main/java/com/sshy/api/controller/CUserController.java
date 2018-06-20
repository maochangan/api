package com.sshy.api.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sshy.api.bean.*;
import com.sshy.api.service.CUserService;
import com.sshy.api.utils.ARManaUtil.*;
import com.sshy.api.utils.COSUtil;
import com.sshy.api.utils.ConstantInterface;
import com.sshy.api.utils.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "")
public class CUserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CUserService cUserService;

    //商户注册
    @RequestMapping(value = "regist", method = RequestMethod.POST)
    public JsonResult regist(@RequestParam("cUnionId") String cUnionId, @ModelAttribute("companyUser") CompanyUser companyUser, HttpSession session) {
        logger.info("商户注册" + companyUser.getcName() + companyUser.getcInfo());
        if (null == cUnionId || null == companyUser.getcName() || null == companyUser.getcPassword()) {
            logger.info("无数据");
            return JsonResult.fail().add("msg", ConstantInterface.DATA_UPLOAD_ERR);
        }
        logger.info("数据完整,进入插入操作");
        if (cUserService.checkCunionIdOnly(cUnionId)) {//unionid是否存在重复
            logger.info("已存在数据");
            return JsonResult.fail().add("msg", ConstantInterface.DATA_NOT_ONLY);
        }
        cUserService.insertUnionId(cUnionId);
        companyUser.setcUnionidId(cUserService.getCunionId(cUnionId));
        boolean state = cUserService.insertCompanyUser(companyUser);
        if (state) {
            logger.info("插入成功！" + state);
            session.setAttribute("cUser", cUnionId);
            return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
        }
        return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
    }

    //商户登陆
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonResult userLogin(@ModelAttribute("companyUser") CompanyUser companyUser) {
        //TODO login
        return JsonResult.fail().add("msg", "null");
    }

    //**************************分割线**************************//

    /*主题管理*/

    /**
     * 获取全部主题列表
     *
     * @param pn 页数
     * @param ps 每页条目数
     * @return pageInfo
     */
    @RequestMapping(value = "getAllARTheme", method = RequestMethod.GET)
    public JsonResult getAllARTheme(@RequestParam("pn") Integer pn, @RequestParam("ps") Integer ps) {
        PageHelper.startPage(pn, ps);
        List<ArThemeManagement> list = cUserService.getAllARTheme();//TODO 数据库做对应查询
        if (null == list) {
            return JsonResult.fail().add("msg", ConstantInterface.NO_DATA_MESSAGE);
        }
        PageInfo<ArThemeManagement> pageInfo = new PageInfo<>(list);
        return JsonResult.success().add("pageInfo", pageInfo);
    }

    /**
     * 新增主题
     */
    @RequestMapping(value = "addARTheme", method = RequestMethod.POST)
    public JsonResult addArTheme(@ModelAttribute("arThemeManagement") ArThemeManagement arThemeManagement) {
        try {
            arThemeManagement.setDeleteNum(0);
//            arThemeManagement.setcUserId(1);
            arThemeManagement.setArThemeStartTime(new Timestamp(new Date().getTime()));
            boolean state = cUserService.addARTheme(arThemeManagement);
            if (state) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            } else {
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 主题更新
     */
    @RequestMapping(value = "updateARTheme", method = RequestMethod.POST)
    public JsonResult updateARTheme(@ModelAttribute("arThemeManagement") ArThemeManagement arThemeManagement) {
        try {
            boolean state = cUserService.updateArTheme(arThemeManagement);
            if (state) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            } else {
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 主题逻辑删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteARTheme", method = RequestMethod.POST)
    public JsonResult deleteARTheme(@RequestParam("id") Integer id) {
        logger.info("主题删除");
        boolean state = cUserService.deleteARThemeById(id);
        if (state) {
            return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
        } else {
            return JsonResult.fail().add("msg", ConstantInterface.NO_DATA_MESSAGE);
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
        } else {
            try {
                Iterator<ArCharManagement> integer = list.iterator();
                List result = new ArrayList();
                while (integer.hasNext()) {
                    Map item = new HashMap();
                    ArCharManagement arCharManagement = integer.next();
                    Map target = GetTarget.getTarget(arCharManagement.getArChartImageId());
                    ArModelManagement arModelManagement = cUserService.getARModel(arCharManagement.getArModelId());
                    ArThemeManagement arThemeManagement = cUserService.getARThemeById(arCharManagement.getArThemeId());
                    if ((Integer) target.get("statusCode") == 0) {
                        item.put("target", target);
                        item.put("arModel", arModelManagement);
                        item.put("arTheme", arThemeManagement);
                        result.add(item);
                    }
                    continue;
                }
                return JsonResult.success().add("result", result);
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
    public JsonResult addARTarget(@RequestParam(value = "image", required = false) MultipartFile image, @RequestParam("name") String name, @RequestParam("size") String size, @RequestParam("outUrl") String outUrl) {
        logger.info("检查参数完整性");
        if (null == image) {
            return JsonResult.fail().add("msg", ConstantInterface.DATA_UPLOAD_ERR);
        }
        try {
            String modelUrl = cUserService.getARModel(1).getArModelUrl();//默认模型
            Map result = AddTarget.addTarget(Base64.getEncoder().encodeToString(image.getBytes()), name, size, modelUrl);
            if ((Integer) result.get("statusCode") == 0) {
                logger.info("success");
                Map target = (Map) result.get("result");
                String targetId = (String) target.get("targetId");
                ArCharManagement arCharManagement = new ArCharManagement();
                arCharManagement.setDeleteNum(0);
                arCharManagement.setArChartImageId(targetId);
                arCharManagement.setArModelId(1);//默认模型id
                arCharManagement.setArThemeId(1);//默认主题id
                arCharManagement.setOutUrl(outUrl);

                String path = ConstantInterface.FILE_BASE_PATH + "cName/" + "image/";
                File dir = new File(path);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                String finalPath = path + image.getOriginalFilename();//key
                File finalDir = new File(finalPath);
                image.transferTo(finalDir);
                String key = "/cName/" + "image/" + image.getOriginalFilename();
                String serverPath = COSUtil.uploadFile(key, finalDir);
                FileTable fileTable = new FileTable();
                fileTable.setFileType("image");
                fileTable.setFileUrl(serverPath);
                fileTable.setFileKey(finalPath);
//              fileTable.setcUserId(1);
                cUserService.addFile(fileTable);
                arCharManagement.setArChartImageUrl(serverPath);
                boolean d = finalDir.delete();
                logger.info("是否删除成功！" + d);
                arCharManagement.setArChartCreateTime(new Timestamp(new Date().getTime()));
                boolean state = cUserService.addARChart(arCharManagement);
                if (state) {
                    return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                } else {
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
    public JsonResult getARTarget(@RequestParam("targetId") String targetId) {
        logger.info("查询识别图信息");
        try {
            Map result = GetTarget.getTarget(targetId);
            if ((Integer) result.get("statusCode") == 0) {
                logger.info("查询成功");
                Map target = (Map) result.get("result");
                return JsonResult.success().add("target", target);
            } else {
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
    public JsonResult updateARTarget(@RequestParam(value = "image", required = false) MultipartFile image, @RequestParam("name") String name,
                                     @RequestParam("size") String size, @RequestParam("active") String active, @RequestParam("targetId") String targetId, @RequestParam("modelId") Integer modelId, @RequestParam("themeId") Integer themeId) {
        System.out.println(name + size + active + targetId + modelId + themeId);
        logger.info("检查参数完整性");
        try {
            logger.info("修改模型和识别图参数");
            ArModelManagement arModelManagement = cUserService.getARModel(modelId);
            ArCharManagement arCharManagement = cUserService.getARChartByTargetId(targetId);
            arCharManagement.setArModelId(arModelManagement.getId());
            arCharManagement.setArThemeId(themeId);
            arCharManagement.setArChartImageId(targetId);
            if (image == null) {
                Map target = GetTarget.getTarget(targetId);
                if ((Integer) target.get("statusCode") == 0) {
                    Map resultTarget = (Map) target.get("result");
                    Map upTarget = UpdateTarget.updateTarget(targetId, (String) resultTarget.get("trackingImage"), active, name, size, arModelManagement.getArModelUrl());
                    if ((Integer) upTarget.get("statusCode") == 0) {
                        boolean state = cUserService.updateARTarget(arCharManagement);
                        if (state) {
                            return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                        }
                        return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                    } else {
                        return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                    }
                } else {
                    return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                }
            } else {
                Map target = GetTarget.getTarget(targetId);
                if ((Integer) target.get("statusCode") == 0) {
                    Map upTarget = UpdateTarget.updateTarget(targetId, Base64.getEncoder().encodeToString(image.getBytes()), active, name, size, arModelManagement.getArModelUrl());
                    if ((Integer) upTarget.get("statusCode") == 0) {
                        boolean state = cUserService.updateARTarget(arCharManagement);
                        if (state) {
                            return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
                        } else {
                            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                        }
                    } else {
                        return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                    }
                } else {
                    return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 识别图删除
     */
    @RequestMapping(value = "removeTarget", method = RequestMethod.POST)
    public JsonResult removeTarget(@RequestParam("targetId") String targetId) {
        logger.info("识别图删除");
        try {
            Map result = RemoveTarget.removeTarget(targetId);
            if ((Integer) result.get("statusCode") == 0) {
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

    /**
     * 获取展示内容
     */
    @RequestMapping(value = "getARModelList", method = RequestMethod.GET)
    public JsonResult getARModelList(@RequestParam("pn") Integer pn, @RequestParam("ps") Integer ps) {
        logger.info("获取展示内容分页参数");
        PageHelper.startPage(pn, ps);
        List<ArModelManagement> list = cUserService.getARModelList();
        if (null == list) {
            return JsonResult.fail().add("msg", ConstantInterface.NO_DATA_MESSAGE);
        } else {
            List list1 = new ArrayList();
            Iterator<ArModelManagement> i = list.iterator();
            while (i.hasNext()) {
                Map r = new HashMap();
                ArModelManagement arModelManagement = i.next();
                Integer num = cUserService.getCountImageNum(arModelManagement.getId());
                r.put("arModelManagement", arModelManagement);
                r.put("num", num);
                list1.add(r);
                arModelManagement.setTestSetting(num);
            }
            PageInfo<ArModelManagement> pageInfo = new PageInfo<>(list1);
            return JsonResult.success().add("pageInfo", pageInfo);
        }
    }

    /**
     * 获取其一展示内容
     */
    @RequestMapping(value = "getARModel", method = RequestMethod.GET)
    public JsonResult getARModel(@RequestParam("id") Integer id) {
        ArModelManagement arModelManagement = cUserService.getARModel(id);
        return JsonResult.success().add("info", arModelManagement);
    }

    /**
     * 展示内容删除
     */
    @RequestMapping(value = "removeARModel", method = RequestMethod.POST)
    public JsonResult removeARModel(@RequestParam("id") Integer id) {
        boolean result = cUserService.removeARModel(id);
        if (result) {
            return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
        } else {
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        }
    }

    /**
     * 展示内容添加
     */
    @RequestMapping(value = "addARModel", method = RequestMethod.POST)
    public JsonResult addARModel(@ModelAttribute("arModel") ArModelManagement arModel) {
        try {
//          arModel.setcUserId(1);//TODO 关联操作
            arModel.setDeleteNum(0);
            arModel.setArModelCreateTime(new Timestamp(new Date().getTime()));
            boolean state = cUserService.addARModel(arModel);
            if (state) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            } else {
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 展示内容修改
     */
    @RequestMapping(value = "updateARModel", method = RequestMethod.POST)
    public JsonResult updateARModel(@ModelAttribute("arModel") ArModelManagement arModel) {
        logger.info("修改操作");
        try {
            boolean result = cUserService.updateARModel(arModel);
            if (result) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            } else {
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }

    }

    /*素材管理*/

    /**
     * 添加素材
     */
    @RequestMapping(value = "uploadModelFile", method = RequestMethod.POST)//model
    public JsonResult uploadVideoFile(@RequestParam(value = "file", required = false) MultipartFile file, @ModelAttribute("fileTable") FileTable fileTable) {
        if (null == file) {
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        }
        try {
            String path = ConstantInterface.FILE_BASE_PATH + "cName/" + fileTable.getFileType() + "/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String finalPath = path + file.getOriginalFilename();//key
            File finalDir = new File(finalPath);
            file.transferTo(finalDir);
            String key = "/cName/" + fileTable.getFileType() + "/" + file.getOriginalFilename();
            String serverPath = COSUtil.uploadFile(key, finalDir);
            fileTable.setFileUrl(serverPath);
            fileTable.setFileKey(finalPath);
//            fileTable.setcUserId(1);
            boolean result = cUserService.addFile(fileTable);
            if (result) {
                boolean state = finalDir.delete();
                logger.info("是否删除成功！" + state);
                return JsonResult.success().add("fileUrl", serverPath).add("id", fileTable.getId());
            }
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    @RequestMapping(value = "uploadFileByTheme", method = RequestMethod.POST)//image
    public JsonResult uploadFile(@RequestParam(value = "file", required = false) MultipartFile file, @ModelAttribute("fileTable") FileTable fileTable) {
        if (null == file) {
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        }
        try {
            String path = ConstantInterface.FILE_BASE_PATH + "cName/" + "image/";
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String finalPath = path + file.getOriginalFilename();//key
            File finalDir = new File(finalPath);
            file.transferTo(finalDir);
            String key = "/cName/" + "image/" + file.getOriginalFilename();
            String serverPath = COSUtil.uploadFile(key, finalDir);
            fileTable.setFileUrl(serverPath);
            fileTable.setFileKey(finalPath);
            fileTable.setFileType("image");
//            fileTable.setcUserId(1);
            boolean result = cUserService.addFile(fileTable);
            if (result) {
                boolean state = finalDir.delete();
                logger.info("是否删除成功！" + state);
                return JsonResult.success().add("fileUrl", serverPath).add("id", fileTable.getId());
            }
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }

    /**
     * 获取素材库列表
     */
    @RequestMapping(value = "getFileList", method = RequestMethod.GET)
    public JsonResult getFileList(@RequestParam("fileType") String fileType) {
        List<FileTable> list = cUserService.getFileList(fileType);
        if (null == list) {
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        } else {
            return JsonResult.success().add("list", list);
        }
    }

    /**
     * 删除素材
     */
    @RequestMapping(value = "deleteFile", method = RequestMethod.POST)
    public JsonResult deleteFile(@RequestParam("id") Integer id) {
        FileTable fileTable = cUserService.selectFileById(id);
        boolean results = COSUtil.deleteFile(fileTable.getFileKey());
        if (results) {
            boolean result = cUserService.deleteFile(id);
            if (result) {
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            }
            return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
        }
        return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
    }

}
