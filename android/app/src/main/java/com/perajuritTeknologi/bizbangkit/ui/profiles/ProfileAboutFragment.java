package com.perajuritTeknologi.bizbangkit.ui.profiles;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.ScrollingView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;

import org.greenrobot.eventbus.EventBus;

public class ProfileAboutFragment extends Fragment {
    private View root;
    private NestedScrollView scrollingView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_profile_general, container, false);
        setUpScrollView();
        return root;
    }

    private void setUpScrollView() {
        scrollingView = root.findViewById(R.id.profile_scroll);
        scrollingView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                EventBus.getDefault().post(new ProfileScrolled(Math.min(scrollY, 300)));
            }
        });
    }
}
