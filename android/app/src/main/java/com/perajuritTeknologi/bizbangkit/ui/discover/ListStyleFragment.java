package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

import java.util.ArrayList;


public class ListStyleFragment extends Fragment {
    private View root;
    private RecyclerView listView;
    private BusinessListAdapter businessListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_listlayout, container, false);
        Log.d("ShenYien", "entered lists");

        setUpComponents();
        setUpList();

        return root;
    }

    private ArrayList<DataStructure.SimpleBusiness> tmp() {
        ArrayList<DataStructure.SimpleBusiness> list = new ArrayList();
        DataStructure.SimpleBusiness temp = new DataStructure.SimpleBusiness();
        for (int i = 0; i < 50; i++) {
            list.add(temp);
        }
        return list;
    }

    private void setUpComponents() {
        listView = root.findViewById(R.id.discover_lists);
        businessListAdapter = new BusinessListAdapter(tmp());
    }

    private void setUpList() {
        listView.setAdapter(businessListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        listView.setLayoutManager(linearLayoutManager);
    }
}
