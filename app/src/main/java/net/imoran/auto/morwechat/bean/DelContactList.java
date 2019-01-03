package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 2018/7/26.
 */

public class DelContactList {
    private String UserName;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getContactFlag() {
        return ContactFlag;
    }

    public void setContactFlag(int contactFlag) {
        ContactFlag = contactFlag;
    }

    private int ContactFlag;

}
