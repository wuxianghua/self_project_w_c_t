package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhuashi on 2018/9/17.
 */

public class WechatMessageBean {
    private String remark_name;
    private String nickname;
    private String contact_id;
    private String icon_url;
    private String chat_type;
    private int message_type;
    private String message_content;
    private String contact_id_ingroup;
    private String contact_name_ingroup;
    private float longitude;
    private float latitude;

    public String getRemark_name() {
        return remark_name;
    }

    public void setRemark_name(String remark_name) {
        this.remark_name = remark_name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public String getContact_id_ingroup() {
        return contact_id_ingroup;
    }

    public void setContact_id_ingroup(String contact_id_ingroup) {
        this.contact_id_ingroup = contact_id_ingroup;
    }

    public String getContact_name_ingroup() {
        return contact_name_ingroup;
    }

    public void setContact_name_ingroup(String contact_name_ingroup) {
        this.contact_name_ingroup = contact_name_ingroup;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
