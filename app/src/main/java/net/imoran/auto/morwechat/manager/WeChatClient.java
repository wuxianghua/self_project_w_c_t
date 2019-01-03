package net.imoran.auto.morwechat.manager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import net.imoran.auto.morwechat.WeChatApp;
import net.imoran.auto.morwechat.bean.AddMsgList;
import net.imoran.auto.morwechat.bean.Contact;
import net.imoran.auto.morwechat.bean.Quest;
import net.imoran.auto.morwechat.bean.SyncKey;
import net.imoran.auto.morwechat.bean.SynckeyMsgBean;
import net.imoran.auto.morwechat.bean.User;
import net.imoran.auto.morwechat.bean.UserLocation;
import net.imoran.auto.morwechat.bean.WechatBaseInfo;
import net.imoran.auto.morwechat.bean.WechatStatusBean;
import net.imoran.auto.morwechat.bean.XmlErrorBean;
import net.imoran.auto.morwechat.constant.Constant;
import net.imoran.auto.morwechat.constant.URLConstant;
import net.imoran.auto.morwechat.service.ServiceApi;
import net.imoran.auto.morwechat.utils.FileUtils;
import net.imoran.auto.morwechat.utils.Player;
import net.imoran.auto.morwechat.utils.XmlUtils;
import net.imoran.auto.morwechat.bean.WeMessage;
import net.imoran.auto.morwechat.utils.DataDealUtil;

import net.imoran.personal.lib.SPHelper;
import net.imoran.rripc.lib.ResponseCallback;
import net.imoran.sdk.bean.base.BaseContentEntity;
import net.imoran.sdk.bean.base.BaseSceneEntity;
import net.imoran.sdk.bean.bean.AudioProgramBean;
import net.imoran.sdk.bean.bean.BaseReply;
import net.imoran.sdk.wx.WechatClient;
import net.imoran.tv.common.lib.utils.PhoneUtils;
import net.imoran.tv.common.lib.utils.QueryUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xinhuashi on 2018/7/24.
 */

public class WeChatClient {
    private final String TAG = WeChatClient.class.getName();
    private static WeChatClient instance;
    private HandlerThread mHandler = new HandlerThread("update_login_status");
    private Handler mWorkHandler;
    private ServiceApi serviceApi;
    private String appid = "wx782c26e4c19acffb";
    private String fun = "new";
    private String lang = "zh_CN";
    private String ticket;
    private String scan;
    private String mUuid;
    private long time;
    private long r;
    private Gson gson;
    private static Context context;
    private List<Contact> contacts;
    private List<Contact> recentContacts;
    private WechatBaseInfo baseInfo;
    private FileUtils fileUtils;
    private WinXinLogStatusListener mListener;
    private WinxinGetContactsListener getContactsListener;
    private UpdateInformationListener updateInformationListener;
    private String deviceID = "e" + String.valueOf(new Random().nextLong()).substring(1, 16);
    private boolean mIsLogOut;
    private String wxSid;
    Player player;

    public void setLogOut(boolean logOut) {
        mIsLogOut = logOut;
    }

    private WeChatClient(Context context) {
        gson = new Gson();
        serviceApi = RetrofitUtils.getInstance(context).create(ServiceApi.class);
        mHandler.start();
        contacts = new ArrayList<>();
        recentContacts = new ArrayList<>();
        fileUtils = FileUtils.getInstance(context);
        mWorkHandler = new Handler(mHandler.getLooper()){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Log.e(TAG,"mWorkHandlerThread" + Thread.currentThread().getName());
                if (msg.what == 1) {
                    loginWeixin();
                }else if (msg.what == 2) {
                    initWeixin();
                }else if (msg.what == 3) {
                    Log.e("haha","sycn1111");
                    syncheckMsg();
                    updateSingleHeader(baseInfo.getUser());
                    Log.e("timesmap",System.currentTimeMillis() + "--111");
                    getContactList();
                }else if (msg.what == 4) {
                    Contact contact = msg.getData().getParcelable("contact");
                    updateSingleHeader(contact);
                }else if (msg.what == 5) {
                    if (mIsLoginSuccess) {
                        Log.e("haha","sycn");
                        syncheckMsg();
                    }
                }
            }
        };
    }

    public static WeChatClient getInstance(Context context){
        WeChatClient.context = context.getApplicationContext();
        if (instance == null) {
            synchronized (WeChatClient.class) {
                if (instance == null) {
                    instance = new WeChatClient(context);
                }
            }
        }
        return instance;
    }

    public WechatBaseInfo getBaseInfo() {
        return baseInfo;
    }

    public void setWinxinLogStatusListener(WinXinLogStatusListener logStatusListener) {
        mListener = logStatusListener;
    }

    public void setWinxinGetContactsListener(WinxinGetContactsListener mGetContactsListener) {
        getContactsListener = mGetContactsListener;
    }

    public void setUpdateInformationListener(UpdateInformationListener mUpdateInfoMsgListener) {
        updateInformationListener = mUpdateInfoMsgListener;
    }

    public interface WinXinLogStatusListener{
        void updateQurtImage(Bitmap bitmap);
        void updateLogStatus();
        void loginSuccess(String skey,String pass_ticket,String baseUrl,String contactList);
        void logOut();
        void getConcats(String skey,String pass_ticket,String baseUrl,String contactList);
    }

    public interface UpdateInformationListener{
        void updateInfoMsg(WeMessage message,Contact contact,boolean isFromMsg);
        void updateRecentContacts(List<Contact> contacts);
    }

    public interface WinxinGetContactsListener{
        void updateContacts(List<Contact> contacts);
        void startLoadContacts();
    }

    public void getQurtImage() {
        Call<ResponseBody> uuidMsg = serviceApi.getUuidMsg(appid, Constant.REDIRECT_URL,fun, lang, System.currentTimeMillis()/1000);
        uuidMsg.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = new String(response.body().bytes());
                    Pattern pattern = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
                    Matcher matcher = pattern.matcher(result);
                    while (matcher.find()) {
                        if (matcher.group(1).equals("200")) {
                            mUuid = matcher.group(2);
                            updateLoginStatus();
                            updateQurt();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"getUuidFailure" + t.toString());
            }
        });
    }

    public void updateQurt() {
        if (mUuid == null) return;
        serviceApi.getQutrCode(mUuid).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String s = null;
                try {
                    byte[] bytes = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    mListener.updateQurtImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    AuthoSuccessRunnable runnable;
    public void updateLoginStatus() {
        if (runnable == null) {
            runnable = new AuthoSuccessRunnable();
        }
        mWorkHandler.postDelayed(runnable,0);
    }

    String s = null;
    String uri = null;
    String raw = null;
    XmlErrorBean errorBean;
    Map<String,String> loginData = new HashMap<>();
    public class AuthoSuccessRunnable implements Runnable{
        @Override
        public void run() {
            time = System.currentTimeMillis()/1000;
            r = time/1579;
            if (mUuid == null) {
                mWorkHandler.postDelayed(AuthoSuccessRunnable.this,500);
                return;
            }
            serviceApi.getLoginStatus(true,mUuid,0,r,time).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() == null) {
                        return;
                    }
                    try {
                        raw = new String(response.body().bytes());
                        s = raw.substring(raw.indexOf("=")+1,raw.indexOf("=") + 4);
                        Log.e(TAG,"logstatus" + raw);
                        if (Constant.SCAN_CODE.equals(s)|| Constant.NOT_SCAN_CODE.equals(s)) {
                            mWorkHandler.postDelayed(AuthoSuccessRunnable.this,500);
                        }else if (Constant.SCAN_CODE_INVALID.equals(s)) {
                            getQurtImage();
                        }else if (Constant.LOGIN_SUCESS.equals(s)){
                            if (mListener != null) {
                                mListener.updateLogStatus();
                            }
                            Log.e("haha","mIsLoginSuccess");
                            mIsLoginSuccess = true;
                            uri = (raw.split("\""))[1];
                            String[] split = uri.split("\\?");
                            String[] split1 = split[1].split("&");
                            for (String s1 : split1) {
                                String[] split2 = s1.split("=");
                                loginData.put(split2[0],split2[1]);
                            }
                            ticket = loginData.get("ticket");
                            scan = loginData.get("scan");
                            Log.e(TAG,"loginThread" + Thread.currentThread().getName());
                            Message msg = Message.obtain();
                            msg.what = 1;
                            mWorkHandler.sendMessageDelayed(msg,0);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void loginWeixin() {
        long r = System.currentTimeMillis();
        if (Constant.WEIXIN_DOMAIN.equals(uri.split("/")[2])) {
            serviceApi.webLoginWeixinT(ticket,mUuid,Constant.LANG,scan,Constant.FUN,r).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = new String(response.body().bytes());
                        Log.e(TAG,"longinThread" + Thread.currentThread().getName());
                        if (s.substring(1,6).equals(Constant.ERROR)){
                            errorBean = XmlUtils.xmlToBean(s, XmlErrorBean.class);
                            if (errorBean.getRet().equals(Constant.RET_CODE_NORMAL)) {
                                Log.e(TAG,"initWeixin");
                                URLConstant.BASE_INIT_URL = "https://wx2.qq.com";
                                URLConstant.BASE_SYNCCHECK_URL = "https://webpush2.weixin.qq.com";
                                Message message = Message.obtain();
                                Log.e(TAG,"重点监测消息" + "初始化1111");
                                message.what = 2;
                                mWorkHandler.sendMessageDelayed(message,0);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }else {
            serviceApi.webLoginWeixin(ticket,mUuid,Constant.LANG,scan,Constant.FUN,r).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String s = new String(response.body().bytes());
                        if (s.substring(1,6).equals(Constant.ERROR)){
                            errorBean = XmlUtils.xmlToBean(s, XmlErrorBean.class);
                            if (errorBean.getRet().equals(Constant.RET_CODE_NORMAL)) {
                                Log.e(TAG,"initWeixin_TOW");
                                URLConstant.BASE_INIT_URL = "https://wx.qq.com";
                                URLConstant.BASE_SYNCCHECK_URL = "https://webpush.weixin.qq.com";
                                Message message = Message.obtain();
                                message.what = 2;
                                Log.e(TAG,"重点监测消息" + "初始化11112222");
                                mWorkHandler.sendMessageDelayed(message,0);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private void initWeixin() {
        JSONObject object=new JSONObject();
        try {
            JSONObject baseRequest=new JSONObject();
            baseRequest.put("Skey",errorBean.getSkey());
            baseRequest.put("Sid",errorBean.getWxsid());
            baseRequest.put("Uin",errorBean.getWxuin());
            baseRequest.put("DeviceID",errorBean.getPass_ticket());
            object.put("BaseRequest",baseRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        time = System.currentTimeMillis();
        serviceApi.webinitWeixin(time,errorBean.getPass_ticket(),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = new String(response.body().bytes());
                    Log.e(TAG,"重点监测消息" + s);
                    baseInfo = gson.fromJson(s,WechatBaseInfo.class);
                    if (baseInfo.getUser().getHeadImgUrl() == null || "".equals(baseInfo.getUser().getHeadImgUrl())) {
                        Message message = Message.obtain();
                        message.what = 1;
                        mWorkHandler.sendMessageDelayed(message,0);
                        return;
                    }
                    recentContacts.clear();
                    for (Contact contact : baseInfo.getContactList()) {
                        if (contact.getVerifyFlag() == 0 && contact.getMemberCount() == 0) {
                            recentContacts.add(contact);
                        }
                    }
                    if (errorBean == null) return;
                    mListener.loginSuccess(errorBean.getSkey(),errorBean.getPass_ticket(),URLConstant.BASE_INIT_URL,gson.toJson(contacts));
                    Log.e(TAG,"contact_current_thread" + Thread.currentThread().getName());
                    downloadRecentAvater(false,recentContacts);
                    DataDealUtil.getInstance(context).initData(baseInfo.getUser().getNickName(),context);
                    Message message = Message.obtain();
                    message.what = 3;
                    mWorkHandler.sendMessageDelayed(message,0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }
    private boolean mIsLoginSuccess;
    @SuppressLint("HandlerLeak")
    private Handler mMsgHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 2) {
                getContactsListener.updateContacts(contacts);
            }else if (msg.what == 3) {
                if (recentContacts != null) {
                    Log.e("reclietent","recentContacts" + recentContacts.size());
                }
                updateInformationListener.updateRecentContacts(recentContacts);
            }
        }
    };

    private void syncMsg() {
        Message msg = Message.obtain();
        msg.what = 5;
        mWorkHandler.sendMessageDelayed(msg,1000);
    }

    public void getContactList() {
        if (getContactsListener != null) {
            getContactsListener.startLoadContacts();
        }
        serviceApi.getContactList(Constant.LANG,errorBean.getPass_ticket(),System.currentTimeMillis(),"1",errorBean.getSkey()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("timesmap",System.currentTimeMillis() + "4444444444");
                String s;
                try {
                    s = new String(response.body().bytes());
                    JSONObject object=new JSONObject(s);
                    JSONArray members=object.getJSONArray("MemberList");
                    contacts.clear();
                    for (int i=0;i<members.length();i++){
                        Contact contact=gson.fromJson(members.get(i).toString(),Contact.class);
                        if (contact.getVerifyFlag() == 0) {
                            contacts.add(contact);
                        }
                    }
                    if (errorBean != null) {
                        mListener.getConcats(errorBean.getSkey(),errorBean.getPass_ticket(),URLConstant.BASE_INIT_URL,gson.toJson(contacts));
                    }
                    downloadAvater(false,contacts);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private WechatStatusBean mBean;
    public void updateContacts(WechatStatusBean bean) {
        mBean = bean;
        Quest wechat_status_change = bean.wechat_status_change;
        String key = "5B24CE667012B42F";
        String deviceId = PhoneUtils.generateDeviceId(context);
        String sendContent = SPHelper.getString(context,"userLocation","");
        UserLocation userLocation = gson.fromJson(sendContent, UserLocation.class);
        String latitude = userLocation.getLatitude();
        String longitude = userLocation.getLongitude();
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("key",key);
        builder.add("deviceid",deviceId);
        if (SPHelper.GetUser(context) != null) {
            String uid = SPHelper.GetUser(context).uid;
            builder.add("moraccountid",uid);
        }
        builder.add("longitude",longitude);
        builder.add("latitude",latitude);
        builder.add("queryid",QueryUtils.genereateQueryId());
        if ("login".equals(wechat_status_change.wechat_status)) {
            builder.add("base_url",wechat_status_change.baseUrl);
            builder.add("skey",wechat_status_change.skey);
            builder.add("pass_ticket",wechat_status_change.pass_ticket);
            builder.add("contactList",wechat_status_change.contactList);
            Log.e("updateContactList","contactList");
        }
        builder.add("wechat_status",wechat_status_change.wechat_status);
        RequestBody requestBody = builder.build();
        Log.e("updateContactList","request");
        serviceApi.updateContacts(requestBody).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    s = new String(response.body().bytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.e("updateContactList","success" + s);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                updateContacts(mBean);
                Log.e("updateContactList","failure-----" + t.toString());
            }
        });
    }

    public void updateLogoffStatus() {
        Bundle bundle = new Bundle();
        bundle.putString("target", "update_wechat_logstatus");
        bundle.putString("pageid", WechatClient.class.getName());
        WeChatApp.rrClient.send(10103, bundle, new ResponseCallback() {
            @Override
            public void onResponse(String s) {
                Log.e("hahahehe", "Success----String s" + s);
            }

            @Override
            public void onResponseError(String s, int i) {
                Log.e("hahahehe", "Error----String s" + s);
            }
        });
    }

    private SynckeyMsgBean synckeyMsgBean = new SynckeyMsgBean();
    //消息检查
    private void syncheckMsg() {
        long r = System.currentTimeMillis();
        Log.e(TAG,"syncheckMsg Thread" + Thread.currentThread().getName());
        if (mIsLogOut) {
            wxSid = "";
        }else {
            wxSid = errorBean.getWxsid();
        }
        serviceApi.syncCheckMsg(deviceID,r,wxSid,errorBean.getWxuin(),errorBean.getSkey(),getSyncKeyStr(),r).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = new String(response.body().bytes());
                    Log.e(TAG,"syncheckmsg" + "重点监测消息" + s);
                    s = (s.split("="))[1];
                    synckeyMsgBean = gson.fromJson(s,SynckeyMsgBean.class);
                    if ("1101".equals(synckeyMsgBean.getRetcode())) {
                        mListener.logOut();
                        getQurtImage();
                        mIsLoginSuccess = false;
                        errorBean = null;
                        baseInfo = null;
                        mIsLogOut = false;
                        return;
                    }
                    if ((Constant.RET_CODE_NORMAL.equals(synckeyMsgBean.getRetcode()))) {
                        if (mIsLoginSuccess) {
                            updateNewMsg();
                        }
                    }else {
                        syncMsg();
                    }
                } catch (IOException e) {
                    Log.e(TAG,"syncheck_exception" + e.toString());
                    syncMsg();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"syncheckmsg_failure" + t.toString());
                syncMsg();
            }
        });
    }
    private WeMessage message;
    private void updateNewMsg() {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), genUpdateMsgData().toString());
        serviceApi.updateNewMsg(errorBean.getWxsid(),errorBean.getSkey(),errorBean.getPass_ticket(),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String s = new String(response.body().bytes());
                    try {
                        message = gson.fromJson(s, WeMessage.class);
                    } catch (Exception e) {
                        message = null;
                    }
                    Log.e(TAG,"收到的消息为" + s);
                    if (contacts == null || contacts.size() == 0) {
                        syncMsg();
                        return;
                    }
                    if (message == null || message.getAddMsgList() == null || message.getAddMsgList().size() == 0){
                        syncMsg();
                        return;
                    }
                    String fromUserName = message.getAddMsgList().get(0).getFromUserName();
                    String toUserName = message.getAddMsgList().get(0).getToUserName();

                    if (fromUserName.substring(0,2).equals("@@")) {
                        syncMsg();
                        return;
                    }
                    if (toUserName.substring(0,2).equals("@@")) {
                        syncMsg();
                        return;
                    }
                    for (Contact contact : contacts) {
                        if (contact.getUserName().equals(baseInfo.getUser().getUserName()) || contact.getNickName().equals(baseInfo.getUser().getNickName())) {
                            continue;
                        }

                        int size = message.getAddMsgList().size();
                        String content = message.getAddMsgList().get(size - 1).getContent();
                        if ("".equals(content) || content == null) {
                            syncMsg();
                            return;
                        }
                        AddMsgList addMsgList = message.getAddMsgList().get(0);
                        if (addMsgList.getFromUserName().equals(contact.getUserName())){
                            Log.e(TAG,"我的消息为来自对方" +"----"+ addMsgList.getMsgType() + "---" + addMsgList.getContent());
                            if (addMsgList.getMsgType() != 1) {
                                addMsgList.setContent("此消息不支持，请到手机端查看");
                            }
                            updateInformationListener.updateInfoMsg(message,contact,true);
                        }if (addMsgList.getToUserName().equals(contact.getUserName())) {
                            Log.e(TAG,"我的消息为来自自己"  +"----"+ addMsgList.getMsgType() + "---" + addMsgList.getContent());
                            if (addMsgList.getMsgType() != 1) {
                                addMsgList.setContent("此消息不支持，请到手机端查看");
                            }
                            updateInformationListener.updateInfoMsg(message,contact,false);
                        }
                    }
                    syncMsg();
                    Log.e("haha","最新的消息为" + message.getAddMsgList().get(0).getContent() + "size" + message.getAddMsgList().size());
                } catch (IOException e) {
                    Log.e("消息","hadddddhah异常消息为" + e.toString());
                    syncMsg();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("消息","hadddddhah失败的消息为" + t.toString());
                syncMsg();
            }
        });
    }

    public  String getVoiceUrl(String msgid,String sky){
        return String.format(URLConstant.BASE_INIT_URL+"/cgi-bin/mmwebwx-bin/webwxgetvoice?msgid=%s&skey=%s",msgid,sky);
    }

    public JSONObject getSyncKeyJson() throws JSONException{
        SyncKey syncKey;
        if (message != null) {
            syncKey = message.getSyncKey();
        }else {
            syncKey = baseInfo.getSyncKey();
        }
        JSONObject syk=new JSONObject();
        syk.put("Count",syncKey.getCount());
        JSONArray list=new JSONArray();
        for (SyncKey.KeyValuePair pair : syncKey.getList()){
            JSONObject object=new JSONObject();
            object.put("Key",pair.getKey());
            object.put("Val",pair.getVal());
            list.put(object);
        }
        syk.put("List",list);
        return syk;
}

    public String getSyncKeyStr(){
        List<SyncKey.KeyValuePair> list;
        if (message != null) {
            list = message.getSyncKey().getList();
        }else {
            list = baseInfo.getSyncKey().getList();
        }
        String key="";
        for (SyncKey.KeyValuePair pair:list){
            key+=pair.getKey()+"_"+pair.getVal()+"|";
        }
        if (key.length()>1){
            return key.substring(0,key.lastIndexOf("|"));
        }else {
            return key;
        }
    }

    private void dealWithContacts(List<Contact> contacts) {
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return (o1.getPYInitial().compareTo(o2.getPYInitial()));
            }
        });
    }

    private void downloadRecentAvater(boolean isUpdate,List<Contact> contacts){
        for(Contact contact:contacts){
            final String name=contact.getUserName();
            final File header=new File(fileUtils.headerPath,name);
            if (header.exists()){
                if (!isUpdate)
                    continue;
            }
            Message message = Message.obtain();
            message.what = 4;
            Bundle bundle = new Bundle();
            bundle.putParcelable("contact",contact);
            message.setData(bundle);
            mWorkHandler.sendMessageDelayed(message,0);
        }
        Message message = Message.obtain();
        message.what = 3;
        mMsgHandler.sendMessageDelayed(message,1000);
    }

    private void downloadAvater(boolean isUpdate,List<Contact> contacts){
        dealWithContacts(contacts);
        for(Contact contact:contacts){
            final String name=contact.getUserName();
            final File header=new File(fileUtils.headerPath,name);
            if (header.exists()){
                if (!isUpdate)
                    continue;
            }
            Message message = Message.obtain();
            message.what = 4;
            Bundle bundle = new Bundle();
            bundle.putParcelable("contact",contact);
            message.setData(bundle);
            mWorkHandler.sendMessageDelayed(message,0);
        }
        Message message = Message.obtain();
        message.what = 2;
        mMsgHandler.sendMessageDelayed(message,1000);
    }
    Map<String,String> keyMap = new HashMap();

    public void updateSingleHeader(Contact contact){
        final String name=contact.getUserName();
        final File header=new File(fileUtils.headerPath,name);
        String[] split = contact.getHeadImgUrl().split("\\?");
        String[] split1 = split[1].split("&");
        for (String s1 : split1) {
            String[] split2 = s1.split("=");
            keyMap.put(split2[0],split2[1]);
        }
        serviceApi.getSingleImage(keyMap.get("seq"),keyMap.get("username"),keyMap.get("skey")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FileOutputStream stream= null;
                try {
                    byte[] bytes = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    stream=new FileOutputStream(header);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    Log.e(TAG,"getsingle_image" + e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"getsingle_image_failure" + t.toString());
            }
        });
    }

    public void updateSingleHeader(User user){
        final String name=user.getUserName();
        Log.e(TAG,user.getHeadImgUrl() + "----HeadUrl");
        final File header=new File(fileUtils.headerPath,name);
        String[] split = user.getHeadImgUrl().split("\\?");
        String[] split1 = split[1].split("&");
        for (String s1 : split1) {
            String[] split2 = s1.split("=");
            keyMap.put(split2[0],split2[1]);
        }
        serviceApi.getSingleImage(keyMap.get("seq"),keyMap.get("username"),keyMap.get("skey")).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                FileOutputStream stream= null;
                try {
                    byte[] bytes = response.body().bytes();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                    stream=new FileOutputStream(header);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
                    stream.flush();
                    stream.close();
                } catch (Exception e) {
                    Log.e(TAG,"getsingleImge" + e.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG,"getsingle_failure" + t.toString());
            }
        });
    }

    public JSONObject genMoblieLoginData() {
        JSONObject object = new JSONObject();
        try {
            JSONObject baseRequest = new JSONObject();
            baseRequest.put("Skey",errorBean.getSkey());
            baseRequest.put("Sid", errorBean.getWxsid());
            baseRequest.put("Uin", errorBean.getWxuin());
            baseRequest.put("DeviceID", errorBean.getPass_ticket());
            object.put("BaseRequest", baseRequest);

            object.put("Code", 3);
            object.put("FromUserName", baseInfo.getUser().getUserName());
            object.put("ToUserName", baseInfo.getUser().getUserName());
            object.put("ClientMsgId", System.currentTimeMillis() / 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public JSONObject genUpdateMsgData() {
        JSONObject object = new JSONObject();
        try {
            JSONObject baseRequest = new JSONObject();
            baseRequest.put("Skey",errorBean.getSkey());
            baseRequest.put("Sid", errorBean.getWxsid());
            baseRequest.put("Uin", errorBean.getWxuin());
            baseRequest.put("DeviceID", errorBean.getPass_ticket());
            object.put("BaseRequest", baseRequest);

            object.put("SyncKey", getSyncKeyJson());
            object.put("rr", ~System.currentTimeMillis());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    public JSONObject genSendMsgData(String content,String fromUserName,String toUserName) {
        JSONObject object = new JSONObject();
        try {
            JSONObject baseRequest = new JSONObject();
            baseRequest.put("Skey",errorBean.getSkey());
            baseRequest.put("Sid", errorBean.getWxsid());
            baseRequest.put("Uin", errorBean.getWxuin());
            baseRequest.put("DeviceID", errorBean.getPass_ticket());
            object.put("BaseRequest", baseRequest);

            JSONObject msgRequest = new JSONObject();
            msgRequest.put("Type",1);
            msgRequest.put("Content",content);
            msgRequest.put("FromUserName",fromUserName);
            msgRequest.put("ToUserName",toUserName);
            msgRequest.put("LocalID",genLocalId());
            msgRequest.put("ClientMsgId",genLocalId());
            object.put("Msg", msgRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    public String genLocalId() {
        long time = System.currentTimeMillis();
        time = time<<4 + new Random().nextInt(32);
        return String.valueOf(time);
    }

    public void sendMsg(String content,String fromUserName,String toUserName) {
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), genSendMsgData(content,fromUserName,toUserName).toString());
        serviceApi.sendMsg(errorBean.getPass_ticket(),body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("updateLogstatusese","sendmsg" + new String(response.body().bytes()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("updateLogstatusese","sendmsg_failure" + t.toString());
            }
        });
    }
}
