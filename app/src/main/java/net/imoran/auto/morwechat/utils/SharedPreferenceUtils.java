package net.imoran.auto.morwechat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.imoran.auto.morwechat.bean.WeMessageBean;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xinhuashi on 2018/9/10.
 */

public class SharedPreferenceUtils {
    //存储sharedpreferences文件名
    private static final String FILE_NAME = "moran_auto_wechat";

    public static void clearDataList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * 获取List
     * @param tag
     * @return
     */
    public static CopyOnWriteArrayList<WeMessageBean> getDataList(String tag, Context context) {
        CopyOnWriteArrayList<WeMessageBean> datalist=new CopyOnWriteArrayList();
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String strJson = sharedPreferences.getString(tag, null);
        if (null == strJson) {
            return datalist;
        }
        Gson gson = new Gson();
        datalist = gson.fromJson(strJson, new TypeToken<CopyOnWriteArrayList<WeMessageBean>>() {
        }.getType());
        return datalist;
    }

    /**
     * 保存List
     * @param tag
     * @param datalist
     */
    public static void setDataList(Context context,String tag, CopyOnWriteArrayList<WeMessageBean> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sharedPreferences = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.putString(tag, strJson);
        editor.commit();
    }
}
