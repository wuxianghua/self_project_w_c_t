package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 2018/7/24.
 */

public class MQRLogin {
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    private String uuid;
}
