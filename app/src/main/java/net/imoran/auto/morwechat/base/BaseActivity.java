package net.imoran.auto.morwechat.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import net.imoran.auto.morwechat.WeChatApp;
import net.imoran.auto.morwechat.utils.CommonEvent;
import net.imoran.auto.morwechat.utils.ToastUtil;

import net.imoran.auto.scenebase.lib.SceneActivity;
import net.imoran.rripc.lib.ResponseCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by xinhua.shi on 2018/12/12.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected Context context;
    protected P presenter;
    protected ProgressDialog progressDialog;
    protected Gson gson = new Gson();
    private static final String TAG = "baseActivity";

    protected abstract int getLayoutResID();

    protected abstract void onViewCreate();

    protected abstract P createPresenter();

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(getLayoutResID());
        presenter = createPresenter();
        initDialog();
        onViewCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePage();
        // 页面展示出来的时候刷新page的query时间
        //updatePageTime();
    }


    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("正在加载");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    @Override
    public void dismissDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    @Override
    public void showToast(String msg) {
        ToastUtil.longShow(this, msg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(CommonEvent empty) {
        onEventData(empty);
    }

    protected void onEventData(CommonEvent data){

    }

    /**
     * 更新当前页面默认的queryid
     *
     * @param queryId
     */
    public void updateQueryId(String queryId) {
        updateQueryId(getDefaultPageId(), queryId);
    }

    /**
     * 默认的页面id
     *
     * @return
     */
    protected String getDefaultPageId() {
        return this.getClass().getName();
    }

    /**
     * 设置当前页面为新页面
     */
    public void updateQueryId(String pageId, String queryId) {

        if (WeChatApp.rrClient == null)
            return;

        Bundle data = new Bundle();
        data.putString("updateAction", "updateQueryId");
        data.putString("pageid", pageId);
        data.putString("queryid", queryId);
        //App.serviceHelper.sendMessage(1, data);
        WeChatApp.rrClient.send(10102, data, new ResponseCallback(){
            public void onResponse(String response){
                // 接收到请求的数据
            }

            public void onResponseError(String msg, int code){
                // 出现错误
            }
        });
    }

    /**
     * 设置当前页面为新页面
     */
    protected void updatePage() {
        if (WeChatApp.rrClient == null)
            return;

        Bundle data = new Bundle();
        data.putString("updateAction", "updatePage");
        String pages = gson.toJson(getVisiblePages());
        data.putString("pageIdList", pages);
        //App.rrClient.sendMessage(1, data);

        WeChatApp.rrClient.send(10102, data, new ResponseCallback(){
            public void onResponse(String response){
                // 接收到请求的数据
            }

            public void onResponseError(String msg, int code){
                // 出现错误
            }
        });
    }

    /**
     * 返回可见的pageid的列表
     * <p>
     * 默认是返回当前的页面全名，子类可以覆盖
     *
     * @return
     */
    protected ArrayList<String> getVisiblePages() {
        ArrayList<String> pages = new ArrayList<>();
        pages.add(getDefaultPageId());
        return pages;
    }

}
