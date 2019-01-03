package net.imoran.auto.morwechat.receiver;

import android.support.annotation.Nullable;

import net.imoran.auto.morwechat.vui.VUIManager;
import net.imoran.auto.scenebase.lib.SceneBroadcastReceiver;
import net.imoran.sdk.bean.base.BaseContentEntity;

public class WechatVuiDealReceiver extends SceneBroadcastReceiver {
    @Nullable
    @Override
    public String onNliDispatch(BaseContentEntity baseContentEntity, String s) {
        VUIManager.getInstance().parseVUIContent(baseContentEntity,null);
        return null;
    }
}
