package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.Utils;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;
import com.perajuritTeknologi.bizbangkit.event.EnterDiscussionDetail;
import com.perajuritTeknologi.bizbangkit.event.GetReplyListEvent;
import com.perajuritTeknologi.bizbangkit.event.ReturnToBusinessPage;
import com.perajuritTeknologi.bizbangkit.ui.discover.DetailPageFragment;
import com.perajuritTeknologi.bizbangkit.ui.discuss.DiscussDetailPageFragment;
import com.perajuritTeknologi.bizbangkit.ui.discuss.ListStyleFragmentPost;
import com.perajuritTeknologi.bizbangkit.ui.discuss.ListStyleFragmentReply;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class DiscussPage extends Fragment {
    private View root;
    private FragmentContainerView fragmentContainer;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment currentMainFragment, listFragment;

    public static int postDetailId;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_discusspage, container, false);

        setUpComponents();
        setUpFragmentManager();
        setUpFragments();
        setDefaultFragment();

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

    private void setUpComponents() {
        //filterBtn = root.findViewById(R.id.discover_filter_button);
        //changeViewBtn = root.findViewById(R.id.changeViewButton);
        fragmentContainer = root.findViewById(R.id.discuss_fragment_container);
        //toolbar = root.findViewById(R.id.discover_top_bar);
    }

    private void setDefaultFragment() {
        currentMainFragment = listFragment;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.discuss_fragment_container,
                currentMainFragment).commit();
        //changeViewBtn.setImageResource(R.drawable.list_ic);
    }

    private void setUpFragments() {
        listFragment = new ListStyleFragmentPost();
    }

    private void setUpFragmentManager() {
        fragmentManager = getFragmentManager();
    }

    private void enterDiscussionDetailFragment() {
        transaction = fragmentManager.beginTransaction();
        Fragment newFragment = new DiscussDetailPageFragment();
        transaction.replace(R.id.discuss_fragment_container,
                newFragment).commit();
    }

    @Subscribe
    public void enterDiscussion(EnterDiscussionDetail event) {
        this.postDetailId = event.id;
        enterDiscussionDetailFragment();
    }

}
