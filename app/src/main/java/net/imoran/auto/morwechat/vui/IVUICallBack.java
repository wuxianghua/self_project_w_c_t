package net.imoran.auto.morwechat.vui;

import android.content.Intent;

/**
 * Created by xinhuashi on 2018/10/16.
 */

public interface IVUICallBack {

    void vuiOpenApp();

    void openChatWin(Intent intent);

    void sendMessageToFri(Intent intent);

    void showAllFriends(Intent intent);

    void sendFriContent(String query,String content);

    void sendExecuteContent(String queryId);

    void inquerySendPerson(String query);

    void stopWechatPlay();

    void openWechatPlay();

    void vuiCloseApp();

    void vuiLogout();

    void closeNotification();

    void openNotification();

    void openContacts(Intent intent);

    void finishEmptyActivity();

    void lookUnreadMsg();
}
