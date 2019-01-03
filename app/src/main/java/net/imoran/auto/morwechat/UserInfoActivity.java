package net.imoran.auto.morwechat;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.imoran.auto.morwechat.base.BaseActivity;
import net.imoran.auto.morwechat.base.BasePresenter;
import net.imoran.auto.morwechat.bean.Contact;
import net.imoran.auto.morwechat.bean.Quest;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.bean.WechatStatusBean;
import net.imoran.auto.morwechat.fragment.InformationFragment;
import net.imoran.auto.morwechat.fragment.PhoneBookFragment;
import net.imoran.auto.morwechat.fragment.SetFragment;
import net.imoran.auto.morwechat.manager.WeChatClient;
import net.imoran.auto.morwechat.receiver.NetBroadcastReceiver;
import net.imoran.auto.morwechat.utils.ContactsDeal;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.utils.NetUtil;
import net.imoran.auto.morwechat.utils.UploadWeChatInfoUtils;
import net.imoran.auto.morwechat.vui.IVUICallBack;
import net.imoran.auto.morwechat.vui.VUIConstant;
import net.imoran.auto.morwechat.vui.VUIManager;
import net.imoran.auto.morwechat.widget.TabItemView;
import net.imoran.auto.morwechat.widget.WinDialog;
import net.imoran.sdk.bean.bean.WechatContactBean;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by xinhuashi on 2018/7/26.
 */

public class UserInfoActivity extends BaseActivity implements NetBroadcastReceiver.NetEvent,IVUICallBack{
    private static final String TAG = "EmptyActivity";
    private TabItemView phoneBookItem;
    private TabItemView informationItem;
    private TabItemView setItem;
    private List<Fragment> fragments;
    private FragmentManager supportFragmentManager;
    private ImageView mTwoDimensionCode;
    private LinearLayout userInfo;
    private RelativeLayout mainInfo;
    private NetBroadcastReceiver mReceiver;
    private ImageView netWorkState;
    private TextView netWorkNotify;
    private TextView netWorkNotifySet;
    private TextView tvShowControlTip;
    private boolean mIsBackGround;
    private ProgressBar mLoginProgress;
    private TextView mTvLoginTips;

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void onViewCreate() {
        initView();
        initListener();
        initFragment();
        initNetWorkState();
        //WeChatApp.application.initVui(this);
        Log.e(TAG,"UserInfoActivity.onCreate");
    }

    private void initNetWorkState() {
        int netWorkState = NetUtil.getNetWorkState(this);
        if (netWorkState == 0 || netWorkState == 1) {
            connectNetWork();
        }else if (netWorkState == -1) {
            disConnectNetWork();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
        VUIManager.getInstance().removeVuiCallback(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        dispatchIntentData(intent);
    }

    private void dispatchIntentData(Intent intent) {
        String type = intent.getStringExtra("type");
        if (VUIConstant.Type.CONTACT.equals(type)) {
            String nickname = intent.getStringExtra("nickname");
            String queryid = intent.getStringExtra("queryid");
            WeMessageBean contact = ContactsDeal.getInstance().getContact(nickname);
            changeToChatView(contact);
        }else if (VUIConstant.Type.MESSAGE.equals(type)) {
            String nickname = intent.getStringExtra("nickname");
            String queryid = intent.getStringExtra("queryid");
            Log.e("vuiControl","queryid" + queryid);
            WeMessageBean contact = ContactsDeal.getInstance().getContact(nickname);
            changeToChatView(contact,queryid);
        }else if (VUIConstant.Type.OPENCONTACTS.equals(type)) {
            changeToPhoneBook();
        }else if (VUIConstant.vuiSelectContacts.equals(type)) {
            String queryid = intent.getStringExtra("queryid");
            ArrayList<WechatContactBean.WechatContactEntity> selectFriends = (ArrayList<WechatContactBean.WechatContactEntity>) intent.getSerializableExtra("selectFriends");
            showSelectFriends(queryid,selectFriends);
        }else if (VUIConstant.Type.OPEN.equals(type)) {
            changeToInformation();
        }else if ("openinformation".equals(type)) {
            changeToInformation();
        }
    }

    private void showSelectFriends(String queryId, ArrayList<WechatContactBean.WechatContactEntity> selectFriends) {
        setItem.setSelect(false);
        informationItem.setSelect(false);
        phoneBookItem.setSelect(true);
        supportFragmentManager.beginTransaction()
                .show(fragments.get(0))
                .hide(fragments.get(2))
                .hide(fragments.get(1))
                .commitAllowingStateLoss();
        ((PhoneBookFragment)fragments.get(0)).showSelectFriends(selectFriends,queryId);
    }

    private void initFragment() {
        supportFragmentManager = getSupportFragmentManager();
        fragments = supportFragmentManager.getFragments();
        if (fragments == null || fragments.size() == 0) {
            fragments = new ArrayList<>();
            fragments.add(new PhoneBookFragment());
            fragments.add(new InformationFragment());
            fragments.add(new SetFragment());
        }
        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.add(R.id.fl_container,fragment);
        }
        transaction.show(fragments.get(1)).hide(fragments.get(0)).hide(fragments.get(2)).commit();
    }

    private void initListener() {
        phoneBookItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToPhoneBook();
            }
        });
        setItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToSet();
            }
        });
        informationItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToInformation();
            }
        });
        VUIManager.getInstance().setVuiCallback(this);
        mReceiver.setNetEvent(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mIsBackGround = true;
        if (fragments != null && fragments.size() >=2) {
            ((InformationFragment)fragments.get(1)).pauseFragment();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mIsBackGround = false;
        if (fragments != null && fragments.size() >=2) {
            ((InformationFragment)fragments.get(1)).resumeFragment();
        }
    }

    public void disConnectNetWork() {
        if(netWorkState != null) {
            netWorkState.setVisibility(View.VISIBLE);
        }
        if(netWorkNotify != null) {
            netWorkNotify.setVisibility(View.VISIBLE);
        }
        if(netWorkNotifySet != null) {
            netWorkNotifySet.setVisibility(View.VISIBLE);
        }
        if (tvShowControlTip != null) {
            tvShowControlTip.setVisibility(View.GONE);
        }
        if (mTwoDimensionCode != null) {
            mTwoDimensionCode.setVisibility(View.GONE);
        }
    }

    public void connectNetWork() {
        if(netWorkState != null) {
            netWorkState.setVisibility(View.GONE);
        }
        if(netWorkNotify != null) {
            netWorkNotify.setVisibility(View.GONE);
        }
        if(netWorkNotifySet != null) {
            netWorkNotifySet.setVisibility(View.GONE);
        }
        if (tvShowControlTip != null) {
            tvShowControlTip.setVisibility(View.VISIBLE);
        }
        if (mTwoDimensionCode != null) {
            mTwoDimensionCode.setVisibility(View.VISIBLE);
        }
    }

    private void changeToPhoneBook() {
        setItem.setSelect(false);
        informationItem.setSelect(false);
        phoneBookItem.setSelect(true);
        supportFragmentManager.beginTransaction()
                .show(fragments.get(0))
                .hide(fragments.get(2))
                .hide(fragments.get(1))
                .commitAllowingStateLoss();
    }

    private void changeToSet() {
        setItem.setSelect(true);
        informationItem.setSelect(false);
        phoneBookItem.setSelect(false);
        supportFragmentManager.beginTransaction()
                .show(fragments.get(2))
                .hide(fragments.get(0))
                .hide(fragments.get(1))
                .commitAllowingStateLoss();
    }

    private void changeToInformation() {
        if (setItem != null) {
            setItem.setSelect(false);
        }
        if (informationItem != null) {
            informationItem.setSelect(true);
        }
        if (phoneBookItem != null) {
            phoneBookItem.setSelect(false);
        }
        if (supportFragmentManager != null && fragments != null && fragments.size() >= 3) {
            supportFragmentManager.beginTransaction()
                    .show(fragments.get(1))
                    .hide(fragments.get(2))
                    .hide(fragments.get(0))
                    .commitNowAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        if(((InformationFragment)fragments.get(1)).onBackPressed()) {

        }else {
            Intent intentHome = new Intent();
            intentHome.setAction(Intent.ACTION_MAIN);
            intentHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(intentHome);
        }
    }

    public void changeToChatView(WeMessageBean messageBean) {
        setItem.setSelect(false);
        informationItem.setSelect(true);
        phoneBookItem.setSelect(false);
        supportFragmentManager.beginTransaction()
                .show(fragments.get(1))
                .hide(fragments.get(2))
                .hide(fragments.get(0))
                .commit();
        ((InformationFragment)fragments.get(1)).showChatView(messageBean);
    }

    public void changeToChatView(WeMessageBean messageBean,String queryId) {
        setItem.setSelect(false);
        informationItem.setSelect(true);
        phoneBookItem.setSelect(false);
        supportFragmentManager.beginTransaction()
                .show(fragments.get(1))
                .hide(fragments.get(2))
                .hide(fragments.get(0))
                .commit();
        ((InformationFragment)fragments.get(1)).showChatView(messageBean,queryId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v!=null&&(!(v instanceof EditText)||!(v instanceof AppCompatEditText))){
                InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }

    private void initView() {
        phoneBookItem = findViewById(R.id.tiv_phone_book);
        setItem = findViewById(R.id.tiv_phone_set);
        informationItem = findViewById(R.id.tiv_phone_info);
        mTwoDimensionCode = findViewById(R.id.two_dimension_code);
        userInfo = findViewById(R.id.userinfo_activity);
        mainInfo = findViewById(R.id.main_activity);
        netWorkState = findViewById(R.id.net_work_status);
        netWorkNotify = findViewById(R.id.net_work_notify);
        netWorkNotifySet = findViewById(R.id.net_work_notify_set);
        tvShowControlTip = findViewById(R.id.tv_show_control_tip);
        mLoginProgress = findViewById(R.id.login_progress);
        mTvLoginTips = findViewById(R.id.tv_login_tip);
        mLoginProgress.setVisibility(View.GONE);
        mReceiver = new NetBroadcastReceiver();
        registerReceiver(mReceiver,mReceiver.getIntentFilter());
        WeChatClient.getInstance(this).setWinxinLogStatusListener(new WeChatClient.WinXinLogStatusListener() {
            @Override
            public void updateQurtImage(Bitmap bitmap) {
                mTwoDimensionCode.setImageBitmap(bitmap);
            }

            @Override
            public void updateLogStatus() {
                mTwoDimensionCode.setVisibility(View.GONE);
                mLoginProgress.setVisibility(View.VISIBLE);
                tvShowControlTip.setVisibility(View.GONE);
                mTvLoginTips.setVisibility(View.VISIBLE);
            }

            @Override
            public void loginSuccess(String skey, String pass_ticket, String baseUrl,String contactList) {
                userInfo.setVisibility(View.VISIBLE);
                mainInfo.setVisibility(View.GONE);
                changeToInformation();
                ((InformationFragment)fragments.get(1)).loginSuccess();
            }

            @Override
            public void logOut() {
                mTwoDimensionCode.setVisibility(View.VISIBLE);
                mLoginProgress.setVisibility(View.GONE);
                userInfo.setVisibility(View.GONE);
                tvShowControlTip.setVisibility(View.VISIBLE);
                mTvLoginTips.setVisibility(View.GONE);
                mainInfo.setVisibility(View.VISIBLE);
                releaseResource();
                sengWeChatLoginStatus("logoff",null,null,null, "wechat_logoff",null);
            }

            @Override
            public void getConcats(String skey, String pass_ticket, String baseUrl, String contactList) {
                sengWeChatLoginStatus("login",skey,pass_ticket,baseUrl, "wechat_login",contactList);
            }
        });
        WeChatClient.getInstance(this).getQurtImage();
    }

    private void updateLoginStatus(String logoff,String service) {
        Quest quest = new Quest();
        quest.wechat_status = logoff;
        WechatStatusBean bean = new WechatStatusBean();
        bean.wechat_status_change = quest;
        String s = gson.toJson(bean);
        UploadWeChatInfoUtils.sendWeChatStatus(UserInfoActivity.this,"request",s,"service",service);
    }

    public void sengWeChatLoginStatus(String loginStatus,String skey,String pass_ticket,String baseUrl,String service,String contactList) {
        Quest quest = new Quest();
        quest.wechat_status = loginStatus;
        quest.baseUrl = baseUrl;
        quest.pass_ticket = pass_ticket;
        quest.skey = skey;
        quest.contactList = contactList;
        WechatStatusBean bean = new WechatStatusBean();
        bean.wechat_status_change = quest;
        WeChatClient.getInstance(this).updateContacts(bean);
    }

    private List<String> dealWithCookie(List<String> cookie) {
        List<String> mCookies = new ArrayList<>();
        for (String s : cookie) {
            mCookies.add(s.split(";")[0]);
        }
        return mCookies;
    }

    private void releaseResource() {
        ((InformationFragment)fragments.get(1)).releaseResource();
        ((PhoneBookFragment)fragments.get(0)).releaseResource();
        ((SetFragment)fragments.get(2)).releaseResource();
        changeToInformation();
        DataDealUtil.getInstance(this).releaseResource(this);
    }

    public void changeFastAnswer(String answerOne,String answerTwo,String answerThree) {
        ((InformationFragment)fragments.get(1)).changeFastAnswer(answerOne,answerTwo,answerThree);
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    public void reFresh(View view) {
        WeChatClient.getInstance(this).getQurtImage();
    }

    @Override
    public void onNetChange(int netMobile) {
        if (netMobile == 0 || netMobile == 1) {
            connectNetWork();
        }else if (netMobile == -1) {
            disConnectNetWork();
        }
    }

    @Override
    public void vuiOpenApp() {

    }

    @Override
    public void openChatWin(Intent intent) {

    }

    @Override
    public void sendMessageToFri(Intent intent) {

    }

    @Override
    public void showAllFriends(Intent intent) {

    }

    @Override
    public void sendFriContent(String queryid,String content) {
        ((InformationFragment)fragments.get(1)).sendVuiContent(queryid,content);
    }

    @Override
    public void sendExecuteContent(String queryId) {
        ((InformationFragment)fragments.get(1)).sendExecuteContent(queryId);
    }

    @Override
    public void inquerySendPerson(String query) {
        ((InformationFragment)fragments.get(1)).updateQueryIdLegacy(query);
    }

    @Override
    public void stopWechatPlay() {
        ((InformationFragment)fragments.get(1)).stopWechatPlay();
        ((SetFragment)fragments.get(2)).stopWechatPlay();
    }

    @Override
    public void openWechatPlay() {
        ((InformationFragment)fragments.get(1)).openWechatPlay();
        ((SetFragment)fragments.get(2)).openWechatPlay();
    }

    @Override
    public void vuiCloseApp() {
        Intent intentHome = new Intent();
        intentHome.setAction(Intent.ACTION_MAIN);
        intentHome.addCategory(Intent.CATEGORY_HOME);
        startActivity(intentHome);
    }

    @Override
    public void vuiLogout() {
        changeToSet();
        ((SetFragment)fragments.get(2)).setmLogOutAccount();
    }

    @Override
    public void closeNotification() {
        WinDialog winDialog = DataDealUtil.getInstance(this).getWinDialog();
        if (winDialog != null) {
            winDialog.hide();
        }
        ContactsDeal.getInstance().setFloatSwitch(false);
        ((SetFragment)fragments.get(2)).closeNotifyMessage();
    }

    @Override
    public void openNotification() {
        WinDialog winDialog = DataDealUtil.getInstance(this).getWinDialog();
        ContactsDeal.getInstance().setFloatSwitch(true);
        if (DataDealUtil.getInstance(this).getMessageBeans().size() != 0) {
            if (winDialog != null && mIsBackGround) {
                winDialog.showWeChatTipView(null);
            }
        }
        ((SetFragment)fragments.get(2)).openNotifyMessage();
    }

    @Override
    public void openContacts(Intent intent) {

    }

    @Override
    public void finishEmptyActivity() {

    }

    @Override
    public void lookUnreadMsg() {

    }
}