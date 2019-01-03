package net.imoran.auto.morwechat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import net.imoran.auto.morwechat.R;

public class WinWeChatTipView extends WinWeChatBaseView {
    private TextView numMsgWe;
    private ImageView wechatTips;

    public WinWeChatTipView(Context context) {
        super(context);
    }

    public WinWeChatTipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WinWeChatTipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void addData() {

    }

    public void setNumMsgWe(int num) {
        if (numMsgWe != null) {
            numMsgWe.setText(num+"");
        }
    }

    public void setNumMsgVisibility(boolean isVisible) {
        if (isVisible) {
            if (numMsgWe != null) {
                numMsgWe.setVisibility(VISIBLE);
            }
        }else {
            if (numMsgWe != null) {
                numMsgWe.setVisibility(GONE);
            }
        }
    }

    @Override
    protected int getViewLayoutResId() {
        return R.layout.window_tips_wechat;
    }

    @Override
    protected void initView(View rootView) {
        numMsgWe = rootView.findViewById(R.id.num_msg_we);
        wechatTips = rootView.findViewById(R.id.wechat_tips);
    }
}
