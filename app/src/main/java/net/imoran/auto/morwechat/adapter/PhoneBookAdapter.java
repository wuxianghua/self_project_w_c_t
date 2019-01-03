package net.imoran.auto.morwechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.utils.FileUtils;
import net.imoran.auto.morwechat.utils.ListUtils;

import net.imoran.auto.morwechat.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class PhoneBookAdapter extends RecyclerView.Adapter<PhoneBookAdapter.PhoneBookHolder> {
    private List<WeMessageBean> mList = new ArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener itemClickListener;
    private CopyOnWriteArrayList<WeMessageBean> mSelectFriends;

    public interface OnItemClickListener {
        void onItemClick(WeMessageBean contact);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PhoneBookAdapter(Context mContext, List<WeMessageBean> mList) {
        this.mContext = mContext;
        if (!ListUtils.isEmpty(mList))
            this.mList.addAll(mList);
        mLayoutInflater = LayoutInflater.from(mContext.getApplicationContext());
        mSelectFriends = new CopyOnWriteArrayList<>();
    }

    public CopyOnWriteArrayList<WeMessageBean> getSelectFriends() {
        return mSelectFriends;
    }

    @Override
    public PhoneBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhoneBookHolder(mLayoutInflater.inflate(R.layout.adapter_phone_book, parent, false));
    }

    String friendName;
    @Override
    public void onBindViewHolder(final PhoneBookHolder holder, int position) {
        final WeMessageBean messageBean = mList.get(position);
        if (mIsWhiteList) {
            holder.addWhiteList.setVisibility(View.VISIBLE);
        }else {
            holder.addWhiteList.setVisibility(View.GONE);
        }
        if(DataDealUtil.getInstance(mContext).isAddWhiteListFriends(messageBean.getNickName())) {
            holder.addWhiteList.setChecked(true);
        }else {
            holder.addWhiteList.setChecked(false);
        }
        if ("".equals(messageBean.getRemarkName()) || messageBean.getRemarkName() == null) {
            friendName = messageBean.getNickName();
            Log.e("friendNameNick",friendName);
        }else {
            friendName = messageBean.getRemarkName();
            Log.e("friendNameRemark",friendName);
        }
        holder.nickName.setText(Html.fromHtml(friendName));
        Glide.with(mContext)
                .load(new File(FileUtils.getInstance(mContext).headerPath,messageBean.getUserName()))
                .into(holder.headImage);
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) itemClickListener.onItemClick(messageBean);
            }
        });
        holder.addWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addWhiteList.isChecked()) {
                    DataDealUtil.getInstance(mContext).addWhiteListFriends(messageBean);
                }else {
                    DataDealUtil.getInstance(mContext).removeWhiteListFriends(messageBean.getNickName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(mList)) return 0;
        else return mList.size();
    }

    public void update(List<WeMessageBean> list) {
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    private boolean mIsWhiteList;

    public void setSelectAddWhiteList(boolean isWhiteList) {
        mIsWhiteList = isWhiteList;
        notifyDataSetChanged();
    }

    public static class PhoneBookHolder extends RecyclerView.ViewHolder {
        private TextView nickName;
        private ImageView headImage;
        private LinearLayout llContainer;
        private CheckBox addWhiteList;

        public PhoneBookHolder(View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.nick_name);
            headImage = itemView.findViewById(R.id.hear_image);
            llContainer = itemView.findViewById(R.id.ll_container);
            addWhiteList = itemView.findViewById(R.id.select_white_item);
        }
    }
}
