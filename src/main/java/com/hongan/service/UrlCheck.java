package com.hongan.service;


import org.jsoup.nodes.Document;

import java.util.List;

public interface UrlCheck {

    //访问url获取状态码,异常写入日志
    public String getUrlCheckMsg(String url);

    //路径获取文件内容
    public List<String> txt2String(String url);

    //解析Html
    public Document analysisHtml(String url);

    // 字符串分解获取url
    public List<String> str2Str(String fileInfo);



}
