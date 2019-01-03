package net.imoran.auto.morwechat.vui;

/**
 * Created by xinhuashi on 2018/10/16.
 */

public interface VUIConstant {
    String vuiConstantKey = "baseContentEntity";
    String vuiSelectContacts = "wechat_message_select";
    interface Domain{
        String CMD = "cmd";
        String WECHAT = "wechat";
    }
    interface Intention {
        String INSTRUCTING = "instructing";
        String VIEWING = "viewing";
        String SENDING = "sending";
        String UNREAD = "unread";
    }
    interface Type{
        String OPEN = "open_wechat_app";
        String CONTACT = "wechat_contact";
        String MESSAGE = "wechat_message";
        String CLOSEREPORT = "close_wechat_report";
        String OPENREPORT = "open_wechat_report";
        String CLOSE = "close_wechat";
        String WECHAT_LOGOUT = "wechat_logout";
        String LOGOUT = "logout";
        String CLOSENOTIFICATION = "close_wechat_notification";
        String OPENNOTIFICATION = "open_wechat_notification";
        String OPENCONTACTS = "open_wechat_contacts";
        String WECHAT_UNREAD = "wechat_unread";
    }

    interface Action{
        String EXECUTE = "execute";
        String CONFIRM = "confirm";
        String INQUIRE = "inquire";
        String SELECT = "select";
    }
}
