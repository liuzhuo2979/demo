package com.hongan.service.serviceImpl;

import com.hongan.service.UrlCheck;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UrlCheckImpl implements UrlCheck {
    private final static Logger logger = LoggerFactory.getLogger(UrlCheckImpl.class);

    /**
     * 访问 url 返回信息，异常/非200写入日志
     * @param url
     * @return
     */
    @Override
    public String getUrlCheckMsg(String url) {
        String msg = "";
        if(!url.contains("http")){
            url = "http://"+url;
        }
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;//请求响应
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = format.format(new Date());
        try{
            response = httpClient.execute(httpGet);
            //获取当前行响应码的状态
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode == 200){
                msg = "成功---url:"+url+"。\n";
                logger.error(currentTime+" - excute url link is:"+url+ "\r\n success print"+
                        "status is : "+httpStatusConversion(statusCode));
            }else {
                msg = "错误---url:"+url+"访问状态码："+statusCode+"。\n";
                logger.error(currentTime+" - excute url link is:" +url+ " error print \r\n"+
                        "status is :"+httpStatusConversion(statusCode));
            }
        }catch (ClientProtocolException e){
            logger.error(currentTime+" - 404 / URI does not specify a valid host name - excute url link is :"+url+ "\r\n error print"+
                    e.getMessage());
            e.printStackTrace();

        }catch(Exception e){
            logger.error(currentTime+" - excute url link is :"+url+ "\r\n error print"+
                    e.getMessage());
            e.printStackTrace();
        }finally{
            if( msg == null || "".equals(msg)){
                msg =  "错误---url:"+url+"。访问异常，详细信息请查看log日志。\n";
            }
            try{
                response.close();
            }catch (Exception e){
                e.printStackTrace();

            }finally {
                return msg;
            }
        }

    }

    /**
     * 读取配置文件内容
     * @param url
     * @return
     */
    @Override
    public List<String> txt2String(String url) {
        FileInputStream fis = null ;
        InputStreamReader isr = null;
        BufferedReader br = null;
        List<String> urList = new ArrayList<String>();
        String s = null;
        try {
            fis = new FileInputStream(url);
            //防止路径出现乱码
            isr = new InputStreamReader(fis,"UTF-8");
            br = new BufferedReader(isr);//构造一个BufferedReader类来读取文件
            while ((s = br.readLine()) != null) {//使用readLine方法，一次读一行
                urList.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if( br != null || isr != null || fis != null ){
                try {
                    br.close();
                    isr.close();
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return urList;
    }

    /**
     * 字符串分解获取url
     * @param fileInfo
     * @return
     */
    @Override
    public List<String> str2Str(String fileInfo){

         String [] urlArray = fileInfo.split("\\r\\n");
         List<String> list = new ArrayList<>();
         for(int i=0;i<urlArray.length;i++){
             String url = urlArray[i];
             if( url != null && !"".equals(url)){
                 list.add(url);
             }
         }
         return list;
    }

    /**
     * 通过 url 解析 html 页面
     * @param url
     */
    @Override
    public Document analysisHtml(String url) {
        if(!url.contains("http")){
            url = "http://"+url;
        }
        Document doc = null;
        InputStream in = null;
        try {
            URL url1 = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)url1.openConnection();
            in = conn.getInputStream();
            doc = Jsoup.parse(in,"utf-8",url);
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(in != null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return doc;
    }


    /**
     * 状态码
     * @param statusCode
     * @return
     */
    private static String httpStatusConversion(int statusCode){
        String statusInfo = null;
        switch (statusCode){
            case 100 :
                statusInfo = "Continue->继续";
                break;
            case 101 :
                statusInfo = "Switching Protocols->交换协议";
                break;
            case 201 :
                statusInfo = "Created->请求被创建";
                break;
            case 202 :
                statusInfo = "Accepted->请求已接受";
                break;
            case 203 :
                statusInfo = "Non-Authoritative Information->请求是非权威信息";
                break;
            case 204 :
                statusInfo = "No Content->请求没有内容";
                break;
            case 205 :
                statusInfo = "Reset Content->请求重置内容";
                break;
            case 206 :
                statusInfo = "Partial Content->请求只有部分内容";
                break;
            case 300 :
                statusInfo = "Multiple Choices->请求有多种选择";
                break;
            case 301 :
                statusInfo = "Moved Permanently->请求被永久移动";
                break;
            case 302 :
                statusInfo = "Found->请求找到";
                break;
            case 303 :
                statusInfo = "See Other->请求需要参见其他";
                break;
            case 304 :
                statusInfo = "Not Modified->请求未被修改";
                break;
            case 305 :
                statusInfo = "Use Proxy->请求使用代理";
                break;
            case 306 :
                statusInfo = "Unused->请求未使用";
                break;
            case 307 :
                statusInfo = "Temporary Redirect->请求Unused";
                break;
            case 400 :
                statusInfo = "Bad Request->请求错误";
                break;
            case 401 :
                statusInfo = "Unauthorized->请求未经授权";
                break;
            case 402 :
                statusInfo = "Payment Required->请求需付费";
                break;
            case 403 :
                statusInfo = "Forbidden->请求被禁止";
                break;
            case 404 :
                statusInfo = "Not Found->请求没有找到";
                break;
            case 405 :
                statusInfo = "Method Not Allowed->请求方法不允许";
                break;
            case 406 :
                statusInfo = "Not Acceptable->请求不可接受";
                break;
            case 407 :
                statusInfo = "Proxy Authentication Required->请求需要代理身份验证";
                break;
            case 408 :
                statusInfo = "Request Timeout->请求超时";
                break;
            case 409 :
                statusInfo = "Conflict->指令冲突";
                break;
            case 410 :
                statusInfo = "Gone->文档永久地离开了指定的位置";
                break;
            case 411 :
                statusInfo = "Length Required->需要Content-Length头请求";
                break;
            case 412 :
                statusInfo = "Precondition Failed->前提条件失败";
                break;
            case 413 :
                statusInfo = "Request Entity Too Large->请求实体太大";
                break;
            case 414 :
                statusInfo = "Request-URI Too Long->请求URI太长";
                break;
            case 415 :
                statusInfo = "Unsupported Media Type->不支持的媒体类型";
                break;
            case 416 :
                statusInfo = "Requested Range Not Satisfiable->请求的范围不可满足";
                break;
            case 417 :
                statusInfo = "Expectation Failed->期望失败";
                break;
            case 500 :
                statusInfo = "Internal Server Error->内部服务器错误";
                break;
            case 501 :
                statusInfo = "Not Implemented->请求未实现";
                break;
            case 502 :
                statusInfo = "Bad Gateway->错误的网关";
                break;
            case 503 :
                statusInfo = "Service Unavailable->服务不可用";
                break;
            case 504 :
                statusInfo = "Gateway Timeout->网关超时";
                break;
            case 505 :
                statusInfo = "HTTP Version Not Supported->HTTP版本不支持";
                break;
        }
        return statusInfo;
    }


}
