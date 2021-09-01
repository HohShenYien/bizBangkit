package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterDiscussionDetail;
import com.perajuritTeknologi.bizbangkit.ui.discuss.DiscussDetailPageFragment;
import com.perajuritTeknologi.bizbangkit.ui.discuss.ListStyleFragmentPost;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class DiscussPage extends Fragment {
    private View root;
    private FragmentContainerView fragmentContainer;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment currentMainFragment, listFragment;
    private ArrayList<DataStructure.SimpleForumPost> posts;

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
        fragmentContainer = root.findViewById(R.id.discuss_fragment_container);
    }

    private void setDefaultFragment() {
        currentMainFragment = listFragment;
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.discuss_fragment_container,
                currentMainFragment).commit();
    }

    private void setUpFragments() {
        listFragment = new ListStyleFragmentPost();
    }

    private void setUpFragmentManager() {
        fragmentManager = getFragmentManager();
    }

    private void enterDiscussionDetailFragment() {
        transaction = fragmentManager.beginTransaction();
//        Fragment newFragment = new DiscussDetailPageFragment();
//        transaction.replace(R.id.discuss_fragment_container,
//                newFragment).commit();
    }

    @Subscribe
    public void enterDiscussion(EnterDiscussionDetail event) {
        postDetailId = event.id;
        enterDiscussionDetailFragment();
    }

}
