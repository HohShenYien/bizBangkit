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

import com.google.android.material.textfield.TextInputEditText;
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

    private DiscussReplyListAdapter discussReplyListAdapter;
    private ImageButton backBtn;
    private ArrayList<DataStructure.SimpleForumReply> replies;
    private DataStructure.SimpleForumPost details;

    private TextView votes, title, content, date, views, username;
    private TextInputEditText replyText;
    private ListView replyList;
    private ImageView upvote, downvote, replyBtn;

    public DiscussDetailPageFragment(DataStructure.SimpleForumPost post,
                                     ArrayList<DataStructure.SimpleForumReply> replies) {
        this.replies = replies;
        this.details = post;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.discuss_post_discussion, container, false);

        setUpComponents();
        setUpAdapter();
        setUpList();

        return root;
    }

    private void setUpAdapter()
    {
        discussReplyListAdapter = new DiscussReplyListAdapter(getContext(), replies);
    }


    private void setUpComponents() {
        votes = root.findViewById(R.id.vote_count);
        username = root.findViewById(R.id.post_username);
        title = root.findViewById(R.id.post_title);
        content = root.findViewById(R.id.post_content);
        date = root.findViewById(R.id.post_date);
        views = root.findViewById(R.id.view_count);
        replyList = root.findViewById(R.id.reply_list);
        upvote = root.findViewById(R.id.upvote_btn);
        downvote = root.findViewById(R.id.downvote_btn);
        replyBtn = root.findViewById(R.id.reply_btn);
        replyText = root.findViewById(R.id.reply_reply);

        votes.setText(details.postVoteCount);
        username.setText(details.username);
        title.setText(details.postTitle);
        content.setText(details.postContent);
        date.setText(details.postDate);
        views.setText(details.postViewCount);
    }

    private void setUpList() {
        replyList.setAdapter(discussReplyListAdapter);
    }

    private void setUpBtn() {

    }
}
