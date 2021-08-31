package com.perajuritTeknologi.bizbangkit.ui.discuss;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.perajuritTeknologi.bizbangkit.event.EnterDiscussionDetail;
import com.perajuritTeknologi.bizbangkit.page.DiscoverPage;
import com.perajuritTeknologi.bizbangkit.page.DiscussPage;
import com.perajuritTeknologi.bizbangkit.ui.discover.BusinessDetailTabAdapter;
//import com.perajuritTeknologi.bizbangkit.event.ReturnToDiscussionPage;
import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class DiscussDetailPageFragment extends Fragment{
    private View root;
    private RecyclerView replyList;
    private LinearLayout topLayout;

    private DiscussReplyListAdapter discussReplyListAdapter;
    private ImageButton backBtn;
    private DataStructure.SimpleForumReply replyDetail;
    private DataStructure.SimpleForumPost details;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_discuss_post_detail, container, false);
        //Log.d("Syahilan", DiscussPage.postDetailId + "-----");

        setUpComponents();
        setUpAdapter();
        setUpList();
        //setUpBackBtn();

        return root;
    }

    private void setUpAdapter()
    {
        //discussReplyListAdapter = new DiscussReplyListAdapter();
    }


    private void setUpComponents() {
        topLayout = root.findViewById(R.id.discuss_Detail_Container);
        replyList = root.findViewById(R.id.reply_lists);
        backBtn = root.findViewById(R.id.post_detail_back_btn);
    }

    private void setUpList() {
        replyList.setAdapter(discussReplyListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        replyList.setLayoutManager(linearLayoutManager);
    }
}
