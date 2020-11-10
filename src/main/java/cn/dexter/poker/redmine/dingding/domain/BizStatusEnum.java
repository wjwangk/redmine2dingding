package cn.dexter.poker.redmine.dingding.domain;

import java.util.ArrayList;
import java.util.List;

public enum BizStatusEnum {

    SUCCESS("00", "成功"), RESPCODE_FAIL("01", "操作失败"),
    NO_CHANGED_DATA("02", "未更新数据"),
    PARAM_MISS("20", "参数缺少"), PARAM_ERR("21", "参数错误"),
    BIZ_ERR("200", "业务错误"), BIZ_WARN("201", "业务警告"),
    SYS_ERR("999", "系统错误"),
    CHANNEL_UNEXPECTED_ERR("202", "钉钉服务异常"),
    CHANNEL_EMPTY_ERR("203", "钉钉服务响应为空");

    private final String code;

    private final String msg;

    BizStatusEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static boolean isStatus(String code) {
        for (BizStatusEnum s : BizStatusEnum.values()) {
            if (s.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }

    public static List<EnumValue> preferences() {
        List<EnumValue> enumList = new ArrayList<>();
        for (BizStatusEnum s : BizStatusEnum.values()) {
            enumList.add(new EnumValue(s.getCode(), s.getMsg()));
        }
        return enumList;
    }
}
