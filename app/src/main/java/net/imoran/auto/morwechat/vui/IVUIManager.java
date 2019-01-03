package net.imoran.auto.morwechat.vui;

import android.app.Activity;
import android.content.Intent;

import net.imoran.sdk.bean.base.BaseContentEntity;

/**
 * Created by xinhuashi on 2018/10/16.
 */

public interface IVUIManager {
    /**
     * 解析VUI数据
     * @param baseContentEntity 数据携带者
     * @param activity  activity
     */
    void parseVUIContent(BaseContentEntity baseContentEntity, Activity activity);

}
