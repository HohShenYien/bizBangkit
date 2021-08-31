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


public class ListStyleFragmentReply extends Fragment {
    private View root;
    private RecyclerView listView;
    private DiscussReplyListAdapter discussReplyListAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discuss_post_detail, container, false);
        Log.d("ShenYien", "entered lists");

        setUpComponents();
        setUpList();

        return root;
    }

    private ArrayList<DataStructure.SimpleForumReply> tmp() {
        ArrayList<DataStructure.SimpleForumReply> list = new ArrayList();
        DataStructure.SimpleForumReply temp = new DataStructure.SimpleForumReply();
        for (int i = 0; i < 50; i++) {
            list.add(temp);
        }
        return list;
    }

    private void setUpComponents() {
        listView = root.findViewById(R.id.reply_lists);
        discussReplyListAdapter = new DiscussReplyListAdapter(tmp());
    }

    private void setUpList() {
        listView.setAdapter(discussReplyListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        listView.setLayoutManager(linearLayoutManager);
    }

}

