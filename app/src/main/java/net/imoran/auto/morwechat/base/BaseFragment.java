package net.imoran.auto.morwechat.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.imoran.auto.scenebase.lib.SceneFragment;

/**
 * Created by xinhua.shi on 2018/12/16.
 */

public abstract class BaseFragment extends SceneFragment {

    protected View rootView;
    protected Context mContext;
    protected Activity mActivity;

    protected abstract int getLayoutResID();

    protected abstract void onViewCreate(@Nullable Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutResID(), null);
        mContext = getContext();
        mActivity = getActivity();
        onViewCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}