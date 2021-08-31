package com.perajuritTeknologi.bizbangkit.ui.discuss;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

import java.util.ArrayList;

public class DiscussReplyListAdapter extends RecyclerView.Adapter<DiscussReplyListAdapter.ViewHolder>{
    public ArrayList<DataStructure.SimpleForumReply> replies;

    public DiscussReplyListAdapter(ArrayList<DataStructure.SimpleForumReply> inputList) {
        replies = inputList;
    }


    @NonNull
    @Override
    public DiscussReplyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discuss_reply_list_layout, parent, false);
        return new DiscussReplyListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiscussReplyListAdapter.ViewHolder holder, int position) {
        //Post post = posts.get(position);
        holder.containerView.setTag(replies.get(position));
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout containerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerView = itemView.findViewById(R.id.discuss_reply_list_layout);
//            containerView.setOnClickListener(v -> {
//                DataStructure.SimplePost this_discussion = (DataStructure.SimplePost) containerView.getTag();
//                //EventBus.getDefault().post(new EnterPostDetail(this_discussion.postId));
//            });
        }
    }


}
