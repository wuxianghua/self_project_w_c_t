package net.imoran.auto.morwechat.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import net.imoran.auto.morwechat.WeChatApp;
import net.imoran.rripc.lib.ConnectionStateListener;
import net.imoran.rripc.lib.RRClient;
import net.imoran.rripc.lib.RRClientFactory;
import net.imoran.rripc.lib.ResponseCallback;

/**
 * Created by xinhua.shi on 2018/6/15.
 */

public class VuiControlMananger implements ConnectionStateListener {

    private static VuiControlMananger instance;
    private RRClient rrClient;

    public VuiControlMananger(Context context) {
        Bundle extra = new Bundle();
        extra.putString("name", "mor-wechat");
        rrClient = RRClientFactory.createRRClient(context, "net.imoran.morservice", extra);
        rrClient.connect(this);
    }

    public static VuiControlMananger getInstance() {
        if (instance == null) {
            synchronized (VuiControlMananger.class) {
                if (instance == null) {
                    instance = new VuiControlMananger(WeChatApp.application);
                }
            }
        }
        return instance;
    }

    public void sendMessage(Bundle bundle) {
        rrClient.send(10102, bundle, new ResponseCallback() {
            @Override
            public void onResponse(String s) {
                Log.e("vuihahah","onResponse" + s);
            }

            @Override
            public void onResponseError(String s, int i) {
                Log.e("vuihahah","onResponseError" + s);
            }
        });
    }

    public void updatePage(Bundle data) {
        rrClient.send(10102, data, new ResponseCallback() {
            @Override
            public void onResponse(String s) {

            }

            @Override
            public void onResponseError(String s, int i) {

            }
        });
    }

    @Override
    public void onConnected(String s) {

    }

    @Override
    public void onDisconnect(String s) {

    }
}
