package cn.dexter.poker.redmine.dingding.exception;

import cn.dexter.poker.redmine.dingding.domain.BizStatusEnum;

public class BizException extends RuntimeException {
    private String code;
    private static final long serialVersionUID = 2359170108745356115L;

    public BizException(String message) {
        super(message);
        this.code = BizStatusEnum.BIZ_ERR.getCode();
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(String code, String message, Object ... args) {
        super(String.format(message, args));
        this.code = code;
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
