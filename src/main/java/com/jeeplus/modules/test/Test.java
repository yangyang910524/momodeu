package com.jeeplus.modules.test;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yangyang
 * @description 测试
 * @create 2019-04-28 09:01
 **/
public class Test {
    /**
     *	接口内部请求
     * @param
     * @throws Exception
     */
    public Object severTest(){
//        Map<String,String> map = new HashMap<String,String>();
//        String errInfo = "success",str = "",rTime="";
//        try{
//            long startTime = System.currentTimeMillis();
//
//            String type = "true";
//            String requestBody = request.getParameter("requestBody");
//            URL url;
//            if(type.equals("POST")){//请求类型  POST or GET
//                Map<String, String> params = new HashMap<String, String>();
//
//                if(requestBody!=null && !requestBody.equals("")){
//                    String[] paramList = requestBody.split("&");
//
//                    for(String param : paramList){
//                        if(param.split("=").length == 2){
//                            params.put(param.split("=")[0], param.split("=")[1]);
//                        }else{
//                            params.put(param.split("=")[0], "");
//                        }
//                    }
//                }
//                HttpPostTest test = new HttpPostTest(s_url, params);
//
//                str=  test.post();
//            }else{
//                url = new URL(s_url);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.setRequestMethod("GET");
//                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
//                //请求结束时间_毫秒
//                String temp = "";
//                while((temp = in.readLine()) != null){
//                    str = str + temp;
//                }
//
//            }
//
//            long endTime = System.currentTimeMillis();
//            rTime = String.valueOf(endTime - startTime);
//        }
//        catch(Exception e){
//            errInfo = "error";
//            str = e.getMessage();
//        }
//        map.put("errInfo", errInfo);	//状态信息
//        map.put("result", str);			//返回结果
//        map.put("rTime", rTime);		//服务器请求时间 毫秒
//        return map;
        return null;
    }
}