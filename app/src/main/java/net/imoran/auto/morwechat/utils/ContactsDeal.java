package net.imoran.auto.morwechat.utils;

import android.support.annotation.NonNull;

import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.fragment.InformationFragment;

import java.util.List;

/**
 * Created by xinhuashi on 2018/10/30.
 */

public class ContactsDeal {
    private static ContactsDeal mInstance;
    private List<WeMessageBean> mContacts;
    private boolean mFloatSwitch = true;
    private boolean mSoundSwitch;
    private boolean mIsHasUnreadMsg;

    private ContactsDeal() {}

    public static ContactsDeal getInstance() {
        if (mInstance == null) {
            synchronized (ContactsDeal.class) {
                if (mInstance == null) {
                    mInstance = new ContactsDeal();
                }
            }
        }
        return mInstance;
    }

    public void setContacts(List<WeMessageBean> messageBeans) {
        mContacts = messageBeans;
    }

    public WeMessageBean getContact(@NonNull String nickname) {
        if (mContacts != null && mContacts.size() != 0) {
            for (WeMessageBean mContact : mContacts) {
                if(nickname.equals(mContact.getNickName())||nickname.equals(mContact.getRemarkName())){
                    return mContact;
                }
            }
        }
        return null;
    }

    public void setSoundSwitch(boolean soundSwitch) {
        mSoundSwitch = soundSwitch;
    }

    public void setFloatSwitch(boolean floatSwitch) {
        mFloatSwitch = floatSwitch;
    }

    public boolean getFloatSwitch() {
        return mFloatSwitch;
    }

    public boolean getSoundSwitch() {
        return mSoundSwitch;
    }

    public boolean ismIsHasUnreadMsg() {
        return mIsHasUnreadMsg;
    }

    public void setmIsHasUnreadMsg(boolean mIsHasUnreadMsg) {
        this.mIsHasUnreadMsg = mIsHasUnreadMsg;
    }
}
