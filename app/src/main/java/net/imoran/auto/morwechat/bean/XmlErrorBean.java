package net.imoran.auto.morwechat.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by xinhuashi on 2018/7/25.
 */
@XStreamAlias("error")
public class XmlErrorBean {
    @XStreamAlias("message")
    private String message;
    @XStreamAlias("ret")
    private String ret;
    @XStreamAlias("skey")
    private String skey;
    @XStreamAlias("wxsid")
    private String wxsid;
    @XStreamAlias("wxuin")
    private String wxuin;
    @XStreamAlias("pass_ticket")
    private String pass_ticket;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }

    public String getSkey() {
        return skey;
    }

    public void setSkey(String skey) {
        this.skey = skey;
    }

    public String getWxsid() {
        return wxsid;
    }

    public void setWxsid(String wxsid) {
        this.wxsid = wxsid;
    }

    public String getWxuin() {
        return wxuin;
    }

    public void setWxuin(String wxuin) {
        this.wxuin = wxuin;
    }

    public String getPass_ticket() {
        return pass_ticket;
    }

    public void setPass_ticket(String pass_ticket) {
        this.pass_ticket = pass_ticket;
    }

    public String getIsgrayscale() {
        return isgrayscale;
    }

    public void setIsgrayscale(String isgrayscale) {
        this.isgrayscale = isgrayscale;
    }

    private String isgrayscale;
}
