package com.graha.purchasingapps.ui.po;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.graha.purchasingapps.R;
import com.graha.purchasingapps.UserDataPr;
import com.graha.purchasingapps.ui.pr.PrAdapter;

import java.util.ArrayList;

public class PoAdapter extends RecyclerView.Adapter<PoAdapter.ListViewHolder> {
    final ArrayList<UserDataPr> listData;

    public PoAdapter(ArrayList<UserDataPr> listData) {
        this.listData = listData;
    }

    public ArrayList<UserDataPr> getData() {
        return listData;
    }

    @NonNull
    @Override
    public PoAdapter.ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new PoAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PoAdapter.ListViewHolder holder, final int position) {
        final UserDataPr userPr = listData.get(position);
        holder.name.setText(userPr.getName());

        holder.thisMonth.setText(String.valueOf(userPr.getThisMonth()));
        holder.lastMonth.setText(String.valueOf(userPr.getLastMonth()));
        holder.monthAgo.setText(String.valueOf(userPr.getMonthAgo()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView thisMonth;
        private final TextView lastMonth;
        private final TextView monthAgo;

        ListViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
            thisMonth = itemView.findViewById(R.id.value_this_month);
            lastMonth = itemView.findViewById(R.id.value_last_month);
            monthAgo = itemView.findViewById(R.id.value_month_ago);
        }
    }
}