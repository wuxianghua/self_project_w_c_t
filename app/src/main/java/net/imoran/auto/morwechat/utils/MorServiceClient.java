package net.imoran.auto.morwechat.utils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import net.imoran.rripc.lib.ConnectionStateListener;
import net.imoran.rripc.lib.RRClient;
import net.imoran.rripc.lib.RRClientFactory;

/**
 * Created by xinhuashi on 2018/8/22.
 */

public class MorServiceClient {

    private RRClient rrClient;
    private Handler mHandler = new Handler();
    private static MorServiceClient instance = null;

    private MorServiceClient(Context context) {
        initVUiSync(context.getApplicationContext());
    }

    public static MorServiceClient getInstance(Context context) {
        if (instance == null) {
            synchronized (MorServiceClient.class) {
                if (instance == null) {
                    instance = new MorServiceClient(context);
                }
            }
        }
        return instance;
    }

    /**
     * @param mContext 同步客户端的处理
     */
    public void initVUiSync(final Context mContext) {
        Bundle extra = new Bundle();
        extra.putString("name", "mor_music");
        rrClient = RRClientFactory.createRRClient(mContext, "net.imoran.morservice", extra);
        rrClient.connect(new ConnectionStateListener() {
            @Override
            public void onConnected(String s) {

            }

            @Override
            public void onDisconnect(String s) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initVUiSync(mContext);
                    }
                }, 3000);
            }
        });
    }

    public RRClient getRrClient() {
        return rrClient;
    }
}
