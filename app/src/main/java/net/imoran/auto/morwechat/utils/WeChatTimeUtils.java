package net.imoran.auto.morwechat.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * Created by xinhua.shi on 2018/4/19.
 */
public class WeChatTimeUtils {

    public static String parseWeChatTime(String time) {
        long weChatTime = Long.parseLong(time) * 1000;
        long currTime = System.currentTimeMillis();
        long timeLong = currTime - weChatTime;
        if(timeLong<0){
            timeLong=0;
        }
        if (timeLong<60*1000)
            return (int)(timeLong/1000.0) + "秒前";
        else if (timeLong<60*60*1000){
            timeLong = timeLong/1000 /60;
            return timeLong + "分钟前";
        }
        else if (timeLong<60*60*24*1000){
            timeLong = timeLong/60/60/1000;
            return timeLong+"小时前";
        }
        else if ((timeLong/1000/60/60/24)<7){
            timeLong = timeLong/1000/ 60 / 60 / 24;
            return timeLong + "天前";
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(new Date(timeLong));
        }
    }

    public static String parseWeChatTime(long time) {
//        long weChatTime = Long.parseLong(time);
        long currTime = System.currentTimeMillis();
        long timeLong = currTime - (time*1000);
        if(timeLong<0){
            timeLong=0;
        }
        if (timeLong<60*1000)
            return (int)(timeLong/1000.0) + "秒前";
        else if (timeLong<60*60*1000){
            timeLong = timeLong/1000 /60;
            return timeLong + "分钟前";
        }
        else if (timeLong<60*60*24*1000){
            timeLong = timeLong/60/60/1000;
            return timeLong+"小时前";
        }
        else if ((timeLong/1000/60/60/24)<7){
            timeLong = timeLong/1000/ 60 / 60 / 24;
            return timeLong + "天前";
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(new Date(timeLong));
        }
    }

    public static String parseWeChatHistoryTime(long time) {
        long currTime = System.currentTimeMillis();
        long timeLong = currTime - time;
        if(timeLong<0){
            timeLong=0;
        }
        if (timeLong<60*1000)
            return (int)(timeLong/1000.0) + "秒前";
        else if (timeLong<60*60*1000){
            timeLong = timeLong/1000 /60;
            return timeLong + "分钟前";
        }
        else if (timeLong<60*60*24*1000){
            timeLong = timeLong/60/60/1000;
            return timeLong+"小时前";
        }
        else if ((timeLong/1000/60/60/24)<7){
            timeLong = timeLong/1000/ 60 / 60 / 24;
            return timeLong + "天前";
        }else{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(new Date(timeLong));
        }
    }
}
