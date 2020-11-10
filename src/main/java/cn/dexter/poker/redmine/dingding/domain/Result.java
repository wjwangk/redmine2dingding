package cn.dexter.poker.redmine.dingding.domain;

import com.alibaba.fastjson.JSON;

public class Result<T> {
    private String respCode = BizStatusEnum.RESPCODE_FAIL.getCode();
    private String respDesc = BizStatusEnum.RESPCODE_FAIL.getMsg();
    private T respData;


    public Result() {
    }

    public Result(String respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    public Result(String respCode, String respDesc, T respData) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.respData = respData;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public T getRespData() {
        return respData;
    }

    public void setRespData(T respData) {
        this.respData = respData;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
