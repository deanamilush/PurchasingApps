package com.graha.purchasingapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PrAdapter extends RecyclerView.Adapter<PrAdapter.ListViewHolder> {
    final ArrayList<UserDataPr> listData;

    public PrAdapter(ArrayList<UserDataPr> listData) {
        this.listData = listData;
    }

    public ArrayList<UserDataPr> getData() {
        return listData;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
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
