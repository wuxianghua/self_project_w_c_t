package net.imoran.auto.morwechat.bean;

/**
 * Created by xinhua.shi on 2018/9/9.
 */

public class AppInfo
{
    private String AppID;

    private int Type;

    public void setAppID(String AppID){
        this.AppID = AppID;
    }
    public String getAppID(){
        return this.AppID;
    }
    public void setType(int Type){
        this.Type = Type;
    }
    public int getType(){
        return this.Type;
    }
}