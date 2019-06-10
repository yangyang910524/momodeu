package com.jeeplus.modules.Interface;

import com.aliyun.oss.OSSClient;
import com.jeeplus.common.config.Global;
import com.jeeplus.common.json.AjaxUserJson;
import com.jeeplus.common.utils.DateUtils;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.core.persistence.Page;
import com.jeeplus.modules.advertisement.entity.Advertisement;
import com.jeeplus.modules.advertisement.service.AdvertisementService;
import com.jeeplus.modules.course.entity.CourseData;
import com.jeeplus.modules.course.entity.CourseInfo;
import com.jeeplus.modules.course.service.CourseDataService;
import com.jeeplus.modules.course.service.CourseInfoService;
import com.jeeplus.modules.coursedataplayrecord.entity.CourseDataPlayRecord;
import com.jeeplus.modules.coursedataplayrecord.service.CourseDataPlayRecordService;
import com.jeeplus.modules.homework.entity.Homework;
import com.jeeplus.modules.notice.entity.Notice;
import com.jeeplus.modules.notice.service.NoticeService;
import com.jeeplus.modules.publiccours.entity.PublicCourse;
import com.jeeplus.modules.publiccours.service.PublicCourseService;
import com.jeeplus.modules.scoreexchange.entity.ScoreExchange;
import com.jeeplus.modules.scoreexchange.service.ScoreExchangeService;
import com.jeeplus.modules.scoreexchangerecord.entity.ScoreExchangeRecord;
import com.jeeplus.modules.scoreexchangerecord.service.ScoreExchangeRecordService;
import com.jeeplus.modules.scorerecord.entity.ScoreRecord;
import com.jeeplus.modules.scorerecord.service.ScoreRecordService;
import com.jeeplus.modules.signuponline.entity.SignUpOnLine;
import com.jeeplus.modules.signuponline.service.SignUpOnLineService;
import com.jeeplus.modules.statistics.entity.Statistics;
import com.jeeplus.modules.statistics.service.StatisticsService;
import com.jeeplus.modules.sys.entity.*;
import com.jeeplus.modules.sys.mapper.UserMapper;
import com.jeeplus.modules.sys.service.ClassesService;
import com.jeeplus.modules.sys.service.CouresOfficeService;
import com.jeeplus.modules.sys.service.SystemService;
import com.jeeplus.modules.sys.service.UserHomeworkService;
import com.jeeplus.modules.sys.utils.DictUtils;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
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
    private ClassesService classesService;
    @Resource
    private SystemService systemService;
    @Resource
    private NoticeService noticeService;
    @Resource
    private AdvertisementService advertisementService;
    @Resource
    private UserHomeworkService userHomeworkService;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PublicCourseService publicCourseService;
    @Resource
    private ScoreRecordService scoreRecordService;
    @Resource
    private SignUpOnLineService signUpOnLineService;
    @Resource
    private CourseDataPlayRecordService courseDataPlayRecordService;
    @Resource
    private CourseDataService courseDataService;
    @Resource
    private ScoreExchangeRecordService scoreExchangeRecordService;
    @Resource
    private CourseInfoService courseInfoService;

    /**
     * @Description 登录
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/login" , method = RequestMethod.POST)
    public AjaxUserJson login(@RequestBody Map<String,String> params,HttpServletRequest request)  {
        AjaxUserJson j = new AjaxUserJson();
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

            User user = userMapper.getByLoginName(new User(null, params.get("username")));
            if(user==null){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg("用户不存在!");
                return j;
            }
            if(!SystemService.validatePassword(params.get("password"),user.getPassword())){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg("密码错误!");
                return j;
            }
            if(!"3".equals(user.getUserType())){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg("无权限访问!");
                return j;
            }
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("登录成功!");
            j.put("user",user);
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
    public AjaxUserJson scoreExchange(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }

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
    public AjaxUserJson scoreRankingByOffice(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            //组装参数
            User user=j.getUser();
            Statistics s=new Statistics();
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
    public AjaxUserJson homeworkStatistics(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            //组装参数
            Statistics s=new Statistics();
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

            j.put("beginTime",s.getBeginTime());
            j.put("endTime",s.getEndTime());
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
    public AjaxUserJson myCourseInfo(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            //组装参数
            CouresOffice co=new CouresOffice();
            if(user==null||user.getOffice()==null||StringUtils.isEmpty(user.getOffice().getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("学生未指定班级!");
                return j;
            }else{
                co.setOffice(user.getOffice());
                co.setUserid(user.getId());
            }

            CourseInfo courseInfo=new CourseInfo();
            courseInfo.setId(params.get("chapterid"));
            co.setCourseInfo(courseInfo);

            CourseInfo ci=new CourseInfo();
            ci.setState("1");
            ci.setLevel(params.get("level"));
            ci.setId(params.get("couresid"));
            co.setFather(ci);

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
     * @Description 我的班级
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/myClasses" , method = RequestMethod.POST)
    public AjaxUserJson myClasses(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            //组装参数
            Statistics s=new Statistics();
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
            Notice notice=new Notice();
            notice.setState("1");
            List<Notice> noticeList=noticeService.findList(notice);
            if(noticeList.size()>0){
                j.put("notice",noticeList.get(0));
            }else{
                j.put("notice",new Notice());
            }
            //同学列表
            j.put("student",systemService.findStudentListByOffice(user.getOffice().getId()));
            //课程级别
            Classes classes = classesService.get(user.getOffice().getId());
            j.put("level",DictUtils.getDictLabel(classes.getLevel(),"bae_course_level",""));
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
    public AjaxUserJson myHomework(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            //组装参数
            UserHomework userHomework=new UserHomework();

            if(params.get("level")==null||"".equals(params.get("level"))
                    || "".equals(DictUtils.getDictLabel(params.get("level"),"bae_course_level",""))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未指定课程级别!");
                return j;
            }

            if(params.get("state")==null||"".equals(params.get("state"))
                    || "".equals(DictUtils.getDictLabel(params.get("state"),"bas_finish_state",""))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未指定作业完成状态!");
                return j;
            }
            userHomework.setStudent(user);
            if("1".equals(params.get("state"))){
                userHomework.setState("1,2");
            }else{
                userHomework.setState(params.get("state"));
            }
            Homework homework=new Homework();
            homework.setCourseLevel(params.get("level"));
            userHomework.setHomework(homework);

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


    /**
     * @Description 用户作业文件上传
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/upload" , method = RequestMethod.POST)
    public AjaxUserJson upload(@RequestParam("file") MultipartFile file,@RequestParam("userid") String userid)  {
        AjaxUserJson j = new AjaxUserJson();
        CommonsMultipartFile cFile=null;
        DiskFileItem fileItem=null;
        InputStream inputStream =null;
        OSSClient ossClient=null;
        try {
            j=systemService.checkUser(userid,j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            if(file==null||file.isEmpty()){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("上传文件不能为空!");
                return j;
            }


            cFile = (CommonsMultipartFile) file;
            fileItem = (DiskFileItem) cFile.getFileItem();
            inputStream = fileItem.getInputStream();
            String fileName=file.getOriginalFilename();
            String suffix="";
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length() - 1))) {
                suffix= fileName.substring(dot).toLowerCase();
            }
            // Endpoint以杭州为例，其它Region请按实际情况填写。

            // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。

            String newFileName=DateUtils.getDate("yyyyMMddHHmmssSSS")+suffix;
            String objectName = "user_homework/"+user.getId()+"/"+ newFileName;

            // 创建OSSClient实例。
            ossClient = new OSSClient(Global.getEndpoint(), Global.getAccessKeyId(), Global.getAccessKeySecret());

            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(Global.getBucketName(), objectName, inputStream);



            j.put("fileName", fileName);
            j.put("newFileName", newFileName);
            j.put("url", Global.getFilePath()+"/"+objectName);
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("上传文件成功!");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        } finally {
            try {
            if(cFile!=null){
                cFile=null;
            }
            if(fileItem!=null){
                fileItem=null;
            }
            if(inputStream!=null){
                inputStream.close();
            }
            if(ossClient!=null){
                // 关闭OSSClient。
                ossClient.shutdown();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return j;
    }

    /**
     * @Description 提交作业
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/saveHomework" , method = RequestMethod.POST)
    public AjaxUserJson saveHomework(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();

            if(params.get("id")==null||"".equals(params.get("id"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未获取到学生作业id!");
                return j;
            }
            if(params.get("fileUrl")==null||"".equals(params.get("fileUrl"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未获取到文件地址!");
                return j;
            }
            if(params.get("startRecordingTime")==null||"".equals(params.get("startRecordingTime"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未获取到开始录音时间!");
                return j;
            }

            UserHomework userHomework= userHomeworkService.get(params.get("id"));
            if(userHomework==null){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg("无法获取到学生作业信息!");
                return j;
            }

            userHomework.setFile(params.get("fileUrl"));
            userHomework.setStartRecordingTime(params.get("startRecordingTime"));
            if("0".equals(userHomework.getState())){
               //首次完成作业
                userHomework.setState("1");
                userHomework.setFinishDate(new Date());
                userHomework.setCreateBy(user);
                userHomework.setUpdateBy(user);
                userHomeworkService.save(userHomework);
                //学生积分加1
                int oldScore=user.getScore();
                user.setScore(oldScore+1);
                systemService.saveUser(user);

                //保存积分修改记录
                ScoreRecord scoreRecord=new ScoreRecord();
                scoreRecord.setUser(user);
                scoreRecord.setOldScore(String.valueOf(oldScore));
                scoreRecord.setNewScore(String.valueOf(user.getScore()));
                User operator=userMapper.get("1");
                scoreRecord.setCreateBy(operator);
                scoreRecord.setUpdateBy(operator);
                scoreRecord.setUser(operator);
                scoreRecordService.save(scoreRecord);
            }else{
                userHomework.setUpdateBy(user);
                //再次修改作业
                userHomeworkService.save(userHomework);
            }

            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");

        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }


    /**
     * @Description 公共课程
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/publicCourse" , method = RequestMethod.POST)
    public AjaxUserJson publicCourse(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            String a = DictUtils.getDictLabel(params.get("type"), "bas_public_course_type", "");
            if(params.get("type")==null||"".equals(params.get("type"))
                    || "".equals(DictUtils.getDictLabel(params.get("type"),"bas_public_course_type",""))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未指定课程类型!");
                return j;
            }

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            PublicCourse publicCourse=new PublicCourse();
            publicCourse.setType(params.get("type"));

            if("1".equals(params.get("isPage").toString())){
                Page<PublicCourse> p=new Page<PublicCourse>();
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

                Page<PublicCourse> pages = publicCourseService.findPage(p,publicCourse);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<PublicCourse> list=publicCourseService.findList(publicCourse);
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
     * @Description 作品排行
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/worksRanking" , method = RequestMethod.POST)
    public AjaxUserJson worksRanking(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        //组装参数
        Statistics s=new Statistics();
        try {

            if(!(params.get("type")==null
                    ||"".equals(params.get("type"))
                    || "".equals(DictUtils.getDictLabel(params.get("type"),"bas_material_type","")))){
                s.setHomeworkType(params.get("type"));
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

                Page<Statistics> pages = statisticsService.worksRanking(p,s);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<Statistics> list=statisticsService.worksRanking(s);
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
     * @Description 作品列表
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/worksList" , method = RequestMethod.POST)
    public AjaxUserJson worksList(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            UserHomework uh=new UserHomework();
            uh.setState("1,2");
            if(params.get("userid")==null||"".equals(params.get("userid"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未指定学生!");
                return j;
            }else{
                uh.setStudent(userMapper.get(params.get("userid")));
            }

            if(!(params.get("type")==null
                    ||"".equals(params.get("type"))
                    || "".equals(DictUtils.getDictLabel(params.get("type"),"bas_material_type","")))){
                Homework homework=new Homework();
                homework.setType(params.get("type"));
                uh.setHomework(homework);
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

                Page<UserHomework> pages = userHomeworkService.findPage(p,uh);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<UserHomework> list=userHomeworkService.findList(uh);
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
     * @Description 作品详情
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/worksDetails" , method = RequestMethod.POST)
    public AjaxUserJson worksDetails(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            if(params.get("userHomeworkId")==null||"".equals(params.get("userHomeworkId"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未获取到学生作业信息!");
                return j;
            }
            UserHomework userHomework=userHomeworkService.get(params.get("userHomeworkId"));
            j.put("userHomework",userHomework);
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
     * @Description 广告管理
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/advertisement" , method = RequestMethod.POST)
    public AjaxUserJson advertisement(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }
            Advertisement advertisement= new Advertisement();
            advertisement.setState("1");
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

                Page<Advertisement> pages = advertisementService.findPage(p,advertisement);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<Advertisement> list=advertisementService.findList(advertisement);
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
     * @Description 修改密码
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/updatePassword" , method = RequestMethod.POST)
    public AjaxUserJson updatePassword(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();

            if(params.get("password")==null||"".equals(params.get("password"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("新密码无效!");
                return j;
            }

            systemService.updatePasswordById(user.getId(),user.getLoginName(),params.get("password"));

            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");

        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 报名接口
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/signUpOnLine" , method = RequestMethod.POST)
    public AjaxUserJson signUpOnLine(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            SignUpOnLine signUpOnLine=new SignUpOnLine();
            signUpOnLine.setName(params.get("name"));
            signUpOnLine.setOld(params.get("old"));
            signUpOnLine.setSex(params.get("sex"));
            signUpOnLine.setPhone(params.get("phone"));
            signUpOnLine.setAddress(params.get("address"));
            signUpOnLineService.save(signUpOnLine);
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 修改头像
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/updatePhoto" , method = RequestMethod.POST)
    public AjaxUserJson updatePhoto(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();

            if(params.get("photo")==null||"".equals(params.get("photo"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("照片不能为空!");
                return j;
            }

            user.setPhoto(params.get("photo"));
            systemService.saveUser(user);
            user=userMapper.get(params.get("userid"));
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");
            j.setUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 根据id获取用户信息
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/getUserByid" , method = RequestMethod.POST)
    public AjaxUserJson getUserByid(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            if(params.get("userid")==null||StringUtils.isEmpty(params.get("userid").toString())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无法获取用户信息!");
                return j;
            }

            User user = userMapper.get(params.get("userid"));
            if(user==null){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg("用户不存在!");
                return j;
            }
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("查询成功!");
            j.setUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 提交课程内容播放记录
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/saveCourseDataPlayRecord" , method = RequestMethod.POST)
    public AjaxUserJson saveCourseDataPlayRecord(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();

            if(params.get("courseDataId")==null||"".equals(params.get("courseDataId"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无法获取课程内容!");
                return j;
            }
            CourseDataPlayRecord courseDataPlayRecord=new CourseDataPlayRecord();
            CourseData courseData = courseDataService.get(params.get("courseDataId"));
            if(courseData==null){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无法获取课程内容!");
                return j;
            }
            courseDataPlayRecord.setCourseData(courseData);
            courseDataPlayRecord.setCreateBy(user);
            courseDataPlayRecord.setUpdateBy(user);
            courseDataPlayRecordService.save(courseDataPlayRecord);
            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");
            j.setUser(user);
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 课程播放记录
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/courseDataPlayRecordList" , method = RequestMethod.POST)
    public AjaxUserJson courseDataPlayRecordList(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            CourseDataPlayRecord courseDataPlayRecord=new CourseDataPlayRecord();
            courseDataPlayRecord.setCreateBy(user);

            CourseData courseData=new CourseData();
            //课程id
            CourseInfo fatherId=new CourseInfo();
            fatherId.setId(params.get("fatherId"));
            courseData.setFather(fatherId);
            //章节id
            CourseInfo courseInfo=new CourseInfo();
            courseInfo.setId(params.get("courseInfoId"));
            courseData.setCourseInfo(courseInfo);
            //内容id
            courseData.setId(params.get("courseDataId"));
            courseDataPlayRecord.setCourseData(courseData);

            if("1".equals(params.get("isPage").toString())){
                Page<CourseDataPlayRecord> p=new Page<CourseDataPlayRecord>();
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
                Page<CourseDataPlayRecord> pages = courseDataPlayRecordService.findPage(p,courseDataPlayRecord);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<CourseDataPlayRecord> list=courseDataPlayRecordService.findList(courseDataPlayRecord);
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
     * @Description 增加/减少积分
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/addScore" , method = RequestMethod.POST)
    public AjaxUserJson addScore(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();

            //保存积分修改记录
            ScoreRecord scoreRecord=new ScoreRecord();
            scoreRecord.setUser(user);

            if(params.get("addScore")==null||"".equals(params.get("addScore"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("积分数值无效!");
                return j;
            }

            User operator=null;
            if(params.get("operatorid")==null||"".equals(params.get("operatorid"))){
                operator=userMapper.get("1");
            }else{
                operator=userMapper.get(params.get("operatorid"));
            }
            if(operator==null||operator.getId()==null||"".equals(operator.getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无效操作人!");
                return j;
            }
            scoreRecord.setCreateBy(operator);
            scoreRecord.setUpdateBy(operator);

            Integer oldScore=user.getScore();
            Integer newScore=user.getScore()+Integer.valueOf(params.get("addScore"));

            user.setScore(newScore);
            systemService.saveUser(user);

            scoreRecord.setOldScore(String.valueOf(oldScore));
            scoreRecord.setNewScore(String.valueOf(newScore));
            scoreRecord.setRemarks(params.get("remark"));
            scoreRecordService.save(scoreRecord);

            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");
            j.setUser(user);
            j.put("oldScore",String.valueOf(oldScore));
            j.put("addScore",String.valueOf(params.get("addScore")));
            j.put("newScore",String.valueOf(newScore));
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 积分兑换
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/scoreExchangeSave" , method = RequestMethod.POST)
    public AjaxUserJson scoreExchangeSave(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();

            ScoreExchange scoreExchange=scoreExchangeService.get(params.get("scoreExchange"));
            if(scoreExchange==null||scoreExchange.getId()==null||"".equals(scoreExchange.getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("未获取到积分兑换物品!");
                return j;
            }

            Integer oldScore=user.getScore();
            Integer scoreExchangeScore=Integer.valueOf(scoreExchange.getScore());
            Integer newScore=user.getScore()-scoreExchangeScore;

            if(newScore<0){
                j.setSuccess(false);
                j.setErrorCode("10003");
                j.setMsg("积分不足!");
                return j;
            }

            //保存积分兑换记录
            ScoreExchangeRecord scoreExchangeRecord=new ScoreExchangeRecord();
            scoreExchangeRecord.setScoreExchange(scoreExchange);
            scoreExchangeRecord.setUser(user);
            scoreExchangeRecord.setCreateBy(user);
            scoreExchangeRecord.setUpdateBy(user);
            scoreExchangeRecordService.save(scoreExchangeRecord);

            //保存积分修改记录
            ScoreRecord scoreRecord=new ScoreRecord();
            scoreRecord.setUser(user);
            scoreRecord.setCreateBy(user);
            scoreRecord.setUpdateBy(user);
            user.setScore(newScore);
            scoreRecord.setRemarks("兑换物品："+scoreExchange.getName());
            systemService.saveUser(user);
            scoreRecord.setOldScore(String.valueOf(oldScore));
            scoreRecord.setNewScore(String.valueOf(newScore));
            scoreRecordService.save(scoreRecord);

            j.setSuccess(true);
            j.setErrorCode("-1");
            j.setMsg("操作成功!");
            j.setUser(user);
            j.put("oldScore",String.valueOf(oldScore));
            j.put("scoreExchangeScore",String.valueOf(scoreExchangeScore));
            j.put("newScore",String.valueOf(newScore));
        } catch (Exception e) {
            e.printStackTrace();
            j.setSuccess(false);
            j.setErrorCode("10001");
            j.setMsg("数据异常!");
        }
        return j;
    }

    /**
     * @Description 积分兑换记录列表
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/scoreExchangeRecordList" , method = RequestMethod.POST)
    public AjaxUserJson scoreExchangeRecordList(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            //组装参数
            ScoreExchangeRecord scoreExchangeRecord=new ScoreExchangeRecord();
            scoreExchangeRecord.setUser(user);

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<ScoreExchangeRecord> p=new Page<ScoreExchangeRecord>();
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

                Page<ScoreExchangeRecord> pages = scoreExchangeRecordService.findPage(p,scoreExchangeRecord);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<ScoreExchangeRecord> list=scoreExchangeRecordService.findList(scoreExchangeRecord);
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
     * @Description 增加接口-根据字典表类型获取列表
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/getDictList" , method = RequestMethod.POST)
    public AjaxUserJson getDictLabels(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            if(params.get("type")==null||"".equals(params.get("type"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无效字典表类型!");
                return j;
            }
            List<DictValue> list = DictUtils.getDictList(params.get("type").toString());
            j.put("list",list==null?new ArrayList<DictValue>():list);
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
     * @Description 增加接口-根据字典表类型获取列表
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/getDictLabel" , method = RequestMethod.POST)
    public AjaxUserJson getDictLabel(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            if(params.get("type")==null||"".equals(params.get("type"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无效字典表类型!");
                return j;
            }
            if(params.get("value")==null||"".equals(params.get("value"))){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("无效字典表值!");
                return j;
            }
            String label = DictUtils.getDictLabel(params.get("value").toString(),params.get("type").toString(),"");
            j.put("label",label);
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
     * @Description 我的章节
     **/
    @ResponseBody
    @RequestMapping(value= "/momo/findChapterList" , method = RequestMethod.POST)
    public AjaxUserJson findChapterList(@RequestBody Map<String,String> params)  {
        AjaxUserJson j = new AjaxUserJson();
        try {
            j=systemService.checkUser(params.get("userid"),j);
            //校验用户信息
            if(!j.isSuccess()){
                return j;
            }
            User user=j.getUser();
            //组装参数
            CourseInfo ci=new CourseInfo();
            if(user==null||user.getOffice()==null||StringUtils.isEmpty(user.getOffice().getId())){
                j.setSuccess(false);
                j.setErrorCode("10002");
                j.setMsg("学生未指定班级!");
                return j;
            }else{
                ci.setOffice(user.getOffice());
            }

            ci.setState("1");
            ci.setLevel(params.get("level"));

            if(params.get("isPage")==null||StringUtils.isEmpty(params.get("isPage").toString())){
                params.put("isPage","0");
            }

            if("1".equals(params.get("isPage").toString())){
                Page<CourseInfo> p=new Page<CourseInfo>();
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

                Page<CourseInfo> pages = courseInfoService.findChapterList(p,ci);
                j.put("count",pages.getCount());
                j.put("pageNo",pages.getPageNo());
                j.put("pageSize",pages.getPageSize());
                j.put("list",pages.getList());
            }else{
                List<CourseInfo> list=courseInfoService.findChapterList(ci);
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

