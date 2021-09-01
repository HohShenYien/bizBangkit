package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import java.util.ArrayList;

public class GetReplyListEvent {
    public final ArrayList<DataStructure.SimpleForumReply> replies;
    public GetReplyListEvent(ArrayList<DataStructure.SimpleForumReply> results) {
        this.replies = results;
    }

}
