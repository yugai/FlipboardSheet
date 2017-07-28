package com.mayi.flipboardsheet;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 作者 by yugai 时间 2017/7/28.
 * ＊ 邮箱 784787081@qq.com
 */

public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.Holder> {
    Context mContext;

    public SimpleAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 30;
    }

    static class Holder extends RecyclerView.ViewHolder {

        public Holder(View itemView) {
            super(itemView);
        }
    }
}
