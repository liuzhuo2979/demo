package com.hongan.web;

import com.hongan.pojo.User;
import com.hongan.service.UrlCheck;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;


@Controller
public class TestController {
    @Autowired
    UrlCheck urlCheck;

    @RequestMapping("/index")
    public String index(){
        String url = "target/classes/templates/url.text";
        List<String> urlList = urlCheck.txt2String(url);
        if(urlList.size() > 0){
            for(String urlParam:urlList){
                String code = urlCheck.getUrlCheckMsg(urlParam);
                if(code != null && "200".equals(code)){
                    urlCheck.analysisHtml(urlParam);
                }
            }
        }
        return "index";
    }

    @RequestMapping("/report")
    public String check(){
        User user = new User("111","111");
        User user1 = new User("222","222");

        List<User> users = new ArrayList();
        users.add(user);
        users.add(user1);

        JRDataSource jrDataSource = new JRBeanCollectionDataSource(users);
        return "";
    }


}
