package net.imoran.auto.morwechat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import net.imoran.auto.morwechat.adapter.ChatFriendAdapter;
import net.imoran.auto.morwechat.adapter.InfomationAdapter;
import net.imoran.auto.morwechat.base.BaseFragment;
import net.imoran.auto.morwechat.bean.AddMsgList;
import net.imoran.auto.morwechat.bean.ChatWithFriendBean;
import net.imoran.auto.morwechat.bean.Contact;
import net.imoran.auto.morwechat.bean.UpdateMessageBean;
import net.imoran.auto.morwechat.bean.UserLocation;
import net.imoran.auto.morwechat.bean.WeMessage;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.bean.WechatBaseInfo;
import net.imoran.auto.morwechat.bean.WechatMessageBean;
import net.imoran.auto.morwechat.manager.WeChatClient;
import net.imoran.auto.morwechat.utils.AndroidUtils;
import net.imoran.auto.morwechat.utils.ContactsDeal;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.utils.MorServiceClient;
import net.imoran.auto.morwechat.utils.UploadWeChatInfoUtils;
import net.imoran.auto.morwechat.utils.VuiControlMananger;
import net.imoran.auto.morwechat.widget.WinDialog;

import net.imoran.auto.morwechat.R;
import net.imoran.personal.lib.SPHelper;
import net.imoran.rripc.lib.ResponseCallback;
import net.imoran.tv.common.lib.adapter.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by xinhuashi on 2018/7/26.
 */

public class InformationFragment extends BaseFragment {
    private static final String TAG = "InformationFragment";
    private ViewStub chatView;
    private TextView nickName;
    private CopyOnWriteArrayList<WeMessageBean> messageBeans;
    private RecyclerView mPresonList;
    private InfomationAdapter adapter;
    private ChatFriendAdapter chatAdapter;
    private List<ChatWithFriendBean> chatBeans;
    private Map<String,List<ChatWithFriendBean>> chatWithUser;
    public static Map<String,List<String>> mUnreadMessage;
    private RelativeLayout chatPersonList;
    private ImageView ivBack;
    private RecyclerView chatList;
    private ImageView chatInputSend;
    private EditText mEditText;
    private String toChatUserName;
    private String toChatNickName;
    private LinearLayout mNormalAnswer;
    private LinearLayout mFastAnswer;
    private ImageView mShowNorInput;
    private ImageView mChatInputBack;
    private ImageView mUserPosition;
    private TextView chatFastInput;
    private int indexFastAnswer;
    private ImageView mChatFastInputSend;
    private Gson gson;
    private RelativeLayout infoListControl;
    private TextView lastPage;
    private TextView nextPage;
    private TextView currentPage;
    private int mCurrentPage = 1;
    private int mTotalPage;
    private int mLastPageItem;
    private WinDialog winDialog;
    public static volatile boolean mIsAppBackGround;
    private LinearLayout mMsgWhiteList;
    private RelativeLayout mWhiteListControl;
    private TextView mCancelAddWhiteList;
    private TextView mAddWhiteList;
    private ImageView mIsAddWhiteList;
    private ImageView mIvChatExtend;
    private ImageView mSoundNotify;
    private View mInflateView; //延迟加载的view
    private List<String> fastInputAnswer = new ArrayList<>();

    @Override
    protected int getLayoutResID() {
        return R.layout.fragment_info;
    }

    @Override
    protected void onViewCreate(@Nullable Bundle savedInstanceState) {
        initView();
    }

    public void showChatView(Contact contact) {
        String friendName;
        chatView.setVisibility(View.VISIBLE);
        chatPersonList.setVisibility(View.GONE);
        if ("".equals(contact.getRemarkName()) || contact.getRemarkName() == null) {
            friendName = contact.getNickName();
        }else {
            friendName = contact.getRemarkName();
        }
        nickName.setText(Html.fromHtml(friendName));
        List<ChatWithFriendBean> chatWithFriendBeans = chatWithUser.get(contact.getUserName());
        if (chatWithFriendBeans == null) {
            chatWithFriendBeans = new ArrayList<>();
            chatWithUser.put(contact.getUserName(),chatWithFriendBeans);
        }
        chatAdapter.update(chatWithFriendBeans);
        toChatUserName = contact.getUserName();
        toChatNickName = "".equals(contact.getRemarkName())? contact.getNickName():contact.getRemarkName();
        chatFastInput.setText(fastInputAnswer.get(0));
    }

    public void showChatView(WeMessageBean contact,String queryId) {
        showChatView(contact);
        updateQueryId(getPageId(),queryId);
    }

    public void showChatView(WeMessageBean contact) {
        if (contact == null) return;
        if (mInflateView == null) {
            chatView.inflate();
            mInflateView = rootView.findViewById(R.id.inflate_chat_view);
            delayInitView();
        }
        if (DataDealUtil.getInstance(mContext).isAddWhiteListFriends(contact.getNickName())) {
            mIsAddWhiteList.setVisibility(View.VISIBLE);
        }else {
            mIsAddWhiteList.setVisibility(View.GONE);
        }
        List<String> unReadMessages = mUnreadMessage.get(contact.getUserName());
        if (unReadMessages != null && unReadMessages.size() != 0) {
            unReadMessages.clear();
        }
        mInflateView.setVisibility(View.VISIBLE);
        chatPersonList.setVisibility(View.GONE);
        if ("".equals(contact.getRemarkName()) || contact.getRemarkName() == null) {
            nickName.setText(Html.fromHtml(contact.getNickName()) + "");
        }else {
            nickName.setText(Html.fromHtml(contact.getRemarkName()) + "");
        }
        List<ChatWithFriendBean> chatWithFriendBeans = chatWithUser.get(contact.getUserName());
        if (chatWithFriendBeans == null) {
            chatWithFriendBeans = new ArrayList<>();
            chatWithUser.put(contact.getUserName(),chatWithFriendBeans);
        }
        chatAdapter.update(chatWithFriendBeans);
        toChatUserName = contact.getUserName();
        toChatNickName = contact.getNickName();
        chatFastInput.setText(fastInputAnswer.get(0));
    }

    public void resumeFragment() {
        if (winDialog != null) {
            winDialog.hide();
        }
        mIsAppBackGround = false;
    }



    public void pauseFragment() {
        mIsAppBackGround = true;
        if (DataDealUtil.getInstance(mContext).getMessageBeans().size() != 0) {
            winDialog.showWeChatTipView(null);
        }
    }

    public void  releaseResource(){
        if (messageBeans != null) {
            messageBeans.clear();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (chatWithUser != null) {
            chatWithUser.clear();
        }
        if (chatBeans != null) {
            chatBeans.clear();
        }
        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        }
        if (chatView != null) {
            chatView.setVisibility(View.GONE);
        }
        if (winDialog != null) {
            winDialog.hide();
        }
    }

    private boolean isSamePersonSendMessage;
    private WechatBaseInfo baseInfo;
    private void initView() {
        chatView = rootView.findViewById(R.id.chat_view);
        mPresonList = rootView.findViewById(R.id.rl_person_list);
        chatPersonList = rootView.findViewById(R.id.chat_person_list);
        infoListControl = rootView.findViewById(R.id.info_list_control);
        lastPage = rootView.findViewById(R.id.lastpage);
        nextPage = rootView.findViewById(R.id.nextpage);
        currentPage = rootView.findViewById(R.id.currentinfoPage);
        mMsgWhiteList = rootView.findViewById(R.id.msg_white_list);
        mWhiteListControl = rootView.findViewById(R.id.white_list_control);
        mCancelAddWhiteList = rootView.findViewById(R.id.cancel_addwhite_list);
        mAddWhiteList = rootView.findViewById(R.id.addwhite_list);
        mSoundNotify = rootView.findViewById(R.id.msg_notify);
        messageBeans = new CopyOnWriteArrayList<>();
        chatWithUser = new HashMap<>();
        mUnreadMessage = new HashMap<>();
        gson = new Gson();
        winDialog = DataDealUtil.getInstance(mContext).getWinDialog();
        fastInputAnswer.add("我在开车，稍后回复~");
        fastInputAnswer.add("我很忙~");
        fastInputAnswer.add("用户自定义");
        DataDealUtil.getInstance(mContext).setFastInputAnswer(fastInputAnswer);
        adapter = new InfomationAdapter(mContext,messageBeans);
        adapter.setOnItemClickListener(new InfomationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(WeMessageBean contact) {
                showChatView(contact);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mPresonList.setLayoutManager(layoutManager);
        mPresonList.setAdapter(adapter);
        WeChatClient.getInstance(mContext).setUpdateInformationListener(new WeChatClient.UpdateInformationListener() {
            @Override
            public void updateInfoMsg(WeMessage message, Contact contact,boolean isFromMsg) {
                dealWindowMsgCount(contact,message,isFromMsg);
                if (messageBeans.size() == 0) {
                    WeMessageBean bean = new WeMessageBean();
                    bean.setUserName(contact.getUserName());
                    bean.setNickName(contact.getNickName());
                    bean.setRemarkName(contact.getRemarkName());
                    genFromMsgBean(contact,message,isFromMsg);
                    bean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                    List<String> contents = new ArrayList<>();
                    contents.add(message.getAddMsgList().get(0).getContent());
                    bean.setContents(contents);
                    messageBeans.add(bean);
                }else {
                    for (WeMessageBean messageBean : messageBeans) {
                        if (messageBean.getUserName().equals(contact.getUserName())) {
                            messageBeans.remove(messageBean);
                            messageBeans.add(0,messageBean);
                            messageBean.getContents().add(message.getAddMsgList().get(0).getContent());
                            messageBean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                            isSamePersonSendMessage = true;
                            addChatFriendsBean(contact,message,isFromMsg);
                        }
                    }
                    if (!isSamePersonSendMessage) {
                        WeMessageBean bean = new WeMessageBean();
                        bean.setUserName(contact.getUserName());
                        bean.setNickName(contact.getNickName());
                        bean.setRemarkName(contact.getRemarkName());
                        bean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                        List<String> contents = new ArrayList<>();
                        genFromMsgBean(contact,message,isFromMsg);
                        contents.add(message.getAddMsgList().get(0).getContent());
                        bean.setContents(contents);
                        messageBeans.add(0,bean);
                    }
                }
                isSamePersonSendMessage = false;
                adapter.notifyDataSetChanged();
                dealWithTotalPage();
                if (chatWithUser.get(toChatUserName) == null || chatAdapter == null) return;
                chatAdapter.notifyDataSetChanged();
                int size = chatWithUser.get(toChatUserName).size() - 1;
                chatList.scrollToPosition(size);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) chatList.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(size, 0);
            }

            @Override
            public void updateRecentContacts(List<Contact> contacts) {
                for (Contact contact : contacts) {
                    if (contact.getVerifyFlag() == 0) {
                        if (messageBeans.size() == 0) {
                            WeMessageBean bean = new WeMessageBean();
                            bean.setUserName(contact.getUserName());
                            bean.setNickName(contact.getRemarkName());
                            bean.setRemarkName(contact.getNickName());
                            bean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                            List<String> contents = new ArrayList<>();
                            contents.add(null);
                            bean.setContents(contents);
                            messageBeans.add(bean);
                        }else {
                            for (WeMessageBean messageBean : messageBeans) {
                                if (messageBean.getUserName().equals(contact.getUserName())) {
                                    messageBeans.remove(messageBean);
                                    messageBeans.add(0,messageBean);
                                    messageBean.getContents().add(null);
                                    isSamePersonSendMessage = true;
                                }
                            }
                            if (!isSamePersonSendMessage) {
                                WeMessageBean bean = new WeMessageBean();
                                bean.setUserName(contact.getUserName());
                                bean.setNickName(contact.getNickName());
                                bean.setRemarkName(contact.getRemarkName());
                                bean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                                List<String> contents = new ArrayList<>();
                                contents.add(null);
                                bean.setContents(contents);
                                messageBeans.add(0,bean);
                            }
                        }
                        isSamePersonSendMessage = false;
                        adapter.notifyDataSetChanged();
                        dealWithTotalPage();
                    }
                }
            }
        });

        mMsgWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter != null && mWhiteListControl != null && infoListControl != null) {
                    mWhiteListControl.setVisibility(View.VISIBLE);
                    infoListControl.setVisibility(View.GONE);
                    adapter.setSelectAddWhiteList(true);
                    DataDealUtil.getInstance(mContext).initMessageBeans();
                }
            }
        });

        mSoundNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContactsDeal.getInstance().getSoundSwitch()) {
                    mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_normal);
                    ContactsDeal.getInstance().setSoundSwitch(false);
                }else {
                    mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_selected);
                    ContactsDeal.getInstance().setSoundSwitch(true);
                }

            }
        });

        mCancelAddWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mWhiteListControl != null && infoListControl != null && adapter != null){
                    mWhiteListControl.setVisibility(View.GONE);
                    infoListControl.setVisibility(View.VISIBLE);
                    adapter.setSelectAddWhiteList(false);
                    DataDealUtil.getInstance(mContext).canCelSaveWhiteListFriends();
                }
            }
        });

        mAddWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (DataDealUtil.getInstance(mContext).saveWhiteListFriend(mContext)) {
                    if (mWhiteListControl != null && infoListControl != null && adapter != null) {
                        mWhiteListControl.setVisibility(View.GONE);
                        infoListControl.setVisibility(View.VISIBLE);
                        adapter.setSelectAddWhiteList(false);
                    }
                }
            }
        });

        lastPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTotalPage < 2) return;
                if (mCurrentPage > 1) {
                    mCurrentPage--;
                    int position = (mCurrentPage - 1) * 6;
                    mPresonList.scrollToPosition(position);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mPresonList.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(position, 0);
                    currentPage.setText(mCurrentPage + "/" + mTotalPage);
                }
                if (mCurrentPage == 1) {
                    setViewGroupLayoutParamsNormal();
                }
                if (mCurrentPage > 1) {
                    lastPage.setSelected(true);
                    if (mCurrentPage < mTotalPage) {
                        nextPage.setSelected(true);
                    }
                } else if (mCurrentPage > 0) {
                    lastPage.setSelected(false);
                    nextPage.setSelected(true);
                }

            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTotalPage < 2) return;
                if (mCurrentPage < mTotalPage) {
                    int position = mCurrentPage * 6;
                    mPresonList.scrollToPosition(position);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mPresonList.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(position, 0);
                    mCurrentPage++;
                    currentPage.setText(mCurrentPage + "/" + mTotalPage);
                }

                if (mCurrentPage < mTotalPage) {
                    nextPage.setSelected(true);
                    if (mCurrentPage > 1) {
                        lastPage.setSelected(true);
                    }
                } else if (mCurrentPage > 0) {
                    setViewGroupLayoutParams();
                    nextPage.setSelected(false);
                    lastPage.setSelected(true);
                }
            }
        });

        winDialog.setOnSendMessageListener(new WinDialog.OnSendMessageListener() {
            @Override
            public void onSendMsg(String content, WeMessageBean weMessageBean) {
                toChatNickName = weMessageBean.getNickName();
                toChatUserName = weMessageBean.getUserName();
                sendContentToOther(content,null);
            }

            @Override
            public void onSendAddress(UserLocation userLocation, String userName,String nickName) {
                toChatUserName = userName;
                toChatNickName = nickName;
                sendContentToOther(userLocation.getAddress(),userLocation);
            }

            @Override
            public void onUpdateCurrentMessage(WeMessageBean weMessageBean) {
                mCurrentMessageBean = weMessageBean;
            }
        });
    }

    public void loginSuccess() {
        if (chatPersonList != null) {
            chatPersonList.setVisibility(View.VISIBLE);
        }
    }

    public void delayInitView() {
        nickName = mInflateView.findViewById(R.id.nick_name);
        chatList = mInflateView.findViewById(R.id.chat_list);
        ivBack = mInflateView.findViewById(R.id.ic_back);
        chatInputSend = mInflateView.findViewById(R.id.chat_input_send);
        mChatFastInputSend = mInflateView.findViewById(R.id.chat_fast_input_send);
        mEditText = mInflateView.findViewById(R.id.chat_input);
        mNormalAnswer = mInflateView.findViewById(R.id.normal_answer);
        mFastAnswer = mInflateView.findViewById(R.id.fast_answer);
        mShowNorInput = mInflateView.findViewById(R.id.show_nor_input);
        mChatInputBack = mInflateView.findViewById(R.id.chat_input_back);
        chatFastInput = mInflateView.findViewById(R.id.chat_fast_input);
        mUserPosition = mInflateView.findViewById(R.id.send_position);
        mIvChatExtend = mInflateView.findViewById(R.id.chat_input_extend);
        mIsAddWhiteList = mInflateView.findViewById(R.id.is_add_white_list);
        LinearLayoutManager chatLayoutManager = new LinearLayoutManager(mContext);
        chatList.setLayoutManager(chatLayoutManager);
        chatBeans = new ArrayList<>();
        chatAdapter = new ChatFriendAdapter(mContext,chatBeans);
        chatList.addItemDecoration(new SpaceItemDecoration(10));
        chatList.setAdapter(chatAdapter);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatView.setVisibility(View.GONE);
                chatPersonList.setVisibility(View.VISIBLE);
                AndroidUtils.hiddenSoftInput(mContext,mEditText);
                mNormalAnswer.setVisibility(View.GONE);
                mFastAnswer.setVisibility(View.VISIBLE);
            }
        });
        chatInputSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendContent = mEditText.getText().toString().trim();
                if (sendContent == null || "".equals(sendContent)) return;
                sendContentToOther(sendContent,null);
            }
        });
        mShowNorInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNormalAnswer.setVisibility(View.VISIBLE);
                mFastAnswer.setVisibility(View.GONE);
                AndroidUtils.showSoftInput(mContext,mEditText);

            }
        });
        mChatInputBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNormalAnswer.setVisibility(View.GONE);
                mFastAnswer.setVisibility(View.VISIBLE);
                AndroidUtils.hiddenSoftInput(mContext,mEditText);
            }
        });

        chatFastInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatFastInput.setText(fastInputAnswer.get(indexFastAnswer));
                indexFastAnswer++;
                indexFastAnswer = indexFastAnswer%3;
            }
        });
        mChatFastInputSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendContent = chatFastInput.getText().toString().trim();
                sendContentToOther(sendContent,null);
            }
        });
        mUserPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sendContent = SPHelper.getString(mContext,"userLocation","");
                UserLocation userLocation = gson.fromJson(sendContent, UserLocation.class);
                sendContentToOther(userLocation.getAddress(),userLocation);
            }
        });
        chatAdapter.setOnItemClickListener(new ChatFriendAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatWithFriendBean contact) {
                Log.e(TAG,"ChatAdapter onClick");
                String s = gson.toJson(contact.getLocationInfo());
                poiRegeo(s);
            }
        });
        mIvChatExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chatFastInput.setText(fastInputAnswer.get(indexFastAnswer));
                indexFastAnswer++;
                indexFastAnswer = indexFastAnswer%3;
            }
        });
    }

    private void updateAcceptMsgToService(WeMessage message,Contact contact,ChatWithFriendBean chatBean) {
        if (!ContactsDeal.getInstance().getSoundSwitch()) {
            return;
        }
        UpdateMessageBean updateMessageBean = new UpdateMessageBean();
        List<WechatMessageBean> list = new ArrayList<>();
        WechatMessageBean messageBean = new WechatMessageBean();
        AddMsgList addMsgList = message.getAddMsgList().get(0);
        messageBean.setChat_type("1");
        messageBean.setContact_id(addMsgList.getMsgId());
        messageBean.setContact_id_ingroup("");
        messageBean.setContact_name_ingroup("");
        messageBean.setIcon_url(contact.getHeadImgUrl());
        if (!"".equals(chatBean.getPoiname()) && chatBean.getPoiname() != null) {
            messageBean.setLatitude(chatBean.getLocationInfo().getLatitude());
            messageBean.setLongitude(chatBean.getLocationInfo().getLongitude());
            messageBean.setMessage_content(chatBean.getLabel());
        }else {
            messageBean.setMessage_content(addMsgList.getContent());
        }
        messageBean.setRemark_name(contact.getRemarkName());
        messageBean.setNickname(contact.getNickName());
        messageBean.setMessage_type(10000);
        list.add(messageBean);
        updateMessageBean.setWechat_message(list);
        String s = gson.toJson(updateMessageBean);
        String nickName = contact.getRemarkName();
        if (nickName == null || nickName.equals(""))
            nickName = contact.getNickName();
        UploadWeChatInfoUtils.sendWeChatMsg(mContext,"ttsContent",nickName+ "说" + messageBean.getMessage_content());
    }

    private WeMessageBean mCurrentMessageBean;
    private void dealWindowMsgCount(Contact contact,WeMessage message,boolean isFrom) {
        if (!DataDealUtil.getInstance(mContext).isAddWhiteListFriends(contact.getNickName())) {
            return;
        }
        if(!mIsAppBackGround) {
            return;
        }
        if (!isFrom && (winDialog.getWinType() != 0)) {
            return;
        }

        if (!isFrom && contact != null && mCurrentMessageBean != null && !contact.getUserName().equals(mCurrentMessageBean.getUserName())) return;

        WeMessageBean bean = new WeMessageBean();
        bean.setUserName(contact.getUserName());
        bean.setMapUrl(message.getAddMsgList().get(0).getUrl());
        bean.setOriealUrl(message.getAddMsgList().get(0).getOriContent());
        bean.setNickName("".equals(contact.getRemarkName())? contact.getNickName():contact.getRemarkName());
        List<String> contents = new ArrayList<>();
        contents.add(message.getAddMsgList().get(0).getContent());
        bean.setContents(contents);
        bean.setFrom(isFrom);
        winDialog.addMessageBean(bean);
    }

    public boolean onBackPressed() {
        if (chatView.getVisibility() == View.VISIBLE) {
            chatView.setVisibility(View.GONE);
            chatPersonList.setVisibility(View.VISIBLE);
            return true;
        }
        return false;
    }

    public void changeFastAnswer(String answerOne,String answerTwo,String answerThree) {
        fastInputAnswer.clear();
        fastInputAnswer.add(answerOne);
        fastInputAnswer.add(answerTwo);
        fastInputAnswer.add(answerThree);
        DataDealUtil.instance.setFastInputAnswer(fastInputAnswer);
    }

    public void dealWithTotalPage() {
        mTotalPage = (messageBeans.size() % 6 == 0 ? (messageBeans.size() / 6) : (messageBeans.size() / 6) + 1);
        mLastPageItem = (messageBeans.size() % 6 == 0 ? 6 : (messageBeans.size() % 6));
        currentPage.setText(mCurrentPage + "/" + mTotalPage);
        if (mTotalPage > 1 && mCurrentPage == 1) {
            nextPage.setSelected(true);
        }
    }

    public void stopWechatPlay() {
        if (mSoundNotify != null) {
            mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_normal);
            ContactsDeal.getInstance().setSoundSwitch(false);
        }
    }

    public void updateQueryIdLegacy(String queryId) {
//        super.updateQueryId(queryId);

        // 这里什么都不要做，只是保持和父类一样就行
        // 这个public 的方法只是因为外部由调用才保留的，这里应该去掉
        // 历史遗留的代码
//        updateQueryId(queryId,getPageId());
    }

    public void openWechatPlay() {
        if (mSoundNotify != null) {
            mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_selected);
            ContactsDeal.getInstance().setSoundSwitch(true);
        }
    }

    public void sendContentToOther(String sendContent,UserLocation userLocation) {
        baseInfo = WeChatClient.getInstance(mContext).getBaseInfo();
        if (baseInfo != null && baseInfo.getUser() != null) {
            WeChatClient.getInstance(mContext).sendMsg(sendContent,baseInfo.getUser().getUserName(),toChatUserName);
            updateNewMsg(sendContent,userLocation);
            if (mEditText != null && chatList != null && chatWithUser != null && chatWithUser.get(toChatNickName) != null) {
                mEditText.setText("");
                int size = chatWithUser.get(toChatUserName).size() - 1;
                chatList.scrollToPosition(size);
                LinearLayoutManager mLayoutManager =
                        (LinearLayoutManager) chatList.getLayoutManager();
                mLayoutManager.scrollToPositionWithOffset(size, 0);
            }
        }
    }

    public void poiRegeo(String locationJson) {
        Bundle bundle = new Bundle();
        bundle.putString("target", "map_poi_regeo");
        bundle.putString("location",locationJson);
        bundle.putString("pageid", InformationFragment.class.getName());
        Log.e(TAG,"ChatAdapter onClick1111");
        MorServiceClient.getInstance(mContext).getRrClient().send(10103, bundle, new ResponseCallback() {
            @Override
            public void onResponse(String s) {
                Log.e("Info",s);
                Log.e(TAG,"ChatAdapter onClick222");
                Intent intent = new Intent();
                intent.setAction("net.imoran.auto.action.route.map");
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("baseContentEntity", s);
                startActivity(intent);
            }

            @Override
            public void onResponseError(String s, int i) {
                Log.e(TAG,"ChatAdapter onClick3333");
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContactsDeal.getInstance().getSoundSwitch()) {
            mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_selected);
        }else {
            mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_normal);
        }
    }

    public void updateNewMsg(String content, UserLocation userLocation) {
        if (messageBeans.size() == 0 && baseInfo != null && baseInfo.getUser() != null && chatWithUser != null) {
            WeMessageBean bean = new WeMessageBean();
            bean.setUserName(toChatUserName);
            bean.setNickName(toChatNickName);
            ChatWithFriendBean chatBean = new ChatWithFriendBean();
            chatBean.setFromMsg(false);
            chatBean.setUserName(baseInfo.getUser().getUserName());
            chatBean.setContent(content);
            if (userLocation != null) {
                chatBean.setLocationInfo(userLocation.getLatitude(),userLocation.getLongitude());
                chatBean.setLabel(userLocation.getAddress());
                chatBean.setPoiname(userLocation.getPoiName());
            }
            List<ChatWithFriendBean> chatWithFriendBeans = chatWithUser.get(toChatUserName);
            if (chatWithFriendBeans == null) {
                chatWithFriendBeans = new ArrayList<>();
            }
            chatWithFriendBeans.add(chatBean);
            chatWithUser.put(toChatUserName,chatWithFriendBeans);
            bean.setCreateTime(System.currentTimeMillis() + "");
            List<String> contents = new ArrayList<>();
            contents.add(content);
            bean.setContents(contents);
            messageBeans.add(bean);
        }else {
            for (WeMessageBean messageBean : messageBeans) {
                if (messageBean.getUserName().equals(toChatUserName)) {
                    messageBeans.remove(messageBean);
                    messageBeans.add(0,messageBean);
                    messageBean.getContents().add(content);
                    messageBean.setCreateTime(String.valueOf(System.currentTimeMillis()));
                    isSamePersonSendMessage = true;
                    ChatWithFriendBean chatBean = new ChatWithFriendBean();
                    chatBean.setFromMsg(false);
                    chatBean.setUserName(baseInfo.getUser().getUserName());
                    chatBean.setContent(content);
                    if (userLocation != null) {
                        chatBean.setLocationInfo(userLocation.getLatitude(),userLocation.getLongitude());
                        chatBean.setLabel(userLocation.getAddress());
                        chatBean.setPoiname(userLocation.getPoiName());
                    }
                    List<ChatWithFriendBean> chatWithFriendBeans = chatWithUser.get(toChatUserName);
                    chatWithFriendBeans.add(chatBean);
                }
            }
            if (!isSamePersonSendMessage) {
                WeMessageBean bean = new WeMessageBean();
                bean.setUserName(toChatUserName);
                bean.setNickName(toChatNickName);
                bean.setCreateTime(System.currentTimeMillis() + "");
                List<String> contents = new ArrayList<>();
                ChatWithFriendBean chatBean = new ChatWithFriendBean();
                chatBean.setFromMsg(false);
                chatBean.setUserName(baseInfo.getUser().getUserName());
                chatBean.setContent(content);
                if (userLocation != null) {
                    chatBean.setLocationInfo(userLocation.getLatitude(),userLocation.getLongitude());
                    chatBean.setLabel(userLocation.getAddress());
                    chatBean.setPoiname(userLocation.getPoiName());
                }
                List<ChatWithFriendBean> chatWithFriendBeans = chatWithUser.get(toChatUserName);
                if (chatWithFriendBeans == null) {
                    chatWithFriendBeans = new ArrayList<>();
                }
                chatWithFriendBeans.add(chatBean);
                chatWithUser.put(toChatUserName,chatWithFriendBeans);
                contents.add(content);
                bean.setContents(contents);
                messageBeans.add(0,bean);
            }
        }
        isSamePersonSendMessage = false;
        adapter.notifyDataSetChanged();
        if (chatAdapter != null) {
            chatAdapter.notifyDataSetChanged();
        }
        dealWithTotalPage();
    }

    private void addChatFriendsBean(Contact contact,WeMessage message,boolean isFromMsg) {
        ChatWithFriendBean chatBean = new ChatWithFriendBean();
        chatBean.setFromMsg(isFromMsg);
        if (isFromMsg) {
            chatBean.setUserName(contact.getUserName());
        }else {
            if (message != null) {
                chatBean.setUserName(message.getAddMsgList().get(0).getFromUserName());
            }
        }
        if (message != null) {
            chatBean.setMapUrl(message.getAddMsgList().get(0).getUrl());
            chatBean.setOriContent(message.getAddMsgList().get(0).getOriContent());
            chatBean.setContent(message.getAddMsgList().get(0).getContent());
        }
        List<ChatWithFriendBean> chatWithFriendBeans = chatWithUser.get(contact.getUserName());
        List<String> mTempList = mUnreadMessage.get(contact.getUserName());
        if (mTempList == null) {
            mTempList = new ArrayList<>();
            mUnreadMessage.put(contact.getUserName(),mTempList);
        }
        if (chatWithFriendBeans == null) {
            chatWithFriendBeans = new ArrayList<>();
            chatWithUser.put(contact.getUserName(),chatWithFriendBeans);
        }
        chatWithFriendBeans.add(chatBean);
        if (isFromMsg && message != null) {
            mTempList.add(message.getAddMsgList().get(0).getContent());
            updateAcceptMsgToService(message,contact,chatBean);
        }
    }

    private void genFromMsgBean(Contact contact,WeMessage message,boolean isFromMsg) {
        ChatWithFriendBean chatBean = new ChatWithFriendBean();
        chatBean.setFromMsg(isFromMsg);
        List<String> mTempList = new ArrayList<>();
        if (isFromMsg) {
            if (message != null) {
                mTempList.add(message.getAddMsgList().get(0).getContent());
            }
            chatBean.setUserName(contact.getUserName());
        }else {
            if (message != null) {
                chatBean.setUserName(message.getAddMsgList().get(0).getFromUserName());
            }
        }
        if (message != null) {
            chatBean.setMapUrl(message.getAddMsgList().get(0).getUrl());
            chatBean.setOriContent(message.getAddMsgList().get(0).getOriContent());
            chatBean.setContent(message.getAddMsgList().get(0).getContent());
        }
        List<ChatWithFriendBean> list = new ArrayList<>();
        list.add(chatBean);
        if (isFromMsg && message != null) {
            updateAcceptMsgToService(message,contact,chatBean);
        }
        mUnreadMessage.put(contact.getUserName(),mTempList);
        chatWithUser.put(contact.getUserName(),list);
    }

    public void setViewGroupLayoutParams() {
        int scale = (int) mContext.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams layoutParams = mPresonList.getLayoutParams();
        layoutParams.height = mLastPageItem * 116 * scale;
        mPresonList.setLayoutParams(layoutParams);
    }

    public void setViewGroupLayoutParamsNormal() {
        int scale = (int) mContext.getResources().getDisplayMetrics().density;
        ViewGroup.LayoutParams layoutParams = mPresonList.getLayoutParams();
        layoutParams.height = 696 * scale;
        mPresonList.setLayoutParams(layoutParams);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {

        }else {
            if (ContactsDeal.getInstance().getSoundSwitch()) {
                mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_selected);
            }else {
                mSoundNotify.setImageResource(R.drawable.ic_wechat_information_speech_normal);
            }
        }
    }

    @Override
    protected ArrayList<String> getHotWords() {
        return null;
    }


    public void sendVuiContent(String queryid,String content) {
        updateQueryId(getPageId(),queryid);
        if (mNormalAnswer != null && mFastAnswer != null && mEditText != null && content != null) {
            mNormalAnswer.setVisibility(View.VISIBLE);
            mFastAnswer.setVisibility(View.GONE);
            mEditText.setText(content);
        }
    }

    public void sendExecuteContent(String queryId) {
        String sendContent = mEditText.getText().toString().trim();
        updateQueryId(queryId);
        if (sendContent == null || "".equals(sendContent)) return;
        sendContentToOther(sendContent,null);
    }
}
