package net.imoran.auto.morwechat.service;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import net.imoran.auto.morwechat.bean.UpdateUnreadMessage;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.fragment.InformationFragment;
import net.imoran.auto.morwechat.utils.ContactsDeal;
import net.imoran.rripc.lib.RRService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinhuashi on 2018/10/24.
 */

public class WechatDataService extends RRService {
    private static final String TAG = "WechatDataService";
    //获取未读微信消息
    public static final int WHAT_GET_UNREAD_MESSAGE = 100;
    private Gson gson = new Gson();
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onClientMessage(Message message) {
        super.onClientMessage(message);
        int what = message.what;
        switch (what) {
            case WHAT_GET_UNREAD_MESSAGE:
                onGetUnreadMessage(message);
                break;
        }
    }

    private void onGetUnreadMessage(Message message) {
        Bundle data = message.getData();
        String wechatParam = data.getString("wechatParam");
        String s = null;
        try {
            JSONObject jsonObject = new JSONObject(wechatParam);
            JSONObject params = jsonObject.getJSONObject("params");
            boolean isHasNickname = params.isNull("nickname");
            UpdateUnreadMessage updateUnreadMessage = new UpdateUnreadMessage();
            UpdateUnreadMessage.WechatMessageUnreadBean unreadBean = new UpdateUnreadMessage.WechatMessageUnreadBean();
            List<UpdateUnreadMessage.WechatMessageUnreadBean.DefaultBean> unreadList = new ArrayList<>();
            UpdateUnreadMessage.WechatMessageUnreadBean.DefaultBean defaultBean = new UpdateUnreadMessage.WechatMessageUnreadBean.DefaultBean();
            if (isHasNickname) {
                int sumNumber = 0;
                String str;
                for (String s1 : InformationFragment.mUnreadMessage.keySet()) {
                    sumNumber += InformationFragment.mUnreadMessage.get(s1).size();
                }
                if(sumNumber == 0) {
                    str = "当前微信没有新消息";
                    ContactsDeal.getInstance().setmIsHasUnreadMsg(false);
                }else {
                    str = "好的";
                    ContactsDeal.getInstance().setmIsHasUnreadMsg(true);
                }
                defaultBean.setStr(str);
                unreadList.add(defaultBean);
                unreadBean.setDefaultX(unreadList);
                updateUnreadMessage.setWechat_message_unread(unreadBean);
            }else{
                String nickname = params.getString("nickname");
                WeMessageBean contact = ContactsDeal.getInstance().getContact(nickname);
                if (contact == null) return;
                List<String> strings = InformationFragment.mUnreadMessage.get(contact.getUserName());
                String str;
                if (strings != null && strings.size() != 0) {
                    str = "好的";
                    ContactsDeal.getInstance().setmIsHasUnreadMsg(true);
                }else {
                    String name = contact.getRemarkName() == null? contact.getNickName():contact.getRemarkName();
                    str = "没有" + name + "的新消息";
                    ContactsDeal.getInstance().setmIsHasUnreadMsg(false);
                }
                defaultBean.setStr(str);
                unreadList.add(defaultBean);
                unreadBean.setDefaultX(unreadList);
                updateUnreadMessage.setWechat_message_unread(unreadBean);
            }
            s = gson.toJson(updateUnreadMessage);
            Log.e(TAG,"SSS-----" + s);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        data.putString("response", s);
        message.setData(data);
        replyToClient(data, message);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
