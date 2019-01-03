package net.imoran.auto.morwechat;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.tencent.bugly.crashreport.CrashReport;

import net.imoran.auto.scenebase.lib.SceneAPI;
import net.imoran.rripc.lib.ConnectionStateListener;
import net.imoran.rripc.lib.RRClient;
import net.imoran.rripc.lib.RRClientFactory;
import net.imoran.rripc.lib.ResponseCallback;

import java.io.InputStream;

/**
 *
 * Created by xinhua.shi on 2018/4/18.
 */
public class WeChatApp extends Application {

    public static WeChatApp application;
    public static String userName;
    private static boolean isConnected = false;
    private Handler mHandler = new Handler();

    public static RRClient rrClient;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //CrashReport.initCrashReport(getApplicationContext(), BuildConfig.BuglyAppID, true);
                ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(application);
                ImageLoader.getInstance().init(configuration);
                // 再程序开始的时候执行初始化
                InputStream inputStream = getResources().openRawResource(R.raw.nli_scenes);

                // inputStream 指定的是 app里面包含的一个 nlicsv 文件，将会被合并到MorService的里面，形成一个总和
                // 不需要的话可以设为null
                SceneAPI.initAll(application,null);

            }
        },3000);
    }

    public void initVui(final Context context) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 初始化一个RRClient对象
                Bundle extra = new Bundle();
                extra.putString("name", "mor-wechat");
                rrClient = RRClientFactory.createRRClient(context, "net.net.morservice", extra);
                connectService();
            }
        },3000);
    }

    private void connectService() {
        rrClient.connect(new ConnectionStateListener() {
            @Override
            public void onConnected(String s) {
                isConnected = true;
            }

            @Override
            public void onDisconnect(String s) {
                isConnected = false;
            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        //这个方法不可靠
        //disconnectService();
    }

    private void disconnectService() {
        rrClient.disconnect();
    }

    /**
     * 设置当前页面为新页面
     */
    public static void updateQueryId(String pageId, String queryId) {
        if (WeChatApp.rrClient == null)
            return;

        Bundle data = new Bundle();
        data.putString("updateAction", "updateQueryId");
        data.putString("pageid", pageId);
        data.putString("queryid", queryId);
        //App.serviceHelper.sendMessage(1, data);
        WeChatApp.rrClient.send(10102, data, new ResponseCallback(){
            public void onResponse(String response){
                // 接收到请求的数据
            }

            public void onResponseError(String msg, int code){
                // 出现错误
            }
        });
    }
}
