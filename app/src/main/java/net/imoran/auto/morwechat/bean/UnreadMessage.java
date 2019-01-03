package net.imoran.auto.morwechat.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xinhuashi on 2018/11/2.
 */

public class UnreadMessage {

    @SerializedName("default")
    private List<UnreadString> defaultX;

    public List<UnreadString> getDefaultX() {
        return defaultX;
    }

    public void setDefaultX(List<UnreadString> defaultX) {
        this.defaultX = defaultX;
    }
}
