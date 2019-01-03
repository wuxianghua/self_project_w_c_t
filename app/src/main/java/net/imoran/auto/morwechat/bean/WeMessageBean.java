package net.imoran.auto.morwechat.bean;

import java.util.List;

/**
 * Created by xinhuashi on 2018/8/1.
 */

public class WeMessageBean {
    private String userName;
    private String nickName;
    private String remarkName;
    private List<String> contents;
    private String mapUrl;
    private String oriealUrl;
    private String createTime;
    private boolean mIsSelected;
    private String remarkPYInitial;
    private String PYQuanPin;
    private String PYInitial;
    private String RemarkPYQuanPin;
    private boolean isFrom;

    public boolean isFrom() {
        return isFrom;
    }

    public void setFrom(boolean from) {
        isFrom = from;
    }

    public String getOriealUrl() {
        return oriealUrl;
    }

    public void setOriealUrl(String oriealUrl) {
        this.oriealUrl = oriealUrl;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getRemarkPYQuanPin() {
        return RemarkPYQuanPin;
    }

    public void setRemarkPYQuanPin(String remarkPYQuanPin) {
        RemarkPYQuanPin = remarkPYQuanPin;
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

    public void setPYQuanPin(String pYQuanPin) {
        this.PYQuanPin = pYQuanPin;
    }

    public String getRemarkPYInitial() {
        return remarkPYInitial;
    }

    public void setRemarkPYInitial(String remarkPYInitial) {
        this.remarkPYInitial = remarkPYInitial;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public boolean ismIsSelected() {
        return mIsSelected;
    }

    public void setmIsSelected(boolean mIsSelected) {
        this.mIsSelected = mIsSelected;
    }
}
