package net.imoran.auto.morwechat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.utils.ContactsDeal;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.vui.IVUICallBack;
import net.imoran.auto.morwechat.vui.VUIConstant;
import net.imoran.auto.morwechat.vui.VUIManager;
import net.imoran.auto.morwechat.widget.WinDialog;
import net.imoran.auto.scenebase.lib.BaseRouteActivity;
import net.imoran.sdk.bean.base.BaseContentEntity;
import net.imoran.sdk.bean.bean.BaseReply;
import net.imoran.sdk.bean.bean.WechatContactBean;
import net.imoran.sdk.bean.bean.WechatMessageBean;
import net.imoran.sdk.entity.info.ContactData;
import net.imoran.sdk.util.ReplyUtils;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xinhuashi on 2018/10/17.
 */

public class EmptyActivity extends BaseRouteActivity implements IVUICallBack{

    private static final String TAG = "EmptyActivity";
    private BaseContentEntity baseContentEntity;
    private WinDialog mWindialog;

    @Override
    public void onCreate(@Nullable  Bundle bundle) {
        VUIManager.getInstance().setVuiCallback(this);
        Log.e("TAG111","ONcREATE");
        setContentView(R.layout.activity_empty);
        super.onCreate(bundle);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected boolean onNliDispatch(BaseContentEntity baseContentEntity, String s) {
        this.baseContentEntity = baseContentEntity;
        VUIManager.getInstance().parseVUIContent(baseContentEntity,this);
        return false;
    }

    private void getContentData(Intent intent) {
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(VUIConstant.vuiConstantKey)) {
            String baseContentEntryStr = intent.getExtras().getString(VUIConstant.vuiConstantKey);
            Log.e("vuiControl","baseContentEntryStr111" + baseContentEntryStr);
            baseContentEntity = ReplyUtils.createResponseFromJson(baseContentEntryStr);
        }else {
            Intent mIntent = new Intent(this,UserInfoActivity.class);
            startActivity(mIntent);
            finish();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.e(TAG,"onStart");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"onResume");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG,"onStart");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VUIManager.getInstance().removeVuiCallback(this);
        Log.e(TAG,"onDestroy");
    }

    private void distributePage() {
        if (baseContentEntity == null) {
            return;
        }
        String domain = baseContentEntity.getBaseSceneEntity().domain;
        String intention = baseContentEntity.getBaseSceneEntity().intention;
        String action = baseContentEntity.getBaseSceneEntity().type;
        if ("".equals(domain) || domain == null) {
            return;
        }
        if ("".equals(intention) || intention == null) {
            return;
        }
        if ("".equals(action) || action == null) {
            return;
        }
        String queryId = baseContentEntity.getBaseSceneEntity().getQueryid();
    }

    @Override
    public void vuiOpenApp() {
        mWindialog = DataDealUtil.getInstance(this).getWinDialog();
        List<WeMessageBean> messageBeans = mWindialog.getMessageBeans();
        if (messageBeans != null && messageBeans.size() != 0) {
            mWindialog.showWindowMainView();
            finish();
        }else {
            Intent intent = new Intent(this,UserInfoActivity.class);
            intent.putExtra("type",VUIConstant.Type.OPEN);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void openChatWin(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void sendMessageToFri(Intent intent) {
        Log.e("TAG","hehehe");
        startActivity(intent);
        finish();
    }

    @Override
    public void showAllFriends(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void sendFriContent(String query,String content) {

    }

    @Override
    public void sendExecuteContent(String queryId) {

    }

    @Override
    public void inquerySendPerson(String queryId) {
        Intent intent = new Intent(this,UserInfoActivity.class);
        intent.putExtra("type","openinformation");
        startActivity(intent);
    }

    @Override
    public void stopWechatPlay() {

    }

    @Override
    public void openWechatPlay() {

    }

    @Override
    public void vuiCloseApp() {

    }

    @Override
    public void vuiLogout() {

    }

    @Override
    public void closeNotification() {

    }

    @Override
    public void openNotification() {

    }

    @Override
    public void openContacts(Intent intent) {
        startActivity(intent);
        finish();
    }

    @Override
    public void finishEmptyActivity() {
        finish();
    }

    @Override
    public void lookUnreadMsg() {
        if (!ContactsDeal.getInstance().ismIsHasUnreadMsg()) {
            finish();
            return;
        }
        mWindialog = DataDealUtil.getInstance(this).getWinDialog();
        List<WeMessageBean> messageBeans = mWindialog.getMessageBeans();
        if (messageBeans != null && messageBeans.size() != 0) {
            mWindialog.showWinMainView();
            finish();
        }else {
            Intent intent = new Intent(this,UserInfoActivity.class);
            intent.putExtra("type",VUIConstant.Type.OPEN);
            startActivity(intent);
            finish();
        }
    }
}
