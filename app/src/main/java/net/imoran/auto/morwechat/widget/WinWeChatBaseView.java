package net.imoran.auto.morwechat.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

abstract class WinWeChatBaseView extends FrameLayout {
    protected Context context;
    protected View rootView;

    public WinWeChatBaseView(Context context) {
        super(context);
        init(context);
    }

    public WinWeChatBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public WinWeChatBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(final Context context) {
        this.context = context;
        View rootView = LayoutInflater.from(context).inflate(getViewLayoutResId(), null);
        initView(rootView);
        initListener();
        this.addView(rootView);
    }

    protected abstract void initListener();

    public void addData() {
    }

    protected abstract int getViewLayoutResId();

    protected abstract void initView(View rootView);
}
