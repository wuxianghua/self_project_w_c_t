package net.imoran.auto.morwechat.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import net.imoran.auto.morwechat.bean.UserLocation;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import net.imoran.auto.morwechat.bean.WeMessage;
import net.imoran.auto.morwechat.fragment.InformationFragment;
import net.imoran.auto.morwechat.utils.ContactsDeal;

import java.util.concurrent.CopyOnWriteArrayList;

import static android.content.Context.WINDOW_SERVICE;

public class WinDialog {
    private static final String TAG = "WinDialog";
    private String baseAddressUrl = "http://restapi.amap.com/v3/staticmap?markers=mid,0xFF0000,A:116.37359,39.92437&key=352c8f38a730448b4b21d35f355a3bdb";
    private final int TYPE_WECHAT_MAIN = 0;
    private final int TYPE_WECHAT_TIP = 1;
    private Context context;
    private WinWeChatMainView winWechatMainView;
    private WinWeChatTipView winWeChatTipView;
    private int type = -1;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private FrameLayout container;
    boolean isShow = false;
    private float downX, downY;
    private int screenW;
    private OnSendMessageListener mListener;
    //当前正在浮层展示的那条消息
    private WeMessageBean mCurrentMessageBean;
    private DisplayImageOptions myOptions;

    public WinDialog(Context context) {
        this.context = context;
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        screenW = context.getResources().getDisplayMetrics().widthPixels;
        container = new FrameLayout(context);
        windowManager = (WindowManager) context.getApplicationContext().getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
                WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        layoutParams.format = PixelFormat.TRANSLUCENT;
        layoutParams.width = 89;
        layoutParams.height = 138;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.x = 0;
        layoutParams.y = 220;
        weMessageBeans = new CopyOnWriteArrayList<>();
    }

    private void show() {
        if (isShow == false) {
            windowManager.addView(container, layoutParams);
            isShow = true;
        }
    }

    public int getWinType() {
        return  type;
    }

    public void hide() {
        type = -1;
        if (isShow) {
            windowManager.removeView(container);
            isShow = false;
        }
        weMessageBeans.clear();
        mCurrentMessageBean = null;
    }

    public boolean isShow () {
        return isShow;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (InformationFragment.mIsAppBackGround) {
                showWeChatTipView(weMessageBeans);
            }
        }
    };

    private CopyOnWriteArrayList<WeMessageBean> weMessageBeans;
    public void addMessageBean(WeMessageBean winMessageBean) {
        Log.e(TAG,"addMessageBean");
        if (type == TYPE_WECHAT_MAIN && !(winMessageBean.getUserName().equals(mCurrentMessageBean.getUserName()))) {
            Log.e(TAG,"addMessageBean1111");
            if (weMessageBeans.size() != 0 ) {
                Log.e(TAG,"addMessageBean2222");
                for (WeMessageBean weMessageBean : weMessageBeans) {
                    Log.e(TAG,"addMessageBean3333");
                    if (weMessageBean.getUserName().equals(winMessageBean.getUserName())) {
                        weMessageBeans.remove(weMessageBean);
                        Log.e(TAG,"addMessageBean44444");
                        weMessageBeans.add(0,winMessageBean);
                        return;
                    }
                }
                Log.e(TAG,"addMessageBean5555");
               weMessageBeans.add(winMessageBean);
            }else {
                Log.e(TAG,"addMessageBean6666");
                weMessageBeans.add(winMessageBean);
            }
            if (winWechatMainView != null) {
                Log.e(TAG,"addMessageBean77777");
                winWechatMainView.updateMessageBean(weMessageBeans);
            }
        }else {
            Log.e(TAG,"addMessageBean8888");
            if (weMessageBeans.size() != 0 ) {
                Log.e(TAG,"addMessageBean99999");
                for (WeMessageBean weMessageBean : weMessageBeans) {
                    Log.e(TAG,"addMessageBean1122");
                    if (weMessageBean.getUserName().equals(winMessageBean.getUserName())) {
                        Log.e(TAG,"addMessageBean3344");
                        weMessageBeans.remove(weMessageBean);
                    }
                }
                if (winWechatMainView != null) {
                    Log.e(TAG,"addMessageBean0000");
                    winWechatMainView.updateMessageBean(weMessageBeans);
                }
                weMessageBeans.add(winMessageBean);
            }
            showWeChatMainView(winMessageBean);
        }
    }

    public interface OnSendMessageListener {
        void onSendMsg(String content,WeMessageBean weMessageBean);
        void onSendAddress(UserLocation userLocation, String userName, String nickName);
        void onUpdateCurrentMessage(WeMessageBean weMessageBean);
    }

    public void setOnSendMessageListener(OnSendMessageListener listener) {
        mListener = listener;
    }

    private WinWeChatBaseView mWinView;
    private void setContentView(final WinWeChatBaseView winView, int width, int height) {
        container.removeAllViews();
        container.addView(winView);
        layoutParams.width = width;
        layoutParams.height = height;
        mWinView = winView;
        winView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                float x = event.getRawX();
                float y = event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = (int) event.getX();
                        downY = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        WinDialog.this.layoutParams.x = (int) (x - downX);
                        int ly = (int) (y - downY);
                        if (ly <= 220) ly = 220;
                        WinDialog.this.layoutParams.y = ly;
                        windowManager.updateViewLayout(container, WinDialog.this.layoutParams);
                        break;
                    case MotionEvent.ACTION_UP:
                        int upX = (int) event.getX();
                        int upY = (int) event.getY();
                        if (Math.abs(upX - downX) < 0.5 && Math.abs(upY - downY) < 0.5) {
                            onClick(winView);
                        }
                        break;
                }
                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendMessageDelayed(Message.obtain(),10000);
                return true;
            }
        });

        if (!isShow) {
            show();
        } else {
            windowManager.updateViewLayout(container, this.layoutParams);
        }
    }

    private void onClick(WinWeChatBaseView view) {
        if (view instanceof WinWeChatTipView) {
            if (mCurrentMessageBean == null) return;
            showWeChatMainView(mCurrentMessageBean);
        }
    }

    public CopyOnWriteArrayList<WeMessageBean> getMessageBeans() {
        return weMessageBeans;
    }

    public void showWindowMainView() {
        onClick(mWinView);
    }


    public void showWinMainView() {
        if (weMessageBeans.size() != 0) {
            WeMessageBean remove = weMessageBeans.remove(0);
            showWeChatMainView(remove);
            if (winWechatMainView != null) {
                winWechatMainView.updateMessageBean(weMessageBeans);
            }
        }
    }

    public void showWeChatMainView(WeMessageBean messageBean) {
        if (!ContactsDeal.getInstance().getFloatSwitch()) return;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendMessageDelayed(Message.obtain(),10000);
        if(messageBean != null) {
            mCurrentMessageBean = messageBean;
            mListener.onUpdateCurrentMessage(mCurrentMessageBean);
        }
        if (winWechatMainView == null) {
            winWechatMainView = new WinWeChatMainView(context);
            winWechatMainView.setUpdateFriendMsgNumListener(new WinWeChatMainView.UpdateFriendMsgNumListener() {
                @Override
                public void onSendMessage(String content, WeMessageBean messageBean) {
                    setContentView(winWechatMainView, 740, 216);
                    mListener.onSendMsg(content,messageBean);
                }

                @Override
                public void onSendAddress(UserLocation userLocation, ImageView meAddress,String userName,String nickName) {
                    setContentView(winWechatMainView, 740, 384);
                    ImageLoader.getInstance().displayImage(baseAddressUrl.replace("A:116.37359,39.92437","A:" + userLocation.getLongitude() +","+ userLocation.getLatitude()), meAddress);
                    mListener.onSendAddress(userLocation,userName,nickName);
                }

                @Override
                public void resetTime() {
                    mHandler.removeCallbacksAndMessages(null);
                    mHandler.sendMessageDelayed(Message.obtain(),10000);
                }

                @Override
                public void releaseResource() {
                    mHandler.removeCallbacksAndMessages(null);
                }

                @Override
                public void msgNumClick() {
                    if (weMessageBeans.size() != 0) {
                        WeMessageBean remove = weMessageBeans.remove(0);
                        showWeChatMainView(remove);
                        if (winWechatMainView != null) {
                            winWechatMainView.updateMessageBean(weMessageBeans);
                        }
                    }

                }
            });
        }
        int height;
        if (messageBean != null && messageBean.getMapUrl() != null && !"".equals(messageBean.getMapUrl())) {
            height = 384;
        }else {
            int length = messageBean.getContents().get(messageBean.getContents().size() - 1).length();
            int i = length / 16 + 1;
            switch (i) {
                case 1:
                    height = 216;
                    break;
                case 2:
                    height = 229;
                    break;
                case 3 :
                    height = 268;
                    break;
                case 4:
                    height = 299;
                    break;
                case 5:
                    height = 335;
                    break;
                case 6:
                    height = 369;
                    break;
                default:
                    height = 369;
                    break;
            }
        }
        setContentView(winWechatMainView, 740, height);
        winWechatMainView.setWeMessageBeans(messageBean);
        type = TYPE_WECHAT_MAIN;
    }
    private CopyOnWriteArrayList<WeMessageBean> mMessageBeans;

    public void showWeChatTipView(CopyOnWriteArrayList<WeMessageBean> messageBeans) {
        if (!ContactsDeal.getInstance().getFloatSwitch()) return;
        if (winWeChatTipView == null) {
            winWeChatTipView = new WinWeChatTipView(context);
        }
        mMessageBeans = messageBeans;
        type = TYPE_WECHAT_TIP;
        if (messageBeans == null || messageBeans.size() == 0) {
            winWeChatTipView.setNumMsgVisibility(false);
        }else {
            winWeChatTipView.setNumMsgVisibility(true);
            winWeChatTipView.setNumMsgWe(messageBeans.size());
        }
        winWeChatTipView.setVisibility(View.VISIBLE);
        setContentView(winWeChatTipView, 89, 138);
    }
}
