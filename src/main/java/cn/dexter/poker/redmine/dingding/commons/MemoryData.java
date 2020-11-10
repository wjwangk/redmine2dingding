package cn.dexter.poker.redmine.dingding.commons;

import cn.dexter.poker.redmine.dingding.config.RedmineConfig;
import cn.dexter.poker.redmine.dingding.model.DingMessage;
import cn.dexter.poker.redmine.dingding.model.Project;
import cn.dexter.poker.redmine.dingding.model.RedmineTask;
import cn.dexter.poker.redmine.dingding.model.WeekReport;
import cn.dexter.poker.redmine.dingding.util.StringUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class MemoryData {

    @Value("${redmine.weekreport.template}") private String weekreportTemplate;

    private Map<String, WeekReport> weekReportMap = new HashMap<>();

    public void addWeekReport(RedmineTask redmineTask){
        synchronized (this) {
            WeekReport weekReport = weekReportMap.get(redmineTask.getId());
            if (StringUtil.isEmpty(weekReport)) {
                weekReportMap.put(redmineTask.getId(), new WeekReport(redmineTask));
            } else {
                weekReport.setSpendTime(redmineTask.getUpdate(), redmineTask.getCreate());
                weekReport.setPercent(redmineTask.getPercent());
                weekReportMap.put(redmineTask.getId(), weekReport);
            }
        }
    }

    public Map<String, DingMessage> getWeekReport(RedmineConfig redmineConfig, boolean isClear){
        Map<String, DingMessage> dingMessageMap = new HashMap<>();
        Map<String, Integer> dingMessageMapCount = new HashMap<>();
        List<Project> projectList = redmineConfig.getProject();
        synchronized (this) {
            Iterator<String> iterator = weekReportMap.keySet().iterator();
            while (iterator.hasNext()){
                WeekReport weekReport = weekReportMap.get(iterator.next());
                DingMessage dingMessage = dingMessageMap.get(weekReport.getProjectName());
                int count = dingMessageMapCount.getOrDefault(weekReport.getProjectName(), 0);
                if (StringUtil.isEmpty(dingMessage)) {
                    dingMessage = new DingMessage();
                }
                for (Project p : projectList) {
                    if (weekReport.getProjectName().equals(p.getName())) {
                        String txtMsg = MessageFormat.format(weekreportTemplate, count + 1, weekReport.getSubject(),
                                weekReport.getAssignee(), weekReport.getPercent(), weekReport.getSpendTime());
                        handleDingMessage(dingMessage, txtMsg);
                        dingMessageMap.put(weekReport.getProjectName(), dingMessage);
                        dingMessageMapCount.put(weekReport.getProjectName(), dingMessageMapCount.getOrDefault(weekReport.getProjectName(), 0) + 1);
                    }
                }
                if (isClear && "100%".equals(weekReport.getPercent())) {
                    iterator.remove();
                }
            }
        }
        return dingMessageMap;
    }

    private void handleDingMessage(DingMessage dingMessage, String msgLine){
        JSONObject textMsg = new JSONObject();
        JSONObject markdown = new JSONObject();
        if (StringUtil.isEmpty(dingMessage.getGroupMessage())) {
            markdown.put("text", msgLine);
        } else {
            textMsg = JSONObject.parseObject(dingMessage.getGroupMessage());
            markdown = textMsg.getJSONObject("markdown");
            markdown.put("text", markdown.getString("text") + msgLine);
        }
        markdown.put("title", "简要周报");
        JSONObject atJson = new JSONObject();
        atJson.put("isAtAll", "true");
        textMsg.put("at", atJson);
        textMsg.put("markdown", markdown);
        textMsg.put("msgtype", "markdown");
        dingMessage.setTitle("简要周报");
        dingMessage.setGroupMessage(textMsg.toString());
    }

}
