package com.perajuritTeknologi.bizbangkit.ui.discuss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class DiscussReplyListAdapter extends BaseAdapter {
    private ArrayList<DataStructure.SimpleForumReply> replies;
    private Context context;

    public DiscussReplyListAdapter(Context context, ArrayList<DataStructure.SimpleForumReply> inputList) {
        replies = inputList;
    }

    public void setItems(ArrayList<DataStructure.SimpleForumReply> replies) {
        this.replies = replies;
    }

    @Override
    public int getCount() {
        return replies.size();
    }

    @Override
    public DataStructure.SimpleForumReply getItem(int position) {
        return replies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.discuss_reply_list_layout, parent, false);
        ImageView userImg = view.findViewById(R.id.reply_user_img);
        TextView userName = view.findViewById(R.id.reply_username);
        TextView content = view.findViewById(R.id.reply_content);
        TextView replyDate = view.findViewById(R.id.reply_date);

        DataStructure.SimpleForumReply reply = replies.get(position);
        if (reply.pic == null) {
            userImg.setImageResource(R.drawable.male_default);
        } else {
            userImg.setImageBitmap(reply.pic);
        }
        userName.setText(reply.username);
        content.setText(reply.replyContent);
        replyDate.setText(reply.replyDate);

        return view;
    }
}
