package net.imoran.auto.morwechat.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.imoran.auto.morwechat.UserInfoActivity;
import net.imoran.auto.morwechat.bean.UserLocation;
import net.imoran.auto.morwechat.bean.WeMessage;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.bean.WechatBaseInfo;
import net.imoran.auto.morwechat.fragment.InformationFragment;
import net.imoran.auto.morwechat.manager.WeChatClient;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.utils.FileUtils;

import net.imoran.auto.morwechat.R;
import net.imoran.personal.lib.SPHelper;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class WinWeChatMainView extends WinWeChatBaseView {
    private final String baseAddressUrl = "http://restapi.amap.com/v3/staticmap?markers=mid,0xFF0000,A:116.37359,39.92437&key=352c8f38a730448b4b21d35f355a3bdb";
    private ImageView friendAvatar;
    private TextView friendMsg;
    private TextView numFriendMsg;
    private LinearLayout winWechatJump;
    private RelativeLayout rlNumFriendMsg;
    private TextView fastShowMsg;
    private ImageView fastSendMsg;
    private Context mContext;
    private UpdateFriendMsgNumListener mListener;
    private View mCoverAvatar;
    private TextView meMsg;
    private Handler mHandler = new Handler();
    public RelativeLayout llShowMsg;
    private ImageView mIvUserLocation;
    private Gson gson;
    private LinearLayout meAddressInfo;
    private TextView meAddressContent;
    private ImageView meAddressPic;
    private LinearLayout friendAddressInfo;
    private TextView friendAddressContent;
    private ImageView friendAddressPic;
    private ImageView mIvWindowSwitch;
    private WechatBaseInfo baseInfo;

    public WinWeChatMainView(Context context) {
        super(context);
        mContext = context;
    }

    private String poiname;
    private String label;
    public void setWeMessageBeans(WeMessageBean weMessageBean) {
        mCurrentMessageBean = weMessageBean;
        List<String> unReadMessages = InformationFragment.mUnreadMessage.get(weMessageBean.getUserName());
        if (unReadMessages != null && unReadMessages.size() != 0) {
            unReadMessages.clear();
        }
        if (mCurrentMessageBean.getMapUrl() != null && !"".equals(mCurrentMessageBean.getMapUrl())) {
            String oriContent = mCurrentMessageBean.getOriealUrl();
            label = oriContent.substring(oriContent.indexOf("label") +7, oriContent.indexOf("maptype") - 2);
            poiname = oriContent.substring(oriContent.indexOf("poiname") + 9, oriContent.indexOf("poiid")-2);
            String[] split = mCurrentMessageBean.getMapUrl().split("=");
            String[] split1 = split[1].split(",");
            if (!mCurrentMessageBean.isFrom()) {
                friendAddressInfo.setVisibility(GONE);
                meAddressInfo.setVisibility(VISIBLE);
                meMsg.setVisibility(GONE);
                friendMsg.setVisibility(GONE);
                if ("[位置]".equals(poiname)) {
                    meAddressContent.setText(label);
                }else {
                    meAddressContent.setText(poiname+ "\n" +label);
                }
                if (split1.length <2) return;
                ImageLoader.getInstance().displayImage(baseAddressUrl.replace("A:116.37359,39.92437","A:" + Float.valueOf(split1[1]) +","+ Float.valueOf(split1[0])), meAddressPic);
            }else {
                friendAddressInfo.setVisibility(VISIBLE);
                meAddressInfo.setVisibility(GONE);
                meMsg.setVisibility(GONE);
                friendMsg.setVisibility(GONE);
                if ("[位置]".equals(poiname)) {
                    friendAddressContent.setText(label);
                }else {
                    friendAddressContent.setText(poiname+ "\n" +label);
                }
                if (split1.length <2) return;
                ImageLoader.getInstance().displayImage(baseAddressUrl.replace("A:116.37359,39.92437","A:" + Float.valueOf(split1[1]) +","+ Float.valueOf(split1[0])), friendAddressPic);
            }
        }else {
            friendAddressInfo.setVisibility(GONE);
            meAddressInfo.setVisibility(GONE);
            if (!mCurrentMessageBean.isFrom()) {
                meMsg.setText(Html.fromHtml(weMessageBean.getContents().get(weMessageBean.getContents().size() - 1)));
                mCoverAvatar.setVisibility(VISIBLE);
                friendMsg.setVisibility(GONE);
                meMsg.setVisibility(VISIBLE);
            }else {
                meMsg.setVisibility(GONE);
                mCoverAvatar.setVisibility(GONE);
                friendMsg.setVisibility(VISIBLE);
                friendMsg.setText(Html.fromHtml(weMessageBean.getContents().get(weMessageBean.getContents().size() - 1)));
            }
        }
        ImageLoader.getInstance().displayImage("file://" + FileUtils.getInstance(mContext).headerPath.getAbsolutePath()+"/"+weMessageBean.getUserName(), friendAvatar);
    }

    public WinWeChatMainView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WinWeChatMainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getViewLayoutResId() {
        return R.layout.window_main_wechat;
    }

    public void setUpdateFriendMsgNumListener(UpdateFriendMsgNumListener listener) {
        mListener = listener;
    }

    public void updateMessageBean(CopyOnWriteArrayList<WeMessageBean> winMessageBean) {
        if(winMessageBean != null) {
            if (winMessageBean.size() == 0) {
                numFriendMsg.setVisibility(GONE);
            }else {
                numFriendMsg.setText(winMessageBean.size() + "");
                numFriendMsg.setVisibility(VISIBLE);
            }
        }
    }

    public interface UpdateFriendMsgNumListener{
        void onSendMessage(String content,WeMessageBean messageBean);
        void onSendAddress(UserLocation userLocation,ImageView meAddress,String userName,String nickName);
        void resetTime();
        void releaseResource();
        void msgNumClick();
    }

    private WeMessageBean mCurrentMessageBean;

    @Override
    protected void initListener() {
        winWechatJump.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.getApplicationContext().startActivity(intent);
                mListener.releaseResource();
            }
        });
        rlNumFriendMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.resetTime();
                    mListener.msgNumClick();
                }
            }
        });
        fastSendMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    meMsg.setText(DataDealUtil.getInstance(mContext).getFastInputAnswer().get(indexFastAnswer));
                    mCoverAvatar.setVisibility(VISIBLE);
                    friendMsg.setVisibility(GONE);
                    meMsg.setVisibility(VISIBLE);
                    friendAddressInfo.setVisibility(GONE);
                    meAddressInfo.setVisibility(GONE);
                    mListener.onSendMessage(DataDealUtil.getInstance(mContext).getFastInputAnswer().get(indexFastAnswer),mCurrentMessageBean);
                    mListener.resetTime();
                }
            }
        });
        fastShowMsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indexFastAnswer++;
                indexFastAnswer = indexFastAnswer%3;
                fastShowMsg.setText(DataDealUtil.instance.getFastInputAnswer().get(indexFastAnswer));
                if (mListener != null) {
                    mListener.resetTime();
                }
            }
        });

        mIvWindowSwitch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                indexFastAnswer++;
                indexFastAnswer = indexFastAnswer%3;
                fastShowMsg.setText(DataDealUtil.instance.getFastInputAnswer().get(indexFastAnswer));
                if (mListener != null) {
                    mListener.resetTime();
                }
            }
        });

        mIvUserLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.resetTime();
                }
                String sendContent = SPHelper.getString(mContext,"userLocation","");
                final UserLocation userLocation = gson.fromJson(sendContent, UserLocation.class);
                meMsg.setVisibility(GONE);
                friendMsg.setVisibility(GONE);
                friendAddressInfo.setVisibility(GONE);
                meAddressInfo.setVisibility(VISIBLE);
                baseInfo = WeChatClient.getInstance(mContext).getBaseInfo();
                if ("[位置]".equals(userLocation.getPoiName())) {
                    meAddressContent.setText(userLocation.getAddress());
                }else {
                    meAddressContent.setText(userLocation.getPoiName()+ "\n" +userLocation.getAddress());
                }
                mListener.onSendAddress(userLocation,meAddressPic,mCurrentMessageBean.getUserName(),mCurrentMessageBean.getNickName());
            }
        });

        friendAddressInfo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private int indexFastAnswer;

    @Override
    protected void initView(View rootView) {
        friendAvatar = rootView.findViewById(R.id.friend_avatar);
        friendMsg = rootView.findViewById(R.id.friend_msg);
        numFriendMsg = rootView.findViewById(R.id.num_msg);
        winWechatJump = rootView.findViewById(R.id.win_wechat_jump);
        rlNumFriendMsg = rootView.findViewById(R.id.msg_num_tip);
        fastSendMsg = rootView.findViewById(R.id.fast_send_msg);
        fastShowMsg = rootView.findViewById(R.id.fast_show_msg);
        mCoverAvatar = rootView.findViewById(R.id.friend_avatar_cover);
        meMsg = rootView.findViewById(R.id.me_msg);
        llShowMsg = rootView.findViewById(R.id.ll_show_msg);
        mIvUserLocation = rootView.findViewById(R.id.iv_user_location);
        meAddressInfo = rootView.findViewById(R.id.ll_address_info);
        meAddressContent = rootView.findViewById(R.id.address_content);
        meAddressPic = rootView.findViewById(R.id.me_address);
        friendAddressInfo = rootView.findViewById(R.id.friend_address_info);
        friendAddressContent = rootView.findViewById(R.id.friend_address_content);
        friendAddressPic = rootView.findViewById(R.id.friend_address);
        mIvWindowSwitch = rootView.findViewById(R.id.window_switch);
        gson = new Gson();
    }

    @Override
    public void addData() {

    }
}
