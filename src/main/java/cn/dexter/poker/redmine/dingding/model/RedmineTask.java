package cn.dexter.poker.redmine.dingding.model;

import cn.dexter.poker.redmine.dingding.util.DateUtil;
import cn.dexter.poker.redmine.dingding.util.StringUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Getter
public class RedmineTask {

    private String id;

    /** 任务标题 */
    private String subject;

    /** 任务描述 */
    private String description = "";

    /** 创建时间 */
    private String create;

    /** 修改时间 */
    private String update;

    /** 项目名称 */
    private String projectName;

    /** 任务状态 */
    private String status;

    /** 项目类型 */
    private String taskType;

    /** 备注 */
    private String remarks = "";

    /** 作者，分发任务的人 */
    private String author;

    /** 优先级 */
    private String priority;

    /** 分配到任务的人， 执行者 */
    private String assignee;

    /** 跟踪者，监工 */
    private String watchers;

    /** 监工名称 用于钉钉 */
    private String watchersName;

    /** 进度 */
    private String percent;

    /** redmine 任务地址 */
    private String url;

    /** 编辑人 修改人 */
    private String editor = "";

    /**
    * @description 转化成实体
    * @author DexterPoker
    * @date 14:30 2020/2/5
    * @param json
    * @param statusColorMap
    * @param oldUrl
    * @param redmineUrl
    * @param mobileMap
    * @return
    */
    public RedmineTask(JSONObject json, Map<String, String> statusColorMap, String oldUrl,
                       String redmineUrl, Map<String, String> mobileMap) {
        JSONObject root = json.getJSONObject("payload");
        JSONObject issue = root.getJSONObject("issue");
        JSONObject journal = root.getJSONObject("journal");
        String note = "";
        String editor = "";
        if (!StringUtil.isEmpty(journal)) {
            note  = journal.getString("notes");//备注
            this.remarks = note.replaceAll("\\r\\n", "\n > ");
            JSONObject editorJson = journal.getJSONObject("author");
            editor = editorJson.getString("lastname") + editorJson.getString("firstname");
            this.editor = "<font color=" + statusColorMap.get("编辑人") + ">" + editor + "</font>";
            this.create = timezone2DateString(journal.getString("created_on"));
        } else {
            this.update = DateUtil.formatTime();
            if (StringUtil.isEmpty(this.create)) {
                this.create = this.update;
            }
        }
        this.subject = issue.getString("subject");//标题
        String desc = issue.getString("description");//描述
        if (!StringUtil.isEmpty(desc)) {
            this.description = desc.replaceAll("\\r\\n", "\n > ");
        }
        JSONObject statusJson = issue.getJSONObject("status");
        String status = statusJson.getString("name");//状态
        if (statusColorMap.containsKey(status)) {
            this.status = "<font color=" + statusColorMap.get(status) + ">" + status + "</font>";
        }
        JSONObject trackerJson = issue.getJSONObject("tracker");
        this.taskType = trackerJson.getString("name");//issue类型
        JSONObject authorJson = issue.getJSONObject("author");
        this.author = authorJson.getString("lastname") + authorJson.getString("firstname");//派发人
        JSONObject assigneeJson = issue.getJSONObject("assignee");//接任务者
        JSONObject jsonObject = Optional.of(assigneeJson).orElse(new JSONObject(1));
        this.assignee = "<font color=" + statusColorMap.get("执行人") + ">" + jsonObject.getString("lastname") + jsonObject.getString("firstname")+ "</font>";
        JSONArray watchersJson = issue.getJSONArray("watchers");//跟踪者
        StringBuilder watchers = new StringBuilder();
        StringBuilder watcherName = new StringBuilder();
        for (Object watcher : watchersJson) {
            JSONObject watcherJson = JSONObject.parseObject(watcher.toString());
            if (CollectionUtils.isEmpty(mobileMap)) {
                watchers.append(watcherJson.getString("lastname")).append(watcherJson.getString("firstname")).append(",");
                watcherName.append(watcherJson.getString("lastname")).append(watcherJson.getString("firstname")).append(",");
            } else {
                String name = watcherJson.getString("lastname") + watcherJson.getString("firstname");
                name = StringUtil.isEmpty(mobileMap.get(name)) ? name : mobileMap.get(name);
                watchers.append("@").append(name).append(",");
                watcherName.append(watcherJson.getString("lastname")).append(watcherJson.getString("firstname")).append(",");
            }
        }
        if (!StringUtil.isEmpty(watchers)) {
            this.watchers = watchers.substring(0, watchers.length() - 1);
        }
        watcherName.append(assignee).append(",");
        watcherName.append(author).append(",");
        this.watchersName = watcherName.toString();
        JSONObject priorityJson = issue.getJSONObject("priority");
        this.priority = priorityJson.getString("name");//优先级
        JSONObject projectJson = issue.getJSONObject("project");
        this.projectName = projectJson.getString("name");//项目名称
        String url = root.getString("url");
        this.url = url.replace(oldUrl, redmineUrl);//置换redmine地址
        this.percent = StringUtil.isEmpty(issue.getString("done_ratio")) ? "0" : issue.getString("done_ratio") + "%";//进度百分比
        this.update = DateUtil.formatTime();
        this.id = issue.getString("id");
    }

    private static String timezone2DateString(String timezone){//可能是redmine系统时间对不上，做了时间处理
        timezone = timezone.replace("T", " ");
        timezone = timezone.substring(0, timezone.indexOf("."));
        Date date = DateUtil.addSecondsToDate(DateUtil.toDate(timezone, DateUtil.YYYYMMDDHHMMSS), 8 * 3600);
        return DateUtil.format(date, DateUtil.YYYYMMDDHHMMSS);
    }

}
