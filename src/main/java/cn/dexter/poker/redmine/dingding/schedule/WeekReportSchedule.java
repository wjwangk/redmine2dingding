package cn.dexter.poker.redmine.dingding.schedule;

import cn.dexter.poker.redmine.dingding.service.RedmineService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WeekReportSchedule {

    @Resource private RedmineService redmineService;

    @Scheduled(cron = "0 0 21 * * 6")
    public void pushWeekReport(){
        redmineService.pushWeekReport();
    }

}
