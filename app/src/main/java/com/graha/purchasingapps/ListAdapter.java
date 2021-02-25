package com.graha.purchasingapps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {
    ArrayList<UserData> mData= new ArrayList<>();

    public ListAdapter(ArrayList<UserData> list) {
    }


    public void setData(ArrayList<UserData> items) {
        mData.clear();
        mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user, viewGroup, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {
        holder.bind(mData.get(position));

        /*final UserData userPr = mData.get(position);
        holder.name.setText(userPr.getName());

        holder.prThisMonth.setText(String.valueOf(userPr.getPrThisMonth()));
        holder.prLastMonth.setText(String.valueOf(userPr.getPrLastMonth()));
        holder.prMonthAgo.setText(String.valueOf(userPr.getPrMonthAgo()));
        holder.poThisMonth.setText(String.valueOf(userPr.getPoThisMonth()));
        holder.poLastMonth.setText(String.valueOf(userPr.getPoLastMonth()));
        holder.poMonthAgo.setText(String.valueOf(userPr.getPrMonthAgo()));*/
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ListViewHolder extends RecyclerView.ViewHolder {

        private final TextView name;
        private final TextView prThisMonth;
        private final TextView prLastMonth;
        private final TextView prMonthAgo;
        private final TextView poThisMonth;
        private final TextView poLastMonth;
        private final TextView poMonthAgo;


        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_item_name);
            prThisMonth = itemView.findViewById(R.id.pr_this_month);
            prLastMonth = itemView.findViewById(R.id.pr_last_month);
            prMonthAgo = itemView.findViewById(R.id.pr_month_ago);
            poThisMonth = itemView.findViewById(R.id.po_this_month);
            poLastMonth = itemView.findViewById(R.id.po_last_month);
            poMonthAgo = itemView.findViewById(R.id.po_month_ago);
        }

        void bind(UserData userData){
            name.setText(userData.getName());
            prThisMonth.setText(String.valueOf(userData.getPrThisMonth()));
            prLastMonth.setText(String.valueOf(userData.getPrLastMonth()));
            prMonthAgo.setText(String.valueOf(userData.getPrMonthAgo()));
        }
    }
}


