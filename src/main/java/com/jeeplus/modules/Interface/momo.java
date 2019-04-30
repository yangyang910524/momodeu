package com.jeeplus.modules.Interface;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jeeplus.common.json.AjaxJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.FileUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.advertisement.entity.Advertisement;
import com.jeeplus.modules.advertisement.service.AdvertisementService;
import com.jeeplus.modules.course.entity.CourseData;
import com.jeeplus.modules.course.entity.CourseInfo;
import com.jeeplus.modules.course.service.CourseDataService;
import com.jeeplus.modules.notice.entity.Notice;
import com.jeeplus.modules.notice.service.NoticeService;
import com.jeeplus.modules.scoreexchange.entity.ScoreExchange;
import com.jeeplus.modules.scoreexchange.service.ScoreExchangeService;
import com.jeeplus.modules.statistics.entity.Statistics;
import com.jeeplus.modules.statistics.service.StatisticsService;
import com.jeeplus.modules.sys.entity.CouresOffice;
import com.jeeplus.modules.sys.entity.User;
import com.jeeplus.modules.sys.entity.UserHomework;
import com.jeeplus.modules.sys.service.CouresOfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.service.UserHomeworkService;
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
    @Resource
    private StatisticsService statisticsService;
    @Resource
    private CouresOfficeService couresOfficeService;
    @Resource
    private CourseDataService courseDataService;
    @Resource
    private SystemService systemService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private AdvertisementService advertisementService;
    @Resource
    private UserHomeworkService userHomeworkService;

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
            String sessionid= (String) UserUtils.getSession().getId();
            String url="http://localhost:8081/jeeplus/a/login;JSESSIONID="+sessionid+"?__ajax=true";
            params.put("mobileLogin","true");
            HttpPostTest test = new HttpPostTest(url,params);
            String result=  test.post();
            //多次登录登出前次登录
            if(result==null||StringUtils.isEmpty(result)){
                j.setSuccess(false);
                j.setErrorCode("1003");
                j.setMsg("该账号已登录，请注销后再次登录！");
                j.put("JSESSIONID",sessionid);
                return j;
            }
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

    /**
     * @Description 班级积分排名
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/scoreRankingByOffice" , method = RequestMethod.POST)
    public AjaxJson scoreRankingByOffice(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        User user=UserUtils.getUser();
        //组装参数
        Statistics s=new Statistics();
        try {
            if(user==null||user.getOffice()==null||StringUtils.isEmpty(user.getOffice().getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("学生未指定班级!");
                return j;
            }else{
                s.setOffice(user.getOffice());
            }


            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }


            if("1".equals(params.get("isPage").toString())){
                Page<Statistics> p=new Page<Statistics>();
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

                Page<Statistics> pages = statisticsService.scoreRankingByUser(p,s);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<Statistics> list=statisticsService.scoreRankingByUser(s);
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }
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

    /**
     * @Description 本班级学生作业完成量排行榜
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/homeworkStatistics" , method = RequestMethod.POST)
    public AjaxJson homeworkStatistics(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        User user=UserUtils.getUser();
        //组装参数
        Statistics s=new Statistics();
        try {
            if(user==null||user.getOffice()==null||StringUtils.isEmpty(user.getOffice().getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("学生未指定班级!");
                return j;
            }else{
                s.setOffice(user.getOffice());
            }

            if("month".equals(params.get("timeType"))){
                s.setBeginTime(DateUtils.getDate("yyyy-MM")+"-01");
                s.setEndTime(DateUtils.getDate("yyyy-MM-dd"));
            }else if("year".equals(params.get("timeType"))){
                s.setBeginTime(DateUtils.getDate("yyyy")+"-01-01");
                s.setEndTime(DateUtils.getDate("yyyy-MM-dd"));
            }

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<Statistics> p=new Page<Statistics>();
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

                Page<Statistics> pages = statisticsService.homeworkStatistics(p,s);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<Statistics> list=statisticsService.homeworkStatistics(s);
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }

            j.put("beginTime","");
            j.put("endTime","");
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

    /**
     * @Description 我的课程
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/myCourseInfo" , method = RequestMethod.POST)
    public AjaxJson myCourseInfo(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        User user=UserUtils.getUser();
        //组装参数
        CouresOffice co=new CouresOffice();
        CourseInfo ci=new CourseInfo();
        ci.setTitleType("1");
        ci.setState("1");
        co.setCourseInfo(ci);
        try {
            if(user==null||user.getOffice()==null||StringUtils.isEmpty(user.getOffice().getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("学生未指定班级!");
                return j;
            }else{
                co.setOffice(user.getOffice());
            }

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<CouresOffice> p=new Page<CouresOffice>();
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

                Page<CouresOffice> pages = couresOfficeService.findPage(p,co);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<CouresOffice> list=couresOfficeService.findList(co);
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }
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

    /**
     * @Description 课程内容
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/myCourseData" , method = RequestMethod.POST)
    public AjaxJson myCourseData(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        try {
            if(params.get("courseid")==null||StringUtils.isEmpty(params.get("courseid").toString())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未选择课程!");
                return j;
            }

            CourseInfo courseInfo=new CourseInfo();
            courseInfo.setId(params.get("courseid").toString());
            CourseData courseData=new CourseData();
            courseData.setCourseInfo(courseInfo);

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<CourseData> p=new Page<CourseData>();
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

                Page<CourseData> pages = courseDataService.findPage(p,courseData);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<CourseData> list=courseDataService.findList(courseData);
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }
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

    /**
     * @Description 我的班级
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/myClasses" , method = RequestMethod.POST)
    public AjaxJson myClasses()  {
        AjaxJson j = new AjaxJson();
        User user=UserUtils.getUser();
        //组装参数
        Statistics s=new Statistics();
        try {
            if(user==null||user.getOffice()==null||StringUtils.isEmpty(user.getOffice().getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("学生未指定班级!");
                return j;
            }else{
                s.setOffice(user.getOffice());
            }
            //用户信息、班级信息
            j.put("user",user);
            //老师列表
            j.put("teacher",systemService.findTeacherListByOffice(user.getOffice().getId()));
            //通知公告（仅显示最新一条标题、内容）
            List<Notice> noticeList=noticeService.findList(new Notice());
            if(noticeList.size()>0){
                j.put("notice",noticeList.get(0));
            }else{
                j.put("notice",new Notice());
            }
            //同学列表
            j.put("student",systemService.findStudentListByOffice(user.getOffice().getId(),user.getId()));
            //课程列表
            CouresOffice co=new CouresOffice();
            co.setOffice(user.getOffice());
            CourseInfo ci=new CourseInfo();
            ci.setTitleType("1");
            ci.setState("1");
            co.setCourseInfo(ci);
            j.put("courseInfo",couresOfficeService.findList(co));
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

    /**
     * @Description 广告图
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/advertisement" , method = RequestMethod.POST)
    public AjaxJson advertisement(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        try {
            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<Advertisement> p=new Page<Advertisement>();
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

                Page<Advertisement> pages = advertisementService.findPage(p,new Advertisement());
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<Advertisement> list=advertisementService.findList(new Advertisement());
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }
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

    /**
     * @Description 我的作业
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/myHomework" , method = RequestMethod.POST)
    public AjaxJson myHomework(@RequestBody Map<String,String> params)  {
        AjaxJson j = new AjaxJson();
        User user=UserUtils.getUser();
        //组装参数
        UserHomework userHomework=new UserHomework();
        try {
            if(user==null||user.getId()==null||StringUtils.isEmpty(user.getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未获取到用户信息!");
                return j;
            }else{
                userHomework.setUserid(user.getId());
            }

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<UserHomework> p=new Page<UserHomework>();
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

                Page<UserHomework> pages = userHomeworkService.findPage(p,userHomework);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<UserHomework> list=userHomeworkService.findList(userHomework);
                j.put("count",list.size());
                j.put("list",list);
                j.put("pageNo","");
                j.put("pageSize","");
            }
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