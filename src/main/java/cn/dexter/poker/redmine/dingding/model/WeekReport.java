package cn.dexter.poker.redmine.dingding.model;

import cn.dexter.poker.redmine.dingding.util.DateUtil;
import cn.dexter.poker.redmine.dingding.util.StringUtil;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Getter
public class WeekReport {

    /** 标题 */
    private String subject;

    /** 执行者 */
    private String assignee;

    /** 百分比 */
    private String percent;

    /**  耗时 */
    private String spendTime;

    /** 项目名称 */
    private String projectName;

    /** 创建时间 */
    private String create;

    /** 更新时间 */
    private String update;

    public WeekReport(RedmineTask redmineTask) {
        this.subject = redmineTask.getSubject();
        this.assignee = redmineTask.getAssignee();
        this.percent = redmineTask.getPercent();
        this.projectName = redmineTask.getProjectName();
        setSpendTime(redmineTask.getUpdate(), redmineTask.getCreate());
    }

    public void setSpendTime(String update, String create) {
        if (StringUtil.isEmpty(this.create)) {
            this.create = create;
        }
        this.update = update;
        String temp = String.valueOf((DateUtil.toDate(update, DateUtil.YYYYMMDDHHMMSS).getTime() -
                DateUtil.toDate(this.create, DateUtil.YYYYMMDDHHMMSS).getTime()) / 3600_000 / 24.0);
        this.spendTime = String.valueOf(new BigDecimal(temp).setScale(2, RoundingMode.HALF_UP));
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
