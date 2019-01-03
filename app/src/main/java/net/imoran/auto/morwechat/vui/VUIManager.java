package net.imoran.auto.morwechat.vui;

import android.app.Activity;
import android.content.Intent;

import net.imoran.auto.morwechat.UserInfoActivity;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.sdk.bean.base.BaseContentEntity;
import net.imoran.sdk.bean.bean.BaseReply;
import net.imoran.sdk.bean.bean.WechatContactBean;
import net.imoran.sdk.bean.bean.WechatMessageBean;
import net.imoran.sdk.util.ReplyUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xinhuashi on 2018/10/16.
 */

public class VUIManager implements IVUIManager {

    private Activity activity;
    private List<IVUICallBack> mVUICallbacks;
    private BaseContentEntity baseContentEntity;

    private VUIManager() {
        mVUICallbacks = new ArrayList<>();
    }


    private static VUIManager instance;

    public static VUIManager getInstance() {
        if (instance == null) {
            synchronized (VUIManager.class) {
                if (instance == null) {
                    instance = new VUIManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void parseVUIContent(BaseContentEntity baseContentEntity, Activity activity) {
        if (activity != null) {
            this.activity = activity;
        }
        this.baseContentEntity = baseContentEntity;
        distributePage();
    }

    public void setVuiCallback(IVUICallBack vuiCallback) {
        if (!mVUICallbacks.contains(vuiCallback)) {
            mVUICallbacks.add(vuiCallback);
        }
    }

    public void removeVuiCallback(IVUICallBack vuiCallBack) {
        if (mVUICallbacks.contains(vuiCallBack)) {
            mVUICallbacks.remove(vuiCallBack);
        }
    }

    private void distributePage() {
        if (baseContentEntity == null) {
            return;
        }
        String domain = baseContentEntity.getBaseSceneEntity().domain;
        String intention = baseContentEntity.getBaseSceneEntity().intention;
        String type = baseContentEntity.getBaseSceneEntity().type;
        String action = baseContentEntity.getBaseSceneEntity().action;

        if ("".equals(domain) || domain == null) {
            return;
        }

        if ("".equals(intention) || intention == null) {
            return;
        }

        if ("".equals(action) || type == null) {
            return;
        }

        if ("".equals(action) || action == null) {
            return;
        }

        String queryId = baseContentEntity.getBaseSceneEntity().getQueryid();
        if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention)&&
                VUIConstant.Type.OPEN.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.vuiOpenApp();
            }
        }if (VUIConstant.Domain.WECHAT.equals(domain) &&
                VUIConstant.Intention.UNREAD.equals(intention)&&
                VUIConstant.Type.WECHAT_UNREAD.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.lookUnreadMsg();
            }
        }else if (VUIConstant.Domain.WECHAT.equals(domain) &&
                VUIConstant.Intention.VIEWING.equals(intention) &&
                VUIConstant.Type.CONTACT.equals(type)) {
            BaseReply baseReply = baseContentEntity.getBaseReply();
            if (baseReply != null && baseReply instanceof WechatContactBean) {
                WechatContactBean contactBean = (WechatContactBean) baseReply;
                List<WechatContactBean.WechatContactEntity> wechat_contact = contactBean.getWechat_contact();
                if (wechat_contact != null && wechat_contact.size() != 0) {
                    Intent intent = new Intent(activity,UserInfoActivity.class);
                    intent.putExtra("queryid",queryId);
                    intent.putExtra("type","wechat_contact");
                    intent.putExtra("nickname",wechat_contact.get(0).getNickname());
                    for (IVUICallBack mVUICallback : mVUICallbacks) {
                        mVUICallback.openChatWin(intent);
                    }
                }
            }
        }else if (VUIConstant.Domain.WECHAT.equals(domain) &&
                VUIConstant.Intention.SENDING.equals(intention) &&
                VUIConstant.Type.MESSAGE.equals(type)) {
            BaseReply baseReply = baseContentEntity.getBaseReply();
            if (baseReply != null && baseReply instanceof WechatMessageBean) {
                WechatMessageBean wechatMessageBean = (WechatMessageBean) baseReply;
                ArrayList<WechatMessageBean.WechatMessageEntity> wechat_message = wechatMessageBean.getWechat_message();
                if (wechat_message != null && wechat_message.size() != 0) {
                    if (VUIConstant.Action.INQUIRE.equals(action)) {
                        Intent intent = new Intent(activity,UserInfoActivity.class);
                        intent.putExtra("queryid",queryId);
                        intent.putExtra("type","wechat_message");
                        intent.putExtra("nickname",wechat_message.get(0).getNickname());
                        for (IVUICallBack mVUICallback : mVUICallbacks) {
                            mVUICallback.sendMessageToFri(intent);
                        }
                    }else if (VUIConstant.Action.CONFIRM.equals(action)) {
                        for (IVUICallBack mVUICallback : mVUICallbacks) {
                            mVUICallback.sendFriContent(queryId,wechat_message.get(0).getMessage_content());
                            mVUICallback.finishEmptyActivity();
                        }
                    }else if (VUIConstant.Action.EXECUTE.equals(action)) {
                        for (IVUICallBack mVUICallback : mVUICallbacks) {
                            mVUICallback.sendExecuteContent(queryId);
                            mVUICallback.finishEmptyActivity();
                        }
                    }
                }else if (VUIConstant.Action.INQUIRE.equals(action)){
                    for (IVUICallBack mVUICallback : mVUICallbacks) {
                        mVUICallback.inquerySendPerson(queryId);
                        mVUICallback.finishEmptyActivity();
                    }
                }
            }
        }else if (VUIConstant.Domain.WECHAT.equals(domain) &&
                VUIConstant.Intention.SENDING.equals(intention) &&
                VUIConstant.Type.CONTACT.equals(type)) {
            BaseReply baseReply = baseContentEntity.getBaseReply();
            if (baseReply != null && baseReply instanceof WechatContactBean) {
                WechatContactBean wechatContactBean = (WechatContactBean) baseReply;
                ArrayList<WechatContactBean.WechatContactEntity> wechat_contact = (ArrayList<WechatContactBean.WechatContactEntity>) wechatContactBean.getWechat_contact();
                if (wechat_contact != null && wechat_contact.size() != 0) {
                    if (VUIConstant.Action.SELECT.equals(action)) {
                        for (IVUICallBack mVUICallback : mVUICallbacks) {
                            Intent intent = new Intent(activity,UserInfoActivity.class);
                            intent.putExtra("queryid",queryId);
                            intent.putExtra("type","wechat_message_select");
                            intent.putExtra("selectFriends",wechat_contact);
                            mVUICallback.showAllFriends(intent);
                        }
                    }
                }
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention) &&
                VUIConstant.Type.CLOSEREPORT.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.stopWechatPlay();
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention) &&
                VUIConstant.Type.OPENREPORT.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.openWechatPlay();
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention) &&
                VUIConstant.Type.CLOSE.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.vuiCloseApp();
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention) &&
                (VUIConstant.Type.LOGOUT.equals(type) || VUIConstant.Type.WECHAT_LOGOUT.equals(type))) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.vuiLogout();
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention) &&
                VUIConstant.Type.CLOSENOTIFICATION.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.closeNotification();
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention) &&
                VUIConstant.Type.OPENNOTIFICATION.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                mVUICallback.openNotification();
            }
        }else if (VUIConstant.Domain.CMD.equals(domain) &&
                VUIConstant.Intention.INSTRUCTING.equals(intention)&&
                VUIConstant.Type.OPENCONTACTS.equals(type)) {
            for (IVUICallBack mVUICallback : mVUICallbacks) {
                Intent intent = new Intent(activity,UserInfoActivity.class);
                intent.putExtra("type",VUIConstant.Type.OPENCONTACTS);
                mVUICallback.openContacts(intent);
            }
        }
    }
}
