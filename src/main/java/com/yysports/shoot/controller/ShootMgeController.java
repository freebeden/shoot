package com.yysports.shoot.controller;

import com.yysports.shoot.model.JobTask;
import com.yysports.shoot.model.ReturnT;
import com.yysports.shoot.model.ShootBean;
import com.yysports.shoot.services.ShootMgeService;
import com.yysports.shoot.util.ReadExcelUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/")
public class ShootMgeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShootMgeController.class);
    @Autowired
    ShootMgeService shootMgeService;



    @GetMapping("/")
    public String index(Model model, HttpServletRequest request){
        model.addAttribute("username","world");
        return "home";
    }

    @GetMapping("/loginList")
    public String loginList(Model model, HttpServletRequest request){
        model.addAttribute("username","world");
        return "loginList";
    }

    @GetMapping("/{id}")
    public String findById(Model model, @PathVariable(value = "id") int id){
        ShootBean shootBean = shootMgeService.loginByAccount(id);
        model.addAttribute("username", shootBean.getUsername());
        return "home";
    }


    @RequestMapping("/joblog/pageList")
    @ResponseBody
    public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String jobName,String jobGroup) {
        Map<String, Object> para = new HashMap<>();
        para.put("jobName",jobName);
        para.put("jobGroup",jobGroup);
        para.put("start",start);
        para.put("length",length);
        List<JobTask> list = shootMgeService.queryJobTaskList(para);
        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("recordsTotal", list.size());		// 总记录数
        maps.put("recordsFiltered", list.size());	// 过滤后的总记录数
        maps.put("data", list);
        return maps;
    }

    @RequestMapping("/joblog/changeState")
    @ResponseBody
    public ReturnT<String> changeState(String jobStatus,String uuid) {
        try {
            LOGGER.info("jobStatus={}",jobStatus);
            LOGGER.info("uuid={}",uuid);
            shootMgeService.modifyJobTask(jobStatus,uuid);
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnT.FAIL;
        }
    }


    @RequestMapping(value = "/joblog/uploadFile")
    @ResponseBody
    public ReturnT<String> upload(@RequestParam("file") MultipartFile file,
                                      HttpServletRequest request) throws Exception {
        // 读取上传文件内容
        ReadExcelUtils excelReader = new ReadExcelUtils(file,
                file.getOriginalFilename());
        //更新用户标识，返回登记信息
        List<ShootBean> shootBeanList = excelReader.readExcelList(shootMgeService);
        shootMgeService.insertShootInfo(shootBeanList);
        return ReturnT.SUCCESS;
    }

    @RequestMapping("/joblog/queryState")
    @ResponseBody
    public ReturnT<String> queryResult() {
        try {
            shootMgeService.queryLoginResult();
            return ReturnT.SUCCESS;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return ReturnT.FAIL;
        }
    }

    @RequestMapping("/shoot/shootList")
    @ResponseBody
    public Map<String, Object> shootList(@RequestParam(required = false, defaultValue = "0") int start,
                                        @RequestParam(required = false, defaultValue = "10") int length,
                                        String username,String itemModel,String state,String updateDate) {
        Map<String, Object> retMap = new HashMap();
        Map<String, Object> map = new HashMap<>();
        map.put("username",username);
        map.put("itemModel",itemModel);
        map.put("state",state);
        map.put("updateDate",updateDate);
        List<ShootBean> myList = shootMgeService.queryShootLoginBean(map);
        retMap.put("recordsTotal", myList.size());
        retMap.put("recordsFiltered", myList.size());
        retMap.put("data", myList);
        return retMap;
    }

}
