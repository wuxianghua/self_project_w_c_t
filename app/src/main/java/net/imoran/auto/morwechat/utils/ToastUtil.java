package net.imoran.auto.morwechat.utils;

/**
 * Created by xinhua.shi on 2018/4/3.
 */

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    public ToastUtil() {
    }

    public static void shortShow(Context context, String content) {
        Toast toast = Toast.makeText(context, content, 0);
        toast.show();
    }

    public static void longShow(Context context, String content) {
        Toast toast = Toast.makeText(context, content, 1);
        toast.show();
    }
}
