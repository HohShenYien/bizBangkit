package com.perajuritTeknologi.bizbangkit.ui.profiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessListEvent;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;
import com.perajuritTeknologi.bizbangkit.ui.discover.BusinessListAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class ProfileDetailsFragment extends Fragment {
    private View root;
    private RecyclerView investedList;
    private InvestedAdapter adapter;
    private TextView info;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.profile_invested, container, false);
        info = root.findViewById(R.id.profile_invested_info);

        setUpList();
        setUpAdapter();
        getContents();
        setUpScrollView();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setUpList() {
        investedList = root.findViewById(R.id.invested_list);
        adapter = new InvestedAdapter(this);
    }

    private void setUpAdapter() {
        investedList.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        investedList.setLayoutManager(linearLayoutManager);
    }

    public void getContents() {
        APICaller.getInvested(((MainActivity)getActivity()).userProfile.userId);
    }

    @Subscribe
    public void handleNewContents(GetBusinessListEvent event) {
        if (event.businesses.size() == 0) {
            info.setText("No investment yet, add one in the discover tab!");
        } else {
            info.setVisibility(View.GONE);
        }
        adapter.setItems(event.businesses);
        adapter.notifyDataSetChanged();
    }

    private void setUpScrollView() {
        NestedScrollView scrollingView = root.findViewById(R.id.invested_scroll);
        scrollingView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                EventBus.getDefault().post(new ProfileScrolled(Math.min(scrollY, 300)));
            }
        });
    }
}
