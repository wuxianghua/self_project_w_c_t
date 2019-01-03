package net.imoran.auto.morwechat.bean;

import java.util.List;

/**
 * Created by xinhuashi on 2018/9/17.
 */

public class UpdateMessageBean {
    private List<WechatMessageBean> wechat_message;

    public List<WechatMessageBean> getWechat_message() {
        return wechat_message;
    }

    public void setWechat_message(List<WechatMessageBean> wechat_message) {
        this.wechat_message = wechat_message;
    }
}
