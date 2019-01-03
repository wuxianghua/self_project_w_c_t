package net.imoran.auto.morwechat.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.bean.WechatBaseInfo;
import net.imoran.auto.morwechat.manager.WeChatClient;
import net.imoran.auto.morwechat.widget.WinDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xinhuashi on 2018/8/31.
 */

public class DataDealUtil {
    public static DataDealUtil instance;
    private WinDialog winDialog;
    public List<String> getFastInputAnswer() {
        return fastInputAnswer;
    }

    public void setFastInputAnswer(List<String> fastInputAnswer) {
        this.fastInputAnswer = fastInputAnswer;
    }

    private List<String> fastInputAnswer = new ArrayList<>();
    private DataDealUtil(Context mContext) {
        mMessageBeans = new CopyOnWriteArrayList<>();
        mTempMessageBeans = new CopyOnWriteArrayList<>();
        winDialog = new WinDialog(mContext);
    }
    public static DataDealUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (DataDealUtil.class) {
                if(instance == null) {
                    instance = new DataDealUtil(context);
                }
            }
        }
        return instance;
    }

    public WinDialog getWinDialog(){
        return winDialog;
    }

    private CopyOnWriteArrayList<WeMessageBean> mTempMessageBeans;
    private CopyOnWriteArrayList<WeMessageBean> mMessageBeans;

    public void initMessageBeans() {
        mTempMessageBeans.clear();
        mTempMessageBeans.addAll(mMessageBeans);
    }

    public boolean saveWhiteListFriend(Context context) {
        if(mTempMessageBeans.size() > 7) {
            Toast.makeText(context,"白名单中的人数已达到上限，不能再添加了",Toast.LENGTH_SHORT).show();
            return false;
        }
        mMessageBeans.clear();
        Log.e("haha","save" + mTempMessageBeans.size());
        mMessageBeans.addAll(mTempMessageBeans);
        mTempMessageBeans.clear();
        return true;
    }

    public CopyOnWriteArrayList<WeMessageBean> getMessageBeans() {
        return mMessageBeans;
    }

    public void canCelSaveWhiteListFriends() {
        mTempMessageBeans.clear();
    }

    public void addWhiteListFriends(WeMessageBean messageBean) {
        if(isAddWhiteListFriends(messageBean.getNickName())) return;
        Log.e("haha","add" + messageBean.getNickName());
        mTempMessageBeans.add(messageBean);
    }

    public boolean isAddWhiteListFriends(String nickName) {
        if (mMessageBeans == null || mMessageBeans.size() == 0) return false;
        for (WeMessageBean mMessageBean : mMessageBeans) {
            if (mMessageBean.getNickName().equals(nickName)) {
                Log.e("haha","is" + nickName);
                return true;
            }
        }
        return false;
    }

    public <T> void releaseResource(Context context) {
        WechatBaseInfo baseInfo = WeChatClient.getInstance(context).getBaseInfo();
        if (baseInfo != null) {
            SharedPreferenceUtils.setDataList(context,baseInfo.getUser().getNickName(),mMessageBeans);
        }
        mMessageBeans.clear();
    }

    public void initData(String nickName,Context context) {
        mMessageBeans = SharedPreferenceUtils.getDataList(nickName, context);
    }

    public void removeWhiteListFriends(String nickName) {
        if (mTempMessageBeans == null || mTempMessageBeans.size() == 0) return;
        for (WeMessageBean mMessageBean : mTempMessageBeans) {
            if (mMessageBean.getNickName().equals(nickName)) {
                Log.e("haha","rm" + nickName);
                mTempMessageBeans.remove(mMessageBean);
            }
        }
    }
}
