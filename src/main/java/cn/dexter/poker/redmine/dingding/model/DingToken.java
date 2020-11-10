package cn.dexter.poker.redmine.dingding.model;

import cn.dexter.poker.redmine.dingding.util.StringUtil;

public class DingToken {

    private String token; //token

    private long expireTime = 7200 * 1000L; //失效时间

    private long createTime; //创建时间

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean isUseful() {
        if (StringUtil.isEmpty(this.createTime, this.token)) {
            return false;
        }
        if ((System.currentTimeMillis() - this.createTime) > expireTime) {
            return false;
        }
        return true;
    }

}
