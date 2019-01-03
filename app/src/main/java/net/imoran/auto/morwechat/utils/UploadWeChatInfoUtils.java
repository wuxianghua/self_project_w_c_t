package net.imoran.auto.morwechat.utils;

import android.content.Context;
import android.content.Intent;
import com.google.gson.Gson;

/**
 * Created by xinhuashi on 2018/8/21.
 */

public class UploadWeChatInfoUtils {
    private static Gson gson = new Gson();

    public static void sendWeChatStatus(Context context,String key, String value,String key1,String value1) {
        Intent intent = new Intent("net.imoran.auto.systemrequest");
        intent.putExtra(key,value);
        intent.putExtra(key1,value1);
        context.sendBroadcast(intent);
    }

    public static void sendWeChatMsg(Context context,String key, String value) {
        Intent intent = new Intent("net.imoran.auto.clientaction.tts");
        intent.putExtra(key,value);
        context.sendBroadcast(intent);
    }
}
