package com.droplab.Controller.Yongyou.Service;

import com.droplab.Utils.HttpUtils;
import com.droplab.Utils.InfoUtils;
import com.droplab.Utils.ResponseUtils;
import com.droplab.service.BugService;
import org.jsoup.Connection;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 检查存在漏洞的URL是否存在。
 */
public class YongyouCheckeURL extends BugService {
    @Override
    public Object run(String type) {
        try {
            if (params.size() > 0) {
                String url = null;
                String urldns = null;
                Iterator<String> iterator = params.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    if (key.equals("url")) {
                        url = params.get(key);
                    } else if (key.equals("urldns")) {
                        urldns = params.get(key);
                    }
                }
                ArrayList<String> urlArrayList = getURLArrayList();
                Connection.Response response = null;
                ResponseUtils responseUtils = new ResponseUtils();
                StringBuilder stringBuilder = new StringBuilder();
                HttpUtils.set_TIME_OUT(10000);
                for (String content : urlArrayList) {
                    //System.out.println(content);
                    try {
                        if (content.equals("ServiceDispatcherServlet")) {
                            response = HttpUtils.post(url + content, InfoUtils.headers, "");
                        } else {
                            response = HttpUtils.get(url + content, InfoUtils.headers);
                        }
                        int i = response.statusCode();
                        if (i == 200 || i == 302) {
                            stringBuilder.append(url + content + "接口存在\n");
                        }
                    }catch (Exception e){
                        stringBuilder.append(url + content + "接口请求出错\n");
                    }
                }
                responseUtils.setMessage(stringBuilder.toString());
                return responseUtils;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    private ArrayList<String> getURLArrayList(){
        ArrayList<String> urlLists = new ArrayList<>();
        urlLists.add("/servlet/~ic/nc.bs.framework.mx.monitor.MonitorServlet");
        urlLists.add("/servlet/~ic/nc.bs.framework.mx.MxServlet");
        urlLists.add("/servlet/~uapss/com.yonyou.ante.servlet.FileReceiveServlet");
        urlLists.add("/servlet/~ic/nc.document.pub.fileSystem.servlet.DownloadServlet");
        urlLists.add("/servlet/~ic/nc.document.pub.fileSystem.servlet.UploadServlet");
        urlLists.add("/servlet/~ic/nc.document.pub.fileSystem.servlet.DeleteServlet");
        urlLists.add("/servlet/~ic/com.ufida.zior.console.ActionHandlerServlet");
        urlLists.add("/ServiceDispatcherServlet");
        urlLists.add("/servlet/~ic/bsh.servlet.BshServlet");
        urlLists.add("/servlet/~ic/ShowAlertFileServlet");
        return urlLists;
    }
}
