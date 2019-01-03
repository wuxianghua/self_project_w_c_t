package net.imoran.auto.morwechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import net.imoran.auto.morwechat.utils.FileUtils;

import net.imoran.auto.morwechat.bean.ChatWithFriendBean;
import net.imoran.auto.morwechat.utils.ListUtils;

import net.imoran.auto.morwechat.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class ChatFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ChatWithFriendBean> mList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener itemClickListener;
    private RequestOptions myOptions;

    public interface OnItemClickListener {
        void onItemClick(ChatWithFriendBean contact);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public ChatFriendAdapter(Context mContext, List<ChatWithFriendBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mLayoutInflater = LayoutInflater.from(mContext.getApplicationContext());
        myOptions = new RequestOptions()
                .centerCrop();

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType ==ITEM_TYPE.TYPE_FROM_MSG.ordinal()) {
            return new FromMsgHolder(mLayoutInflater.inflate(R.layout.item_chat_friend, parent, false));
        }else {
            return new ToMsgHolder(mLayoutInflater.inflate(R.layout.item_chat_me, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ChatWithFriendBean chatWithFriendBean = mList.get(position);
        int itemViewType = getItemViewType(position);
        if (itemViewType == ITEM_TYPE.TYPE_FROM_MSG.ordinal()) {
            Glide.with(mContext)
                    .load(new File(FileUtils.getInstance(mContext).headerPath,chatWithFriendBean.getUserName()))
                    .into(((FromMsgHolder) holder).headImage);
            if (chatWithFriendBean.getMapUrl() != null && !"".equals(chatWithFriendBean.getMapUrl())) {
                ((FromMsgHolder) holder).llAddressInfo.setVisibility(View.VISIBLE);
                ((FromMsgHolder) holder).content.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(chatWithFriendBean.getMapUrl())
                        .apply(myOptions)
                        .into(((FromMsgHolder) holder).address);
                if ("[位置]".equals(chatWithFriendBean.getPoiname())) {
                    ((FromMsgHolder) holder).addressContent.setText(chatWithFriendBean.getLabel());
                }else {
                    ((FromMsgHolder) holder).addressContent.setText(chatWithFriendBean.getPoiname() + "\n" +chatWithFriendBean.getLabel());
                }
                ((FromMsgHolder) holder).llAddressInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(chatWithFriendBean);
                    }
                });
            }else {
                ((FromMsgHolder) holder).llAddressInfo.setVisibility(View.GONE);
                ((FromMsgHolder) holder).content.setVisibility(View.VISIBLE);
                if (chatWithFriendBean.getContent() == null) return;
                ((FromMsgHolder) holder).content.setText(Html.fromHtml(chatWithFriendBean.getContent()));
            }
        }else{
            if (chatWithFriendBean.getMapUrl() != null && !"".equals(chatWithFriendBean.getMapUrl())) {
                ((ToMsgHolder) holder).llAddressInfo.setVisibility(View.VISIBLE);
                ((ToMsgHolder) holder).content.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(chatWithFriendBean.getMapUrl())
                        .apply(myOptions)
                        .into(((ToMsgHolder) holder).address);
                if ("[位置]".equals(chatWithFriendBean.getPoiname())) {
                    ((ToMsgHolder) holder).addressContent.setText(chatWithFriendBean.getLabel());
                }else {
                    ((ToMsgHolder) holder).addressContent.setText(chatWithFriendBean.getPoiname() + "\n" +chatWithFriendBean.getLabel());
                }
                ((ToMsgHolder) holder).llAddressInfo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        itemClickListener.onItemClick(chatWithFriendBean);
                    }
                });
            }else {
                ((ToMsgHolder) holder).llAddressInfo.setVisibility(View.GONE);
                ((ToMsgHolder) holder).content.setVisibility(View.VISIBLE);
                ((ToMsgHolder) holder).content.setText(Html.fromHtml(chatWithFriendBean.getContent()));
            }
            Glide.with(mContext)
                    .load(new File(FileUtils.getInstance(mContext).headerPath,chatWithFriendBean.getUserName()))
                    .apply(myOptions)
                    .into(((ToMsgHolder) holder).headImage);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mList.get(position).isFromMsg()) {
            return ITEM_TYPE.TYPE_FROM_MSG.ordinal();
        }else {
            return ITEM_TYPE.TYPE_TO_MSG.ordinal();
        }
    }

    public static enum ITEM_TYPE {
        TYPE_FROM_MSG,
        TYPE_TO_MSG;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(mList)) return 0;
        else return mList.size();
    }

    public void update(List<ChatWithFriendBean> list) {
        this.mList = list;
        this.notifyDataSetChanged();
    }

    public static class FromMsgHolder extends RecyclerView.ViewHolder {
        private ImageView headImage;
        private TextView content;
        private ImageView address;
        private LinearLayout llAddressInfo;
        private TextView addressContent;

        public FromMsgHolder(View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.friend_avatar);
            content = itemView.findViewById(R.id.friend_content);
            address = itemView.findViewById(R.id.friend_address);
            llAddressInfo = itemView.findViewById(R.id.ll_address_info);
            addressContent = itemView.findViewById(R.id.address_content);
        }
    }

    public static class ToMsgHolder extends RecyclerView.ViewHolder {
        private ImageView headImage;
        private TextView content;
        private ImageView address;
        private LinearLayout llAddressInfo;
        private TextView addressContent;

        public ToMsgHolder(View itemView) {
            super(itemView);
            headImage = itemView.findViewById(R.id.me_avatar);
            content = itemView.findViewById(R.id.me_content);
            address = itemView.findViewById(R.id.me_address);
            llAddressInfo = itemView.findViewById(R.id.ll_address_info);
            addressContent = itemView.findViewById(R.id.address_content);
        }
    }
}
