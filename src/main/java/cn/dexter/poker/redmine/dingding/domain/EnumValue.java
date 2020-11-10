

package cn.dexter.poker.redmine.dingding.domain;

import java.io.Serializable;

public class EnumValue implements Serializable {

    private static final long serialVersionUID = 9160977099890948456L;

    private String            value;

    private String            name;

    /**
     * Creates a new instance of EnumValue.
     *
     * @param value
     * @param name
     */
    public EnumValue(String value, String name) {
        super();
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
