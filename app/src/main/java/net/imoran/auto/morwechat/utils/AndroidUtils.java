package net.imoran.auto.morwechat.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.imoran.auto.morwechat.constant.Constant;

import java.util.List;

/**
 * Created by xinhua.shi on 2018/12/23.
 */

public class AndroidUtils {

    public static void sendWeChatBroadCast(Context context, String request) {
        Intent loginIntent = new Intent();
        loginIntent.setAction("net.net.auto.systemrequest");
//        String sendContent = "{\n" +
//                "        \"wechat_status_change\":{\n" +
//                "            \"wechat_status\":\"login\"\n" +
//                "        }\n" +
//                "    }";
        loginIntent.putExtra("request", request);
        //发送广播
        context.sendBroadcast(loginIntent);
    }

    public static void hiddenSoftInput(Context context, View view) {
        InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(view.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void showSoftInput(Context context,View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void fromBackWindToFront(Context context) {
        //获取ActivityManager
        ActivityManager mAm = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        //获得当前运行的task
        List<ActivityManager.RunningTaskInfo> taskList = mAm.getRunningTasks(100);
        for(ActivityManager.RunningTaskInfo rti : taskList) {
        //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
        if(rti.topActivity.getPackageName().equals(context.getPackageName())) {
            Intent LaunchIntent =new Intent(Intent.ACTION_MAIN);
            ComponentName cn =new ComponentName(Constant.PACKAGE,rti.topActivity.getClassName());
            LaunchIntent.setComponent(cn);
            context.startActivity(LaunchIntent);
            break;
        }

        }
    }
}
