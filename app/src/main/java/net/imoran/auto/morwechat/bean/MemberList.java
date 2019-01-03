package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 2018/7/26.
 */

public class MemberList {
    private int Uin;
    private String UserName;
    private String NickName;
    private long AttrStatus;
    private String PYInitial;

    public int getUin() {
        return Uin;
    }

    public void setUin(int uin) {
        Uin = uin;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public long getAttrStatus() {
        return AttrStatus;
    }

    public void setAttrStatus(long attrStatus) {
        AttrStatus = attrStatus;
    }

    public String getPYInitial() {
        return PYInitial;
    }

    public void setPYInitial(String PYInitial) {
        this.PYInitial = PYInitial;
    }

    public String getPYQuanPin() {
        return PYQuanPin;
    }

    public void setPYQuanPin(String PYQuanPin) {
        this.PYQuanPin = PYQuanPin;
    }

    public String getRemarkPYInitial() {
        return RemarkPYInitial;
    }

    public void setRemarkPYInitial(String remarkPYInitial) {
        RemarkPYInitial = remarkPYInitial;
    }

    public String getRemarkPYQuanPin() {
        return RemarkPYQuanPin;
    }

    public void setRemarkPYQuanPin(String remarkPYQuanPin) {
        RemarkPYQuanPin = remarkPYQuanPin;
    }

    public int getMemberStatus() {
        return MemberStatus;
    }

    public void setMemberStatus(int memberStatus) {
        MemberStatus = memberStatus;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String displayName) {
        DisplayName = displayName;
    }

    public String getKeyWord() {
        return KeyWord;
    }

    public void setKeyWord(String keyWord) {
        KeyWord = keyWord;
    }

    private String PYQuanPin;
    private String RemarkPYInitial;
    private String RemarkPYQuanPin;
    private int MemberStatus;
    private String DisplayName;
    private String KeyWord;
}
