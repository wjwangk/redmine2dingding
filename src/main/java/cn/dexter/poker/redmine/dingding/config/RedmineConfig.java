package cn.dexter.poker.redmine.dingding.config;

import cn.dexter.poker.redmine.dingding.model.Project;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author DexterPoker
 * @version 创建时间：2019/1/11 17:41
 */
@Component
@ConfigurationProperties(prefix = "redmine")
public class RedmineConfig {

    private List<Project> project;

    public List<Project> getProject() {
        return project;
    }

    public void setProject(List<Project> project) {
        this.project = project;
    }
}
