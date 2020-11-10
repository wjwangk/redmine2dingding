package cn.dexter.poker.redmine.dingding.service;

import cn.dexter.poker.redmine.dingding.domain.BizStatusEnum;
import cn.dexter.poker.redmine.dingding.domain.Result;
import cn.dexter.poker.redmine.dingding.model.DingToken;
import cn.dexter.poker.redmine.dingding.util.HttpUtil;
import cn.dexter.poker.redmine.dingding.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by DexterPoker on 2018/3/12.
 * @description 钉钉接口相关，这里用到接口可能较老
 *  可参考钉钉文档https://ding-doc.dingtalk.com/
 */
@Service
public class DingDingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DingDingService.class);

    private Map<String, String> userMap = null;//存放用户姓名和userid

    private Map<String, String> userPhoneMap;//存在姓名、手机号

    public Map<String, String> getUserMap() {
        return userMap;
    }

    public Map<String, String> getUserPhoneMap(){
        return userPhoneMap;
    }

    private static DingToken dingToken = new DingToken();

    /**
     * 获取钉钉token
     * @return token
     */
    private Result<JSONObject> getToken(String corpId, String corpSecret){
        if (!dingToken.isUseful()) {
            Map<String ,String> params = new HashMap<>();
            params.put("corpid", corpId);
            params.put("corpsecret", corpSecret);
//            String ret = HttpUtil.sendPost("https://oapi.dingtalk.com/gettoken", params);
            String ret = HttpUtil.sendGet("https://oapi.dingtalk.com/gettoken?corpid=" + corpId + "&corpsecret=" + corpSecret);
            LOGGER.info("获取token返回:" + ret);
            Result<JSONObject> result = bulidResult(ret);
            if (BizStatusEnum.SUCCESS.getCode().equals(result.getRespCode())) {
                dingToken.setCreateTime(System.currentTimeMillis());
                dingToken.setToken(result.getRespData().getString("access_token"));
                JSONObject json = new JSONObject();
                json.put("token", result.getRespData().getString("access_token"));
                result.setRespData(json);
            }
            return result;
        } else {
            JSONObject json = new JSONObject();
            json.put("token", dingToken.getToken());
            return new Result<>(BizStatusEnum.SUCCESS.getCode(), BizStatusEnum.SUCCESS.getMsg(), json);
        }
    }

    /**
    * @description 获取用户列表
    * @author DexterPoker
    * @date 10:05 2020/2/5
    * @param corpId
    * @param corpSecret
    */
    public void getUserList(String corpId, String corpSecret){
        Result<JSONObject> getTokenResult = getToken(corpId, corpSecret);
        if (BizStatusEnum.SUCCESS.getCode().equals(getTokenResult.getRespCode())) {
            String ret = HttpUtil.sendGet("https://oapi.dingtalk.com/user/list?access_token=" + getTokenResult.getRespData().getString("token")
                    + "&department_id=1");
            Result<JSONObject> result = bulidResult(ret);
            if (BizStatusEnum.SUCCESS.getCode().equals(result.getRespCode())) {
                JSONArray jsonArray = result.getRespData().getJSONArray("userlist");
                for (Object o : jsonArray) {
                    JSONObject json = JSONObject.parseObject(o.toString());
                    if (CollectionUtils.isEmpty(userMap)) {
                        userMap = new HashMap<>();
                    }
                    if (CollectionUtils.isEmpty(userPhoneMap)) {
                        userPhoneMap = new HashMap<>();
                    }
                    userMap.put(json.getString("name"), json.getString("userid"));
                    userPhoneMap.put(json.getString("name"), json.getString("mobile"));
                }
            }
        }
    }

    /**
    * @description 发送Markdown消息
    * @author DexterPoker
    * @date 10:07 2020/2/5
    * @param userIdGroup
    * @param title
    * @param text
    * @param corpId
    * @param corpSecret
    * @param agentId
    * @return java.lang.String
    */
    private String sendMessageMarkdown(String userIdGroup, String title, String text, String corpId, String corpSecret, String agentId){
        Result<JSONObject> getTokenResult = getToken(corpId, corpSecret);
        if (BizStatusEnum.SUCCESS.getCode().equals(getTokenResult.getRespCode())) {
            Map<String, String> params = new HashMap<>();
            params.put("touser", userIdGroup);
            params.put("agentid", agentId);
            params.put("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.put("title", title);
            markdown.put("text", text);
            params.put("markdown", JSON.toJSONString(markdown));
            LOGGER.info("发送数据:{}", markdown);
            String ret = HttpUtil.sendHttpsPost("https://oapi.dingtalk.com/message/send?access_token=" + getTokenResult.getRespData().getString("token"), JSON.toJSONString(params));
            LOGGER.info("发送markdown返回:" + ret);
            return ret;
        } else {
            return getTokenResult.toString();
        }
    }

    /**
    * @description 通过人名匹配后发送Markdown消息
    * @author DexterPoker
    * @date 10:08 2020/2/5
    * @param userNames
    * @param title
    * @param text
    * @param corpId
    * @param corpSecret
    * @param agentId
    * @return java.lang.String
    */
    public String sendMessageMarkdownByUserName(String userNames, String title, String text, String corpId, String corpSecret, String agentId){
        Result<JSONObject> getTokenResult = getToken(corpId, corpSecret);
        if (BizStatusEnum.SUCCESS.getCode().equals(getTokenResult.getRespCode())) {
            String[] users = userNames.split(",");
            Map<String, String> usersMap = getUserMap();
            if (CollectionUtils.isEmpty(usersMap)) {
                getUserList(corpId, corpSecret);
                usersMap = getUserMap();
            }
            for (String user : users) {
                if (!usersMap.containsKey(user)) {
                    getUserList(corpId, corpSecret);
                }
            }
            StringBuffer stringBuffer = new StringBuffer();
            usersMap = getUserMap();
            for (String user : users) {
                stringBuffer.append(usersMap.get(user));
                stringBuffer.append("|");
            }
            if (stringBuffer.length() > 0) {
                String s = sendMessageMarkdown(stringBuffer.substring(0, stringBuffer.length() - 1), title, text, corpId, corpSecret, agentId);
                return s;
            }
        }
        return "ex";
    }

    private Result<JSONObject> bulidResult(String ret){
        if (StringUtil.isEmpty(ret)) {
            return new Result(BizStatusEnum.CHANNEL_EMPTY_ERR.getCode(), BizStatusEnum.CHANNEL_EMPTY_ERR.getMsg());
        } else {
            try {
                JSONObject json = JSONObject.parseObject(ret);
                Result result = new Result();
                JSONObject data = new JSONObject();
                for (Map.Entry entry : json.entrySet()) {
                    if ("errcode".equals(entry.getKey())) {
                        result.setRespCode("0".equals(entry.getValue().toString()) ? BizStatusEnum.SUCCESS.getCode() : entry.getValue().toString());
                    } else if("errmsg".equals(entry.getKey())) {
                        result.setRespDesc(entry.getValue().toString());
                    } else {
                        data.put(entry.getKey().toString(), entry.getValue());
                    }
                }
                result.setRespData(data);
                return result;
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
                return new Result<>(BizStatusEnum.CHANNEL_UNEXPECTED_ERR.getCode(), BizStatusEnum.CHANNEL_UNEXPECTED_ERR.getMsg());
            }
        }
    }

}
