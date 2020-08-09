package com.hongan.service;

public interface HtmlCheck {

    //id校验 html 页面
    public String checkHtml(String url,String id);

    //tableId 校验 table 的数据
    public String checkTable(String url, String tableId);
}
