package com.perajuritTeknologi.bizbangkit.page;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.TooltipCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.ui.discover.CardStyleFragment;
import com.perajuritTeknologi.bizbangkit.ui.discover.ListStyleFragment;

public class DiscoverPage extends Fragment {
    private View root;
    private MaterialButton filterBtn;
    private FragmentContainerView fragmentContainer;
    private ImageButton changeViewBtn;
    private boolean isCardView;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_discoverpage, container, false);
        isCardView = true;

        setUpComponents();
        setUpBtnHint();
        setUpFragmentManager();

        setDefaultFragment();
        setUpFragmentChangeButton();

        return root;
    }

    private void setUpComponents() {
        filterBtn = root.findViewById(R.id.discover_filter_button);
        changeViewBtn = root.findViewById(R.id.changeViewButton);
        fragmentContainer = root.findViewById(R.id.discover_fragment_container);
    }

    private void setUpBtnHint() {
        TooltipCompat.setTooltipText(filterBtn, "Filter businesses");
        TooltipCompat.setTooltipText(changeViewBtn, "Change view");
    }

    private void setDefaultFragment() {
        Fragment cardFragment = new CardStyleFragment();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.discover_fragment_container,
                cardFragment).commit();
        changeViewBtn.setImageResource(R.drawable.list_ic);
    }

    private void setUpFragmentManager() {
        fragmentManager = getFragmentManager();
    }

    private void setUpFragmentChangeButton() {
        changeViewBtn.setOnClickListener(v -> {
            transaction = fragmentManager.beginTransaction();
            Fragment newFragment;
            if (isCardView) {
                newFragment = new ListStyleFragment();
                changeViewBtn.setImageResource(R.drawable.ic_red_card);
            } else {
                newFragment = new CardStyleFragment();
                changeViewBtn.setImageResource(R.drawable.list_ic);
            }
            isCardView = !isCardView;
            transaction.replace(R.id.discover_fragment_container,
                    newFragment).commit();
        });
    }


}
