package net.imoran.auto.morwechat.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xinhuashi on 2018/11/8.
 */

public class UpdateUnreadMessage {

    /**
     * wechat_message_unread : {"default":[{"str":"\u201c哈哈哈哈\u201d"}]}
     */

    private WechatMessageUnreadBean wechat_message_unread;

    public WechatMessageUnreadBean getWechat_message_unread() {
        return wechat_message_unread;
    }

    public void setWechat_message_unread(WechatMessageUnreadBean wechat_message_unread) {
        this.wechat_message_unread = wechat_message_unread;
    }

    public static class WechatMessageUnreadBean {
        @SerializedName("default")
        private List<DefaultBean> defaultX;

        public List<DefaultBean> getDefaultX() {
            return defaultX;
        }

        public void setDefaultX(List<DefaultBean> defaultX) {
            this.defaultX = defaultX;
        }

        public static class DefaultBean {
            /**
             * str : “哈哈哈哈”
             */

            private String str;

            public String getStr() {
                return str;
            }

            public void setStr(String str) {
                this.str = str;
            }
        }
    }
}
