package net.imoran.auto.morwechat.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.util.Log;

import net.imoran.auto.morwechat.bean.Quest;
import net.imoran.auto.morwechat.bean.WechatStatusBean;
import net.imoran.auto.morwechat.manager.WeChatClient;
import net.imoran.auto.morwechat.utils.NetUtil;
import net.imoran.auto.morwechat.vui.VUIConstant;
import net.imoran.auto.morwechat.vui.VUIManager;
import net.imoran.sdk.bean.base.BaseContentEntity;
import net.imoran.sdk.util.ReplyUtils;
import net.imoran.sdk.wx.WechatClient;

/**
 * Created by xinhuashi on 2018/10/9.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    //网络状态监听
    public static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private BaseContentEntity baseContentEntity;
    public static NetEvent event;

    public IntentFilter getIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECTIVITY_CHANGE);
        return filter;
    }

    public void setNetEvent(NetEvent mEvent) {
        Log.e("networkChange","change  event" + mEvent.toString() );
        event = mEvent;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        //如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            int netWorkState = NetUtil.getNetWorkState(context);
            Log.e("networkChange", "change");
            if (event != null) {
                Log.e("networkChange", "change1111");
                event.onNetChange(netWorkState);
            }
        }else if (intent.getAction().equals(ACTION_BOOT)) {
            sengWeChatLoginStatus(context,"logoff",null,null,null, "wechat_logoff",null);
        }
    }

    public void sengWeChatLoginStatus(Context context,String loginStatus,String skey,String pass_ticket,String baseUrl,String service,String contactList) {
        Quest quest = new Quest();
        quest.wechat_status = loginStatus;
        quest.baseUrl = baseUrl;
        quest.pass_ticket = pass_ticket;
        quest.skey = skey;
        quest.contactList = contactList;
        WechatStatusBean bean = new WechatStatusBean();
        bean.wechat_status_change = quest;
        WeChatClient.getInstance(context).updateContacts(bean);
    }

    //自定义接口
    public interface NetEvent {
        void onNetChange(int netMobile);
    }
}
