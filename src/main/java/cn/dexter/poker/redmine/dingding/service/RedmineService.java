package cn.dexter.poker.redmine.dingding.service;

import cn.dexter.poker.redmine.dingding.commons.MemoryData;
import cn.dexter.poker.redmine.dingding.config.RedmineConfig;
import cn.dexter.poker.redmine.dingding.model.DingMessage;
import cn.dexter.poker.redmine.dingding.model.Project;
import cn.dexter.poker.redmine.dingding.model.RedmineTask;
import cn.dexter.poker.redmine.dingding.util.HttpUtil;
import cn.dexter.poker.redmine.dingding.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author DexterPoker
 * @version 创建时间：2018/12/20 9:53
 */

@Service
@Slf4j
public class RedmineService {

    @Resource private DingDingService dingDingService;

    @Resource private MemoryData memoryData;

    @Resource private RedmineConfig redmineConfig;

    @Value("${redmine.template}") private String template;

    public String redmineRequestHandler(JSONObject json) {
        try {
            JSONObject root = json.getJSONObject("payload");
            JSONObject issue = root.getJSONObject("issue");
            JSONObject projectJson = issue.getJSONObject("project");
            String projectName = projectJson.getString("name");//项目名称

            if (StringUtil.isEmpty(projectName)) {
                return "获取项目名称失败";
            }
            List<Project> projects = redmineConfig.getProject();
            Project project = null;
            for (Project p : projects) {
                if (projectName.equalsIgnoreCase(p.getName())) {
                    project = p;
                    break;
                }
            }
            if (StringUtil.isEmpty(project)) {
                return projectName + "配置不存在";
            }

            RedmineTask redmineTask = new RedmineTask(json, project.stringToMap(project.getStatusColorMap()), project.getDefaultUrl(),
                    project.getRedmineUrl(), project.getPhoneMapMap());
            DingMessage dingMessage = redmine2DingdingMsgHandler(redmineTask, project, template);
            memoryData.addWeekReport(redmineTask);
            if ("1".equals(project.getSinglePush())) {
                //单独推送给个人
                String s = dingDingService.sendMessageMarkdownByUserName(dingMessage.getUserList(), dingMessage.getTitle(),
                        dingMessage.getMessage(), project.getCorpId(), project.getCorpSecret(), project.getAgentId());
                return s;
            } else {
                //发送到群里：钉钉机器人
                log.info("dingding msg:{}", dingMessage.getGroupMessage());
                String ret = HttpUtil.sendHttpsPost(project.getDingdingWebHookUrl(), dingMessage.getGroupMessage());
                log.info("dingding ret:{}", ret);
                return ret;
            }
        }catch (Exception e) {
            log.error(e.getMessage(), e);
            return e.getMessage();
        }
    }

    public void pushWeekReport(){
        Map<String, DingMessage> weekReportMap = memoryData.getWeekReport(redmineConfig, true);
        for (Project p : redmineConfig.getProject()) {
            DingMessage dingMessage = weekReportMap.get(p.getName());
            if (Objects.nonNull(dingMessage)) {
                continue;
            }
            log.info("dingding msg:{}", dingMessage.getGroupMessage());
            String ret = HttpUtil.sendHttpsPost(p.getDingdingWebHookUrl(), dingMessage.getGroupMessage());
            log.info("dingding ret:{}", ret);
        }
    }

    public String viewWeekReport(){
        return JSON.toJSONString(memoryData.getWeekReport(redmineConfig, false));
    }

    public DingMessage redmine2DingdingMsgHandler(RedmineTask redmineTask, Project project, String template){
        JSONObject textMsg = new JSONObject();
        JSONObject markdown = new JSONObject();
        textMsg.put("msgtype", "markdown");
        String context = "";
        if (CollectionUtils.isEmpty(project.getPhoneMapMap())) {
            context = MessageFormat.format(template, redmineTask.getSubject(), redmineTask.getProjectName(), redmineTask.getDescription(),
                    redmineTask.getStatus(), redmineTask.getTaskType(), redmineTask.getPercent(), redmineTask.getRemarks(),
                    redmineTask.getAuthor(), redmineTask.getPriority(), "@" + redmineTask.getAssignee(), Optional.ofNullable(redmineTask.getWatchers()).orElse(""),
                    redmineTask.getEditor(), redmineTask.getUpdate(), redmineTask.getUrl());
        } else {
            String assignee1 = StringUtil.isEmpty(project.getPhoneMapMap().get(redmineTask.getAssignee())) ?
                    redmineTask.getAssignee() : project.getPhoneMapMap().get(redmineTask.getAssignee());
            assignee1 = "@" + assignee1 + " ";
            context = MessageFormat.format(template, redmineTask.getSubject(), redmineTask.getProjectName(), redmineTask.getDescription(),
                    redmineTask.getStatus(), redmineTask.getTaskType(), redmineTask.getPercent(), redmineTask.getRemarks(),
                    redmineTask.getAuthor(), redmineTask.getPriority(), assignee1, redmineTask.getWatchers(), redmineTask.getEditor(),
                    redmineTask.getUpdate(), redmineTask.getUrl());
        }
        markdown.put("text", context);
        markdown.put("title", redmineTask.getSubject());
        JSONObject atJson = new JSONObject();
        if (!CollectionUtils.isEmpty(project.getPhoneMapMap())) {
            List<String> atList = new ArrayList<>();
            atList.add(project.getPhoneMapMap().get(redmineTask.getAssignee()));
            for (String watcher : redmineTask.getWatchers().split(",")) {
                atList.add(project.getPhoneMapMap().get(watcher));
            }
            atJson.put("atMobiles", atList.toArray());
        }
        atJson.put("isAtAll", "false");
        textMsg.put("at", atJson);
        textMsg.put("markdown", markdown);

        DingMessage dingMessage = new DingMessage();
        dingMessage.setMessage(context);
        dingMessage.setTitle(redmineTask.getSubject());
        dingMessage.setUserList(redmineTask.getWatchersName());
        dingMessage.setGroupMessage(textMsg.toString());

        return dingMessage;
    }

}
