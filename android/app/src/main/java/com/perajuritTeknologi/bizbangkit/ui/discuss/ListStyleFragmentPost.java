package com.perajuritTeknologi.bizbangkit.ui.discuss;

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


public class ListStyleFragmentPost extends Fragment {
    private View root;
    private RecyclerView listView;
    private DiscussPostListAdapter discussPostListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discuss_post_list_layout, container, false);

        setUpComponents();
        setUpList();

        return root;
    }

    private ArrayList<DataStructure.SimpleForumPost> tmp() {
        ArrayList<DataStructure.SimpleForumPost> list = new ArrayList();
        DataStructure.SimpleForumPost temp = new DataStructure.SimpleForumPost();
        for (int i = 0; i < 50; i++) {
            list.add(temp);
        }
        return list;
    }

    private void setUpComponents() {
        listView = root.findViewById(R.id.post_lists);
        discussPostListAdapter = new DiscussPostListAdapter(tmp());
    }

    private void setUpList() {
        listView.setAdapter(discussPostListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        listView.setLayoutManager(linearLayoutManager);
    }
}
