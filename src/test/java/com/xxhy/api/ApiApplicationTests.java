package com.xxhy.api;

import com.sshy.api.App;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class ApiApplicationTests {

    @Test
    public void contextLoads() {
    }



//    @Test
//    public void testA(){
//
//        String cookie1 = null;
//        // 登陆 Url
//        String loginUrl = "http://www.osports.cn/j_security_check";
//        // 需登陆后访问的 Url
//        String dataUrl = "http://www.osports.cn/resource/bag.do?bagId=1441532&cateId=";
//        HttpClient httpClient = new HttpClient();
//        // 模拟登陆，按实际服务器端要求选用 Post 或 Get 请求方式
//        PostMethod postMethod = new PostMethod(loginUrl);
//
//        // 设置登陆时要求的信息，用户名和密码
//        NameValuePair[] data = {new NameValuePair("j_username", "快版权"), new NameValuePair("j_password", "kuaibanquan123")};
//        postMethod.setRequestBody(data);
//        try {
//            // 设置 HttpClient 接收 Cookie,用与浏览器一样的策略
//            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
//            int statusCode = httpClient.executeMethod(postMethod);
//            System.out.println("statusCode:"+statusCode);
//
//            // 获得登陆后的 Cookie
//            Cookie[] cookies = httpClient.getState().getCookies();
//            System.out.println(cookies.length+"123456");
//            StringBuffer tmpcookies = new StringBuffer();
//            for (Cookie c : cookies) {
//                tmpcookies.append(c.toString() + ";");
//                System.out.println("cookies = "+c.toString());
//            }
//            if (statusCode == 200) {//重定向到新的URL
//                System.out.println("模拟登录成功");
//                // 进行登陆后的操作
//                GetMethod getMethod = new GetMethod(dataUrl);
//                // 每次访问需授权的网址时需带上前面的 cookie 作为通行证
//                getMethod.setRequestHeader("cookie", tmpcookies.toString());
//                int a = httpClient.executeMethod(getMethod);
//                String s = getMethod.getResponseBodyAsString();
////                System.out.println(s);
//                // 你还可以通过 PostMethod/GetMethod 设置更多的请求后数据
//                // 例如，referer 从哪里来的，UA 像搜索引擎都会表名自己是谁，
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }



}
