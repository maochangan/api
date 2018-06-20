package com.sshy.api.controller;

import com.sshy.api.bean.SimpleUser;
import com.sshy.api.service.SimpleUserService;
import com.sshy.api.utils.ARutils.ResultInfo;
import com.sshy.api.utils.ARutils.WebAR;
import com.sshy.api.utils.ConstantInterface;
import com.sshy.api.utils.JsonResult;
import com.sshy.api.utils.WxAuthUtil;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@CrossOrigin(origins = "*" , maxAge = 3600)
@RestController
@RequestMapping(value = "user")
public class SimpleUserController {

    Logger logger = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private SimpleUserService simpleUserService;


    @RequestMapping(value = "wxLogin", method = RequestMethod.GET)
    public void wxLogin(HttpServletResponse response, String companyUnionId, HttpSession session) {
        session.setAttribute("unionId", companyUnionId);
        logger.info("登录接口");
        String backUrl = "https://wutian.zijimedia.cc/analysis/return_url";
        String url = null;
        try {
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + WxAuthUtil.APP_ID
                    + "&redirect_uri=" + backUrl
                    + "&response_type=code"
                    + "&scope=snsapi_userinfo"
                    + "&state=STATE#wechat_redirect";
            logger.info("forward重定向地址{" + url + "}");
            response.sendRedirect(url);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    @RequestMapping(value = "return_url", method = RequestMethod.GET)
    public JsonResult wxCallBack(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        try {
            logger.info("微信回调函数");
            String unId = (String) session.getAttribute("unionId");
            if (null == unId) {
                return JsonResult.fail().add("msg", ConstantInterface.DATA_UPLOAD_ERR);
            }
            String code = request.getParameter("code");
            logger.info("code:" + code);
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WxAuthUtil.APP_ID
                    + "&secret=" + WxAuthUtil.APPSECRET
                    + "&code=" + code
                    + "&grant_type=authorization_code";
            logger.info("地址：" + url);
            JSONObject jsonObject = WxAuthUtil.doGet(url);
            logger.info("json" + jsonObject.toString());
            String openid = jsonObject.getString("openid");
            String access_token = jsonObject.getString("access_token");
            logger.info("notFind" + access_token + "------------");
            String refresh_token = jsonObject.getString("refresh_token");
            logger.info("验证tokens是否失效");
            String chickUrl = "https://api.weixin.qq.com/sns/auth?access_token=" + access_token + "&openid=" + openid;
            JSONObject chickuserInfo = WxAuthUtil.doGet(chickUrl);
            if (0 != chickuserInfo.getInt("errcode")) {
                String refreshTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + openid + "&grant_type=refresh_token&refresh_token=" + refresh_token;
                JSONObject refreshInfo = WxAuthUtil.doGet(chickUrl);
                access_token = refreshInfo.getString("access_token");
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token
                    + "&openid=" + openid
                    + "&lang=zh_CN";
            JSONObject userInfo = WxAuthUtil.doGet(infoUrl);
            SimpleUser user = new SimpleUser();
            user.setsOpenId(userInfo.getString("openid"));
            user.setsNickName(userInfo.getString("nickname"));
            user.setsGender(userInfo.getInt("sex"));
            user.setsProvince(userInfo.getString("province"));
            user.setsCity(userInfo.getString("city"));
            user.setsCountry(userInfo.getString("country"));
            user.setsHeadImgUrl(userInfo.getString("headimgurl"));
            user.setsUnionId(userInfo.getString("unionid"));
            user.setOrderCompanyUnionId(unId);
            boolean flag = true;
//            boolean flag = simpleUserService.insertUser(user);
            if (flag) {
                session.setAttribute("open_id", openid);
                Cookie cookie = new Cookie("open_id", openid);
                cookie.setPath("/");
                cookie.setMaxAge(30 * 60);
                response.addCookie(cookie);
                response.setContentType("text/html;charset=UTF-8");
                response.getWriter().print(
                        "<html><head><title></title></head>" +
                                "<body>" +
                                "<script>" +
                                "window.location.href = 'https://wutian.zijimedia.cc/webview/' " +
                                "</script>" +
                                "</body>");
                return JsonResult.success().add("msg", ConstantInterface.SUCCESS_MSG);
            } else {
                return JsonResult.fail().add("msg", ConstantInterface.FAIL_MSG);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return JsonResult.fail().add("err" , "err");
        }
    }


    /**
     * 识别图
     */
    @RequestMapping(value = "getArMeta", method = RequestMethod.POST)
    public JsonResult getARMeta(String image){
        WebAR webAR = new WebAR();
        try {
            ResultInfo info = webAR.recognize(image);
            if (info.getStatusCode() == 0) {
                logger.info("识别到目标");

                System.out.println(info.getResult().getTarget().getTargetId());
                return JsonResult.success().add("target", info.getResult().getTarget());
            } else {
                logger.info("未识别到目标");
                return JsonResult.fail().add("msg", info.getResult().getMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            return JsonResult.fail().add("err", "err");
        }
    }
}
