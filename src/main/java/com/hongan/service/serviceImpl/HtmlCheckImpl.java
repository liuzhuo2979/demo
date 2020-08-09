package com.hongan.service.serviceImpl;

import com.hongan.service.HtmlCheck;
import com.hongan.service.UrlCheck;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HtmlCheckImpl implements HtmlCheck {
    @Autowired
    UrlCheck urlCheck;

    /**
     * 校验 html 页面
     * @param url
     * @param id
     * @return11111
     */
    @Override
    public String checkHtml(String url, String id) {
        if( url == null && "".equals(url)){
            return "";
        }
        String msg = urlCheck.getUrlCheckMsg(url);
        String value = "";
        if(msg != null && msg.contains("成功")){
            Document doc = urlCheck.analysisHtml(url);

            if(id != null && !"".equals(id)){
                Element ele = doc.getElementById(id);
                if( ele == null ){
                    return "错误---"+url+"---页面不存在此校验元素ID："+ id +"。\n";
                }else{
                    value = ele.val();
                    if( value == null || "".equals(value)){
                        return "错误---"+url+"---校验元素ID:"+id+"值为空"+"。\n";
                    }
                }
            }
            return "成功---+url---"+url+"。\n";
        }
        return "错误---"+url+"不是有效的url,详细信息请查看log日志。校验元素ID："+id+"。\n";
    }

    /**
     * 校验 table 数据量
     * @param url
     * @param tableId
     * @return
     */
    @Override
    public String checkTable(String url, String tableId){
        if( url == null && "".equals(url)){
            return "";
        }
        String msg = urlCheck.getUrlCheckMsg(url);
        if(msg != null && msg.contains("成功")){
            //获取到解析 html 的dom
            Document doc = urlCheck.analysisHtml(url);
            if(tableId != null && !"".equals(tableId)){
                Element tableEle = doc.getElementById(tableId);
                if( tableEle == null ){
                    return "错误---"+url+"---不存在此table ID："+ tableId +"。\n";
                }else{
                    int num = tableEle.childNodeSize() - 1;
                    if( num > 0 ){
                        return "成功---"+url+"--- tableId:"+tableId+"表格有 "+ num +" 条数据。\n";
                    }else{
                        return "错误---"+url+"--- tableId:"+tableId+"表格没有数据。\n";
                    }
                }

            }
            return "成功---+url---"+url+"。\n";

        }
        return "错误---"+url+"不是有效的url,详细信息请查看log日志。\n";
    }

}
