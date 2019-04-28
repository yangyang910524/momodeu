package com.jeeplus.modules.test;

import com.jeeplus.modules.tools.utils.HttpPostTest;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yangyang
 * @description 测试
 * @create 2019-04-28 09:01
 **/
public class Test {
    public static void main(String[] args) {
        Test test=new Test();
        test.login("username","admin");
    }

    /**
     *	接口内部请求
     * @param
     * @throws Exception
     */
    public Map login(String username,String password){
        Map<String,String> map = new HashMap<String,String>();
        String errInfo = "success",str = "",rTime="";
        String url="http://localhost:8081/jeeplus/a/login?__ajax";
        try{
            long startTime = System.currentTimeMillis();

            String type = "true";
            Map<String, String> params = new HashMap<String, String>();
            params.put("username",username);
            params.put("password",password);
            params.put("mobileLogin","true");

            HttpPostTest test = new HttpPostTest(url, params);
            str=  test.post();
            long endTime = System.currentTimeMillis();
            rTime = String.valueOf(endTime - startTime);
        }
        catch(Exception e){
            errInfo = "error";
            str = e.getMessage();
        }
        //状态信息
        map.put("errInfo", errInfo);
        //返回结果
        map.put("result", str);
        //服务器请求时间 毫秒
        map.put("rTime", rTime);
        return map;
    }


}