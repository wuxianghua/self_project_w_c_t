package net.imoran.auto.morwechat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.imoran.auto.morwechat.UserInfoActivity;
import net.imoran.auto.morwechat.base.BaseFragment;
import net.imoran.auto.morwechat.manager.WeChatClient;

import net.imoran.auto.morwechat.R;
import net.imoran.auto.morwechat.utils.ContactsDeal;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.widget.SwitchButton;
import net.imoran.auto.morwechat.widget.WinDialog;

import java.net.IDN;
import java.util.ArrayList;

/**
 * Created by xinhuashi on 2018/7/26.
 */

public class SetFragment extends BaseFragment {
    private static final String TAG = SetFragment.class.getSimpleName();
    private LinearLayout mSetView;
    private LinearLayout mLlFastAnswer;
    private ImageView mIcBack;
    private RelativeLayout mRlFastAnswer;
    private TextView mLogOutAccount;
    private RelativeLayout mChangeAccount;
    private LinearLayout mLogOutRetry;
    private TextView mLogoutEnsure;
    private TextView mLogoutCancel;
    private LinearLayout mLogoutLoading;
    private EditText userImCar;
    private EditText userImBusy;
    private EditText userImCustom;
    private TextView ensureSave;
    private SwitchButton soundSwitch;
    private SwitchButton floatSwitch;
    private WinDialog winDialog;

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_set;
    }

    @Override
    protected void onViewCreate(@Nullable Bundle savedInstanceState) {
        initView();
        initListener();
    }

    private void initListener() {
        mRlFastAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetView.setVisibility(View.GONE);
                mLlFastAnswer.setVisibility(View.VISIBLE);
            }
        });
        mIcBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetView.setVisibility(View.VISIBLE);
                mLlFastAnswer.setVisibility(View.GONE);
            }
        });
        mLogOutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetView.setVisibility(View.GONE);
                mLogOutRetry.setVisibility(View.VISIBLE);
            }
        });
        mChangeAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetView.setVisibility(View.GONE);
                mLogOutRetry.setVisibility(View.VISIBLE);
            }
        });
        mLogoutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSetView.setVisibility(View.VISIBLE);
                mLogOutRetry.setVisibility(View.GONE);
            }
        });
        mLogoutEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext,"请同时在手机端退出账户",Toast.LENGTH_SHORT).show();
                WeChatClient.getInstance(mContext).setLogOut(true);
                mLogOutRetry.setVisibility(View.GONE);
                mLogoutLoading.setVisibility(View.VISIBLE);
            }
        });

        userImCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImCustom.setCursorVisible(true);
            }
        });

        ensureSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userImCustom.setCursorVisible(false);
                userImBusy.setCursorVisible(false);
                userImCar.setCursorVisible(false);
                String answerOne = userImCar.getText().toString().trim();
                String answerTwo = userImBusy.getText().toString().trim();
                String answerThree = userImCustom.getText().toString().trim();
                ((UserInfoActivity)mActivity).changeFastAnswer(answerOne,answerTwo,answerThree);
                Toast.makeText(mContext,"已经保存修改",Toast.LENGTH_SHORT).show();
            }
        });
        soundSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    ContactsDeal.getInstance().setSoundSwitch(false);
                }else {
                    ContactsDeal.getInstance().setSoundSwitch(true);
                }
            }
        });
        floatSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (isChecked) {
                    ContactsDeal.getInstance().setFloatSwitch(false);
                }else {
                    ContactsDeal.getInstance().setFloatSwitch(true);
                }
            }
        });
    }

    public void stopWechatPlay() {
        if (soundSwitch != null) {
            soundSwitch.setChecked(true);
        }
    }

    public void openWechatPlay() {
        if (soundSwitch != null) {
            soundSwitch.setChecked(false);
        }
    }

    public void setmLogOutAccount() {
        Toast.makeText(mContext,"请同时在手机端退出账户",Toast.LENGTH_SHORT).show();
        WeChatClient.getInstance(mContext).setLogOut(true);
        if (mLogoutLoading != null && mLogOutRetry != null && mSetView != null) {
            mLogOutRetry.setVisibility(View.GONE);
            mLogoutLoading.setVisibility(View.VISIBLE);
            mSetView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void releaseResource() {
        mSetView.setVisibility(View.VISIBLE);
        mLogoutLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden) {

        }else {
            if (ContactsDeal.getInstance().getSoundSwitch()) {
                soundSwitch.setChecked(false);
            }else {
                soundSwitch.setChecked(true);
            }
            if (ContactsDeal.getInstance().getFloatSwitch()) {
                floatSwitch.setChecked(false);
            }else {
                floatSwitch.setChecked(true);
            }
        }
    }

    @Override
    protected ArrayList<String> getHotWords() {
        return null;
    }

    public void closeNotifyMessage() {
        if (floatSwitch != null) {
            floatSwitch.setChecked(true);
        }
    }

    public void openNotifyMessage() {
        if (floatSwitch != null) {
            floatSwitch.setChecked(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isVisible()) {
            Log.e("PhoneBookFragment","set");
        }
    }

    private void initView() {
        mSetView = rootView.findViewById(R.id.set_view);
        mLlFastAnswer = rootView.findViewById(R.id.fast_answer);
        mRlFastAnswer = rootView.findViewById(R.id.rl_fast_answer);
        mIcBack = rootView.findViewById(R.id.ic_back);
        mLogOutAccount = rootView.findViewById(R.id.log_out_account);
        mChangeAccount = rootView.findViewById(R.id.change_account);
        mLogOutRetry = rootView.findViewById(R.id.wechat_log_out_retry);
        mLogoutEnsure = rootView.findViewById(R.id.log_out_ensure);
        mLogoutCancel = rootView.findViewById(R.id.log_out_cancel);
        mLogoutLoading = rootView.findViewById(R.id.logout_ll_loading);
        ensureSave = rootView.findViewById(R.id.ensure_save);
        userImBusy = rootView.findViewById(R.id.user_imbusy);
        userImCar = rootView.findViewById(R.id.user_imcar);
        userImCustom = rootView.findViewById(R.id.user_custom);
        soundSwitch = rootView.findViewById(R.id.sound_switch);
        floatSwitch = rootView.findViewById(R.id.float_switch);
        winDialog = DataDealUtil.getInstance(mContext).getWinDialog();
    }
}
