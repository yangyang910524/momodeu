package com.jeeplus.modules.Interface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.scoreexchange.entity.ScoreExchange;
import com.jeeplus.modules.scoreexchange.service.ScoreExchangeService;
import com.jeeplus.modules.sys.utils.UserUtils;
import com.jeeplus.modules.tools.utils.HttpPostTest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yangyang
 * @description
 * @create 2019-04-28 11:35
 **/
@Controller
public class momo {

    @Resource
    private ScoreExchangeService scoreExchangeService;
    /**
     * @Description 登录
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/login" , method = RequestMethod.POST)
    public AjaxJson login(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        try {
            if(params.get("username")==null||StringUtils.isEmpty(params.get("username").toString())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("用户名不能为空!");
                return j;
            }
            if(params.get("password")==null||StringUtils.isEmpty(params.get("password").toString())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("密码不能为空!");
                return j;
            }
            String url="http://localhost:8081/jeeplus/a/login?__ajax";
            params.put("mobileLogin","true");
            HttpPostTest test = new HttpPostTest(url,params);
            String result=  test.post();
            JSONObject json= JSON.parseObject(result);
            if(!(Boolean) json.get("success")){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg(json.get("msg")==null?"":json.get("msg").toString());
                return j;
            }
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("登录成功!");
            JSONObject body= JSON.parseObject(json.get("body").toString());
            j.put("JSESSIONID",body.get("JSESSIONID")==null?"":body.get("JSESSIONID").toString());
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 登出
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/logout" , method = RequestMethod.POST)
    public AjaxJson logout(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        try {
            if(params.get("JSESSIONID")==null||StringUtils.isEmpty(params.get("JSESSIONID").toString())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("JSESSIONID不能为空!");
                return j;
            }
            URL url=new URL("http://localhost:8081/jeeplus/a/logout;JSESSIONID="+params.get("JSESSIONID").toString()+"?__ajax=true");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            //请求结束时间_毫秒
            String temp = "";
            String result = "";
            while((temp = in.readLine()) != null){
                result = result + temp;
            }
            JSONObject json= JSON.parseObject(result);
            if(!("1".equals(json.get("success")))){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg(json.get("msg")==null?"":json.get("msg").toString());
                return j;
            }
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("退出成功!");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }


    /**
     * @Description 文件上传
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/upload" , method = RequestMethod.POST)
    public AjaxJson logout(@RequestParam("file") MultipartFile file)  {
        AjaxJson j = new AjaxJson();
        Map map = new HashMap();
        map.put("ret", 200);
        map.put("code", 0);
        map.put("msg", "");
        try {
            if(file==null||file.isEmpty()){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("上传文件不能为空!");
                return j;
            }
            String realPath = "C://文件/" ;
            // 转存文件
            FileUtils.createDirectory(realPath);
            file.transferTo(new File( realPath +  file.getOriginalFilename()));
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.put("path",realPath + file.getOriginalFilename());
            j.setMsg("文件上传成功!");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 积分兑换列表
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/scoreExchange" , method = RequestMethod.POST)
    public AjaxJson scoreExchange(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        try {
            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<ScoreExchange> p=new Page<ScoreExchange>();
                if(params.get("pageNo")==null||StringUtils.isEmpty(params.get("pageNo").toString())){
                    p.setPageNo(1);
                }else{
                    p.setPageNo(Integer.valueOf(params.get("pageNo").toString()));
                }
                if(params.get("pageSize")==null||StringUtils.isEmpty(params.get("pageSize").toString())){
                    p.setPageSize(10);
                }else{
                    p.setPageSize(Integer.valueOf(params.get("pageSize").toString()));
                }
                Page<ScoreExchange> pages = scoreExchangeService.findPage(p, new ScoreExchange());
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<ScoreExchange> list=scoreExchangeService.findList(new ScoreExchange());
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }
            j.put("user", UserUtils.getUser());
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("查询成功!");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }
}