package com.perajuritTeknologi.bizbangkit.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.TabChanged;
import com.perajuritTeknologi.bizbangkit.page.BusinessPage;
import com.perajuritTeknologi.bizbangkit.page.DiscoverPage;
import com.perajuritTeknologi.bizbangkit.page.DiscussPage;
import com.perajuritTeknologi.bizbangkit.page.HomePage;
import com.perajuritTeknologi.bizbangkit.page.ProfilePage;

import org.greenrobot.eventbus.EventBus;

public class HomeFragment extends Fragment {
    private View root;
    public BottomNavigationView bottomNavBar;
    private FragmentTransaction transaction;
    private FragmentActivity fragmentActivity;
    private FragmentManager fragmentManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        setUpBottomNavBar();
        setUpFragmentManager();
        setUpFragmentSwitch();
        setUpSelfInMainActivitiy();

        if (((MainActivity)getActivity()).userProfile != null &&
                MainActivity.businessDetails != null &&
                MainActivity.eWalletBalance != null
        ) {
            setDefaultFragment();
        }

        return root;
    }

    public void changeFragment(Fragment newFragment, boolean allowBack) {
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, newFragment);
        if (allowBack) {
            transaction.addToBackStack(newFragment.toString());
        }
        transaction.commit();
    }

    private void setUpBottomNavBar() {
        bottomNavBar = root.findViewById(R.id.bottom_navigation);
        bottomNavBar.setSelectedItemId(R.id.bot_nav_home);
        bottomNavBar.setVisibility(View.GONE);
    }

    public void setDefaultFragment() {
        Fragment homePage = new HomePage();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,
                homePage).commit();
        bottomNavBar.setVisibility(View.VISIBLE);
    }

    private void setUpFragmentSwitch() {
        bottomNavBar.setOnItemSelectedListener(item -> {
            item.setChecked(true);
            Fragment selectedFragment = null;
            transaction = fragmentManager.beginTransaction();
            // set the fragments for corresponding items clicked
            switch (item.getItemId()) {
                case R.id.bot_nav_business:
                    selectedFragment = new BusinessPage();
                    EventBus.getDefault().post(new TabChanged(getString(R.string.bot_nav_business)));
                    break;
                case R.id.bot_nav_discover:
                    selectedFragment = new DiscoverPage();
                    EventBus.getDefault().post(new TabChanged(getString(R.string.bot_nav_discover)));
                    break;
                case R.id.bot_nav_discuss:
                    selectedFragment = new DiscussPage();
                    EventBus.getDefault().post(new TabChanged(getString(R.string.bot_nav_discuss)));
                    break;
                case R.id.bot_nav_home:
                    selectedFragment = new HomePage();
                    EventBus.getDefault().post(new TabChanged(getString(R.string.bot_nav_home)));
                    break;
                case R.id.bot_nav_profile:
                    selectedFragment = new ProfilePage();
                    EventBus.getDefault().post(new TabChanged(getString(R.string.bot_nav_profile)));
                    break;
                default:
                    selectedFragment = new HomePage();
                    EventBus.getDefault().post(new TabChanged(getString(R.string.bot_nav_home)));
            }
            changeFragment(selectedFragment, false);
            return true;
        });
    }

    private void setUpFragmentManager() {
        fragmentManager = getFragmentManager();
    }

    private void setUpSelfInMainActivitiy() {
        ((MainActivity)getActivity()).setHomeFragment(this);
    }
}