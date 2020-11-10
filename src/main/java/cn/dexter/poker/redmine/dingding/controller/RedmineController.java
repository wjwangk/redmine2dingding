package cn.dexter.poker.redmine.dingding.controller;

import cn.dexter.poker.redmine.dingding.service.RedmineService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author DexterPoker
 * @version 创建时间：2019/1/4 11:22
 */
@RestController
@RequestMapping("redming2dingding")
public class RedmineController {

    @Resource private RedmineService redmineService;

    @RequestMapping("toProject")
    public String dingding(@RequestBody JSONObject json) {
        return redmineService.redmineRequestHandler(json);
    }

//    @DPdoc
//    @RequestMapping("viewWeekReport")
//    public String viewWeekReport(User user){
//        return redmineService.viewWeekReport();
//    }

}
