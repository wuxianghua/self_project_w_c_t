package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 2018/9/25.
 */

public class BaseRequest {
    private String Uin;
    private String Sid;

    public String getUin() {
        return Uin;
    }

    public void setUin(String uin) {
        Uin = uin;
    }

    public String getSid() {
        return Sid;
    }

    public void setSid(String sid) {
        Sid = sid;
    }

    public String getSkey() {
        return Skey;
    }

    public void setSkey(String skey) {
        Skey = skey;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    private String Skey;
    private String DeviceID;
}
