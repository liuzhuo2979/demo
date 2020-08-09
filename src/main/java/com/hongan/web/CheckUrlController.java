package com.hongan.web;

import com.alibaba.fastjson.JSON;
import com.hongan.service.HtmlCheck;
import com.hongan.service.UrlCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Controller
public class CheckUrlController {
    @Autowired
    UrlCheck urlCheck;

    @Autowired
    HtmlCheck htmlCheck;

    @RequestMapping(value = "/checkUrl")
    @ResponseBody
    public String checkUrl(String map1, String map2, String urlPath ) throws Exception {
        StringBuffer msg = new StringBuffer();
        Map regMap = JSON.parseObject(map1);
        Map ipMap = JSON.parseObject(map2);
        Set set = ipMap.entrySet();
        Set set1 = regMap.entrySet();

        //通过文件
        if( urlPath != null && !"".equals(urlPath)){
            //文件上传的请求
            List<String> urlList = urlCheck.txt2String(urlPath);
            if( urlList.size() > 0 ){
                for( String sUrl : urlList){
                    if( sUrl != null && !"".equals(sUrl)){
                        for (Iterator iter1 = set1.iterator(); iter1.hasNext(); ) {
                            Map.Entry entry1 = (Map.Entry) iter1.next();
                            String id1 = (String) entry1.getKey();
                            String tableId1 = (String) entry1.getValue();
                            if(id1!=null && !"".equals(id1)){
                                msg.append(htmlCheck.checkHtml(sUrl, id1));
                            }

                            if(tableId1 != null && !"".equals(tableId1) ){
                                msg.append(htmlCheck.checkTable(sUrl, tableId1));
                            }
                            if( "".equals(id1) && "".equals(tableId1) ){
                                msg.append(urlCheck.getUrlCheckMsg(sUrl));
                            }
                        }
                    }

                }
            }
        }

        //表单填写
        for(Iterator iter = set.iterator(); iter.hasNext();) {
            Map.Entry entry = (Map.Entry) iter.next();
            String value = (String) entry.getValue();
            if(value != null && !"".equals(value)){
                for (Iterator iter1 = set1.iterator(); iter1.hasNext(); ) {
                    Map.Entry entry1 = (Map.Entry) iter1.next();
                    String id = (String) entry1.getKey();
                    String tableId = (String) entry1.getValue();
                    if(id!=null && !"".equals(id)){
                        msg.append(htmlCheck.checkHtml(value, id));
                    }
                    if(tableId != null && !"".equals(tableId) ){
                        msg.append(htmlCheck.checkTable(value, tableId));
                    }

                    if( "".equals(id) && "".equals(tableId) ){
                        msg.append(urlCheck.getUrlCheckMsg(value));
                    }
                }
            }

        }
        return msg.toString();
    }

    @RequestMapping(value = "/checkMain")
    public String checkMain(){
        return "textHtml";
    }


}
