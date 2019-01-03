package net.imoran.auto.morwechat.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import net.imoran.auto.morwechat.adapter.PhoneBookAdapter;
import net.imoran.auto.morwechat.base.BaseFragment;
import net.imoran.auto.morwechat.bean.Contact;
import net.imoran.auto.morwechat.bean.LetterBean;
import net.imoran.auto.morwechat.utils.ContactsDeal;
import net.imoran.auto.morwechat.utils.FileUtils;
import net.imoran.auto.morwechat.utils.VuiControlMananger;
import net.imoran.auto.morwechat.widget.MorHDecoration;
import net.imoran.auto.morwechat.UserInfoActivity;
import net.imoran.auto.morwechat.adapter.PhoneBookLetterAdapter;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.manager.WeChatClient;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.utils.ListUtils;
import net.imoran.auto.morwechat.utils.StringUtils;

import net.imoran.auto.morwechat.R;
import net.imoran.sdk.bean.bean.WechatContactBean;
import net.imoran.sdk.bean.bean.WechatMessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinhuashi on 2018/7/26.
 */

public class PhoneBookFragment extends BaseFragment {

    private static final String TAG = "PhoneBookFragment";
    private RecyclerView mRlContact;
    private PhoneBookAdapter phoneBookAdapter;
    private FileUtils fileUtils;
    List<WeMessageBean> mContacts;
    private EditText etSearch;
    private RecyclerView recyclerView;
    private PhoneBookLetterAdapter phoneBookLetterAdapter;
    LinearLayoutManager linearLayoutManager;
    private ImageView ivPrevious;
    private ImageView ivNext;
    private int index = 0;
    private boolean canSearch;
    private List<WeMessageBean> selectContacts;
    private TextView tvEmpty;
    private TextView tvError;
    private List<WeMessageBean> messageBeans;
    private LinearLayout llLoading;
    private LinearLayout mMsgNotify;
    private LinearLayout llFriendContainer;
    private RelativeLayout llWhiteListControl;
    private TextView tvAddWhiteList;
    private TextView tvCancelWhiteList;
    private LinearLayout mllSelectLetter;
    private Gson gson;
    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_phone_book;
    }

    @Override
    protected void onViewCreate(@Nullable Bundle savedInstanceState) {
        initView();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            phoneBookAdapter = new PhoneBookAdapter(mContext,messageBeans);
            ContactsDeal.getInstance().setContacts(messageBeans);
            phoneBookAdapter.setOnItemClickListener(new PhoneBookAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(WeMessageBean messageBean) {
                    if (getActivity() instanceof UserInfoActivity) {
                        ((UserInfoActivity) getActivity()).changeToChatView(messageBean);
                    }
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            mRlContact.setLayoutManager(layoutManager);
            mRlContact.setAdapter(phoneBookAdapter);
            mRlContact.addItemDecoration(new MorHDecoration());
            changeLetter(0);
        }
    };

    private void initView() {
        mRlContact = (RecyclerView) rootView.findViewById(R.id.rl_contact);
        recyclerView = rootView.findViewById(R.id.rv_letter);
        ivPrevious = rootView.findViewById(R.id.iv_previous);
        ivNext = rootView.findViewById(R.id.iv_next);
        phoneBookLetterAdapter = new PhoneBookLetterAdapter(mContext,getDefaultLetterList());
        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new MorHDecoration());
        recyclerView.setAdapter(phoneBookLetterAdapter);
        selectContacts = new ArrayList<>();
        tvEmpty = rootView.findViewById(R.id.tv_empty);
        tvError = rootView.findViewById(R.id.tv_error);
        llLoading = rootView.findViewById(R.id.ll_loading);
        etSearch = rootView.findViewById(R.id.et_search);
        mMsgNotify = rootView.findViewById(R.id.msg_white_list);
        llFriendContainer = rootView.findViewById(R.id.ll_friend_container);
        llWhiteListControl = rootView.findViewById(R.id.white_list_control);
        tvAddWhiteList = rootView.findViewById(R.id.addwhite_list);
        mllSelectLetter = rootView.findViewById(R.id.ll_letter);
        tvCancelWhiteList = rootView.findViewById(R.id.cancel_addwhite_list);
        messageBeans = new ArrayList<>();
        gson = new Gson();
        WeChatClient.getInstance(mContext).setWinxinGetContactsListener(new WeChatClient.WinxinGetContactsListener() {
            @Override
            public void updateContacts(final List<Contact> contacts) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (Contact contact : contacts) {
                            WeMessageBean bean = new WeMessageBean();
                            bean.setUserName(contact.getUserName());
                            bean.setNickName(contact.getNickName());
                            bean.setRemarkName(contact.getRemarkName());
                            bean.setPYInitial(contact.getPYInitial());
                            bean.setPYQuanPin(contact.getPYQuanPin());
                            bean.setRemarkPYInitial(contact.getRemarkPYInitial());
                            bean.setRemarkPYQuanPin(contact.getRemarkPYQuanPin());
                            messageBeans.add(bean);
                        }
                        mContacts = messageBeans;
                        mHandler.sendMessage(Message.obtain());
                    }
                }).start();
            }

            @Override
            public void startLoadContacts() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (llLoading.getVisibility() != View.VISIBLE) {
                            llLoading.setVisibility(View.VISIBLE);
                        }
                        if (llFriendContainer.getVisibility() == View.VISIBLE) {
                            llFriendContainer.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index < mLetterBeans.size() - 1)
                    index++;
                else index = mLetterBeans.size() - 1;
                changeLetter(index);
            }
        });
        ivPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index > 0)
                    index--;
                else index = 0;
                changeLetter(index);
            }
        });
        tvCancelWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (phoneBookAdapter != null && llWhiteListControl != null) {
                    phoneBookAdapter.setSelectAddWhiteList(false);
                    llWhiteListControl.setVisibility(View.GONE);
                    DataDealUtil.getInstance(mContext).canCelSaveWhiteListFriends();
                }
            }
        });
        tvAddWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataDealUtil.getInstance(mContext).saveWhiteListFriend(mContext)) {
                    if (phoneBookAdapter != null && llWhiteListControl != null) {
                        phoneBookAdapter.setSelectAddWhiteList(false);
                        llWhiteListControl.setVisibility(View.GONE);
                    }
                }
            }
        });

        phoneBookLetterAdapter.setOnItemClickListener(new PhoneBookLetterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String letter, int i) {
                findContactByLetter(letter, false);
                phoneBookAdapter.update(selectContacts);
                index = i;
                recyclerView.smoothScrollToPosition(index);
            }
        });

        mMsgNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(phoneBookAdapter != null && llWhiteListControl != null) {
                    phoneBookAdapter.setSelectAddWhiteList(true);
                    llWhiteListControl.setVisibility(View.VISIBLE);
                    DataDealUtil.getInstance(mContext).initMessageBeans();
                }
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String key = editable.toString();
                if (StringUtils.isEmpty(key)) {
                    findContactByLetter(lastLetter, true);
                    mllSelectLetter.setVisibility(View.VISIBLE);
                    if (phoneBookAdapter != null) {
                        phoneBookAdapter.update(selectContacts);
                    }
                    llFriendContainer.setVisibility(View.VISIBLE);
                    tvEmpty.setVisibility(View.GONE);
                } else {
                    findContactByKey(key.toLowerCase());
                    mllSelectLetter.setVisibility(View.GONE);
                    if (ListUtils.isEmpty(selectContacts)) {
                        llFriendContainer.setVisibility(View.GONE);
                        tvEmpty.setVisibility(View.VISIBLE);
                    } else {
                        llFriendContainer.setVisibility(View.VISIBLE);
                        tvEmpty.setVisibility(View.GONE);
                        if (phoneBookAdapter != null) {
                            phoneBookAdapter.update(selectContacts);
                        }
                    }
                }
            }
        });
    }

    private void findContactByKey(String key) {
        selectContacts.clear();
        for (WeMessageBean contactBean : mContacts) {
            if (contactBean.getNickName().contains(key) || contactBean.getPYQuanPin().contains(key)
                    ||contactBean.getRemarkName().contains(key) || contactBean.getPYInitial().contains(key)
                    ||contactBean.getRemarkPYInitial().contains(key)||contactBean.getRemarkPYQuanPin().contains(key)) {
                selectContacts.add(contactBean);
            }
        }
    }

    private void changeLetter(int index) {
        if (ListUtils.isEmpty(mLetterBeans)) return;
        phoneBookLetterAdapter.setSelect(index);
        findContactByLetter(mLetterBeans.get(index).getLetter(), false);
        phoneBookAdapter.update(selectContacts);
        recyclerView.smoothScrollToPosition(index);
        if (llLoading.getVisibility() == View.VISIBLE) {
            llLoading.setVisibility(View.GONE);
        }
        if (llFriendContainer.getVisibility() != View.VISIBLE) {
            llFriendContainer.setVisibility(View.VISIBLE);
        }
    }

    private String lastLetter;
    private String mPinyinInital;
    private void findContactByLetter(String letter, boolean force) {
        if (StringUtils.isEmpty(letter)) return;
        if (!force) if (letter.equalsIgnoreCase(lastLetter)) return;
        selectContacts.clear();
        lastLetter = letter;
        int i = (int) letter.charAt(0);
        for (WeMessageBean contactBean : mContacts) {
            if ("".equals(contactBean.getRemarkPYQuanPin())) {
                mPinyinInital = Html.fromHtml(contactBean.getPYQuanPin()).toString();
            }else {
                mPinyinInital = Html.fromHtml(contactBean.getRemarkPYQuanPin()).toString();
            }
            if (mPinyinInital.equals("")) {
                if (i == 35) {
                    selectContacts.add(contactBean);
                }
                continue;
            }
            if ((i >= 65 && i <= 90) || (i >= 97 && i <= 122)) {
                if (mPinyinInital.substring(0,1).equalsIgnoreCase(letter)) {
                    selectContacts.add(contactBean);
                }
            } else {
                if (!StringUtils.isEmpty(mPinyinInital.substring(0,1))) {
                    int j = (int) mPinyinInital.substring(0,1).charAt(0);
                    if (!((j >= 65 && j <= 90) || (j >= 97 && j <= 122))) {
                        selectContacts.add(contactBean);
                    }
                } else {
                    selectContacts.add(contactBean);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }

    private List<LetterBean> mLetterBeans;
    private List<LetterBean> getDefaultLetterList() {
        List<LetterBean> letterBeanList = new ArrayList<>();
        String[] str = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
                "O", "P", "Q", "R", "S","T","U","V","W","X","Y","Z"};
        for (String s : str) {
            LetterBean bean = new LetterBean();
            bean.setLetter(s);
            letterBeanList.add(bean);
        }
        mLetterBeans = letterBeanList;
        return letterBeanList;
    }

    public void  releaseResource(){
        if (mContacts == null) return;
        mContacts.clear();
        index = 0;
        if (selectContacts != null) {
            selectContacts.clear();
        }
        phoneBookAdapter.notifyDataSetChanged();
    }

    public void showSelectFriends(ArrayList<WechatContactBean.WechatContactEntity> selectFriends, String queryId) {
        selectContacts.clear();
        if (selectFriends != null && selectFriends.size() != 0 && mContacts != null && mContacts.size() != 0) {
            l:for (WechatContactBean.WechatContactEntity selectFriend : selectFriends) {
                Log.e(TAG,"nick" + selectFriend.getNickname());
                for (WeMessageBean mContact : mContacts) {
                    if ((mContact.getNickName().equals(selectFriend.getNickname()))){
                        Log.e(TAG,"nickName" + selectFriend.getNickname());
                        Log.e(TAG,"remarkName" + mContact.getNickName());
                        selectContacts.add(mContact);
                        continue l;
                    }
                }
            }
        }
        updateQueryId(getPageId(),queryId);
        phoneBookAdapter.update(selectContacts);
    }

    @Override
    protected ArrayList<String> getHotWords() {
        return null;
    }

    protected ArrayList<String> getVisiblePages() {
        ArrayList<String> pages = new ArrayList<>();
        pages.add(getPageId());
        return pages;
    }
}
