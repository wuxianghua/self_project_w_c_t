package net.imoran.auto.morwechat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import net.imoran.auto.morwechat.bean.WeMessageBean;
import net.imoran.auto.morwechat.bean.WhiteFriendBean;
import net.imoran.auto.morwechat.utils.FileUtils;

import net.imoran.auto.morwechat.utils.DataDealUtil;
import net.imoran.auto.morwechat.utils.ListUtils;

import net.imoran.auto.morwechat.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class InfomationAdapter extends RecyclerView.Adapter<InfomationAdapter.PhoneBookHolder> {
    private CopyOnWriteArrayList<WeMessageBean> mList = new CopyOnWriteArrayList<>();
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private OnItemClickListener itemClickListener;
    private List<WhiteFriendBean> whiteFriendBeans;

    public interface OnItemClickListener {
        void onItemClick(WeMessageBean contact);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public InfomationAdapter(Context mContext, CopyOnWriteArrayList<WeMessageBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        whiteFriendBeans = new ArrayList<>();
        mLayoutInflater = LayoutInflater.from(mContext.getApplicationContext());
    }

    @Override
    public PhoneBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhoneBookHolder(mLayoutInflater.inflate(R.layout.item_chat_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final PhoneBookHolder holder, int position) {
        final WeMessageBean contactBean = mList.get(position);
        String friendName;
        if (mIsWhiteList) {
            holder.addWhiteList.setVisibility(View.VISIBLE);
        }else {
            holder.addWhiteList.setVisibility(View.GONE);
        }
        if(DataDealUtil.getInstance(mContext).isAddWhiteListFriends(contactBean.getNickName())) {
            holder.addWhiteList.setChecked(true);
        }else {
            holder.addWhiteList.setChecked(false);
        }
        if ("".equals(contactBean.getRemarkName()) || contactBean.getRemarkName() == null) {
            friendName = contactBean.getNickName();
        }else {
            friendName = contactBean.getRemarkName();
        }
        holder.nickName.setText(Html.fromHtml(friendName));
        Glide.with(mContext)
                .load(new File(FileUtils.getInstance(mContext).headerPath,contactBean.getUserName()))
                .into(holder.headImage);
        holder.llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) itemClickListener.onItemClick(contactBean);
            }
        });
        if (contactBean.getContents() == null || contactBean.getContents().size() == 0 || contactBean.getCreateTime() == null) return;
        holder.updateTime.setText(getFormatTime(Long.valueOf(contactBean.getCreateTime())));
        holder.content.setText(contactBean.getContents().get(contactBean.getContents().size() - 1));
        holder.addWhiteList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.addWhiteList.isChecked()) {
                    DataDealUtil.getInstance(mContext).addWhiteListFriends(contactBean);
                }else {
                    DataDealUtil.getInstance(mContext).removeWhiteListFriends(contactBean.getNickName());
                }
            }
        });
    }

    public String getFormatTime(Long time) {
        Date date = new Date(time);
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String format = dateFormat.format(date);
        return format;
    }

    private boolean mIsWhiteList;

    public void setSelectAddWhiteList(boolean isWhiteList) {
        mIsWhiteList = isWhiteList;
        notifyDataSetChanged();
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

    public static class PhoneBookHolder extends RecyclerView.ViewHolder {
        private TextView nickName;
        private ImageView headImage;
        private RelativeLayout llContainer;
        private TextView content;
        private TextView updateTime;
        private CheckBox addWhiteList;

        public PhoneBookHolder(View itemView) {
            super(itemView);
            nickName = itemView.findViewById(R.id.item_chat_name);
            headImage = itemView.findViewById(R.id.item_chat_image);
            llContainer = itemView.findViewById(R.id.ll_container);
            content = itemView.findViewById(R.id.item_last_info);
            updateTime = itemView.findViewById(R.id.update_time);
            addWhiteList = itemView.findViewById(R.id.select_white_item);
        }
    }
}
