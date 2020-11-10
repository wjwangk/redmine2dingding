package cn.dexter.poker.redmine.dingding.model;

import cn.dexter.poker.redmine.dingding.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author DexterPoker
 * @version 创建时间：2019/1/11 18:05
 */

public class Project {

    private String name;

    private String defaultUrl;

    private String dingdingWebHookUrl;

    private String statusColorMap;

    private String phoneMap;

    private String redmineUrl;

    private String singlePush;

    private String corpId;

    private String corpSecret;

    private String agentId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultUrl() {
        return defaultUrl;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

    public String getDingdingWebHookUrl() {
        return dingdingWebHookUrl;
    }

    public void setDingdingWebHookUrl(String dingdingWebHookUrl) {
        this.dingdingWebHookUrl = dingdingWebHookUrl;
    }

    public String getStatusColorMap() {
        return statusColorMap;
    }

    public void setStatusColorMap(String statusColorMap) {
        this.statusColorMap = statusColorMap;
    }

    public String getPhoneMap() {
        return phoneMap;
    }

    public void setPhoneMap(String phoneMap) {
        this.phoneMap = phoneMap;
    }

    private Map<String, String> phoneMapMap;

    private Map<String, String> statusColorMapMap;

    public String getRedmineUrl() {
        return redmineUrl;
    }

    public void setRedmineUrl(String redmineUrl) {
        this.redmineUrl = redmineUrl;
    }

    public Map<String, String> getPhoneMapMap() {
        return phoneMapMap;
    }

    public void setPhoneMapMap(Map<String, String> phoneMapMap) {
        this.phoneMapMap = phoneMapMap;
    }

    public Map<String, String> getStatusColorMapMap() {
        return statusColorMapMap;
    }

    public void setStatusColorMapMap(Map<String, String> statusColorMapMap) {
        this.statusColorMapMap = statusColorMapMap;
    }

    public String getSinglePush() {
        return singlePush;
    }

    public void setSinglePush(String singlePush) {
        this.singlePush = singlePush;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getCorpSecret() {
        return corpSecret;
    }

    public void setCorpSecret(String corpSecret) {
        this.corpSecret = corpSecret;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public Map<String, String> stringToMap(String s) {
        if (StringUtil.isEmpty(s)) return new HashMap<>();
        String[] colors = s.split(";");
        Map<String, String> map = new HashMap<>();
        for (String c : colors) {
            String[] cSplit = c.split(",");
            map.put(cSplit[0], cSplit[1]);
        }
        return map;
    }

}
