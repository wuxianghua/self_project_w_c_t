package net.imoran.auto.morwechat.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import net.imoran.auto.morwechat.bean.LetterBean;
import net.imoran.auto.morwechat.utils.ListUtils;

import net.imoran.auto.morwechat.R;

import java.util.ArrayList;
import java.util.List;


public class PhoneBookLetterAdapter extends RecyclerView.Adapter<PhoneBookLetterAdapter.PhoneBookHolder> {
    private List<LetterBean> mList = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private FrameLayout.LayoutParams params;
    private OnItemClickListener itemClickListener;
    private int lastIndex = 0;

    public interface OnItemClickListener {
        void onItemClick(String letter, int index);
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PhoneBookLetterAdapter(Context mContext, List<LetterBean> mList) {
        if (!ListUtils.isEmpty(mList))
            this.mList.addAll(mList);
        mLayoutInflater = LayoutInflater.from(mContext.getApplicationContext());
        params = new FrameLayout.LayoutParams(35, 53);
    }

    @Override
    public PhoneBookHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhoneBookHolder(mLayoutInflater.inflate(R.layout.adapter_phone_letter_item, parent, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(PhoneBookHolder holder, final int position) {
        final LetterBean letter = mList.get(position);
        holder.tvLetter.setText(letter.getLetter());
        if (letter.isSelect()) {
            params.width = 53;
            holder.flContainer.setLayoutParams(params);
            holder.tvLetter.setBackgroundResource(R.drawable.bg_paired_list);
        } else {
            params.width = 35;
            holder.flContainer.setLayoutParams(params);
            holder.tvLetter.setBackground(new ColorDrawable(Color.TRANSPARENT));
        }
        holder.tvLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelect(position);
                if (itemClickListener != null)
                    itemClickListener.onItemClick(letter.getLetter(), position);
            }
        });
    }

    public void update(List<LetterBean> list) {
        lastIndex = 0;
        mList.clear();
        mList.addAll(list);
        this.notifyDataSetChanged();
    }

    public void setSelect(int index) {
        if (index > -1 && index < getItemCount()) {
            mList.get(lastIndex).setSelect(false);
            notifyItemChanged(lastIndex);
            mList.get(index).setSelect(true);
            notifyItemChanged(index);
            lastIndex = index;
        }
    }

    public void moveToMiddle(Context context, RecyclerView rvView, View clkView) {
        int itemWidth = clkView.getWidth();
        int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int scrollWidth = clkView.getLeft() - (screenWidth / 2 - itemWidth / 2);
        rvView.scrollBy(scrollWidth, 0);
    }

    @Override
    public int getItemCount() {
        if (ListUtils.isEmpty(mList)) return 0;
        else return mList.size();
    }

    public static class PhoneBookHolder extends RecyclerView.ViewHolder {
        FrameLayout flContainer;
        TextView tvLetter;

        public PhoneBookHolder(View itemView) {
            super(itemView);
            flContainer = itemView.findViewById(R.id.fl_container);
            tvLetter = itemView.findViewById(R.id.tv_letter);
        }
    }
}
