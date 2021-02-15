package com.graha.purchasingapps.ui.pr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.graha.purchasingapps.PrAdapter;
import com.graha.purchasingapps.R;
import com.graha.purchasingapps.UserDataPr;

import java.util.ArrayList;

public class PrFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView rvUserPr;
    private ArrayList<UserDataPr> list = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pr, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvUserPr = view.findViewById(R.id.rvUserPr);
        rvUserPr.setHasFixedSize(true);

        list.addAll(getListUser());
        showListUser();
    }

    public ArrayList<UserDataPr> getListUser() {
        String[] dataName = getResources().getStringArray(R.array.data_name);
        int[] dataThisMonth = getResources().getIntArray(R.array.this_month);
        int[] dataLastMonth = getResources().getIntArray(R.array.last_month);
        int[] dataMonthAgo = getResources().getIntArray(R.array.last_month);
        ArrayList<UserDataPr> listUser = new ArrayList<>();
        for (int i = 0; i < dataName.length; i++) {
            UserDataPr list = new UserDataPr();
            list.setName(dataName[i]);
            list.setThisMonth(dataThisMonth[i]);
            list.setLastMonth(dataLastMonth[i]);
            list.setMonthAgo(dataMonthAgo[i]);
            listUser.add(list);
        }
        return listUser;
    }

    private void showListUser(){
        rvUserPr.setLayoutManager(new LinearLayoutManager(getActivity()));
        PrAdapter cardAdapter = new PrAdapter(list);
        rvUserPr.setAdapter(cardAdapter);
    }
}