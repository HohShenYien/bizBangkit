package com.perajuritTeknologi.bizbangkit.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.page.BusinessPage;
import com.perajuritTeknologi.bizbangkit.page.DiscoverPage;
import com.perajuritTeknologi.bizbangkit.page.DiscussPage;
import com.perajuritTeknologi.bizbangkit.page.HomePage;
import com.perajuritTeknologi.bizbangkit.page.ProfilePage;

public class HomeFragment extends Fragment {
    private View root;
    private BottomNavigationView bottomNavBar;
    private FragmentTransaction transaction;
    private FragmentActivity fragmentActivity;
    private FragmentManager fragmentManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_home, container, false);
        setUpBottomNavBar();
        setUpFragmentManager();
        setUpFragmentSwitch();
        setDefaultFragment();
        return root;
    }

    private void setUpBottomNavBar() {
        bottomNavBar = root.findViewById(R.id.bottom_navigation);
        bottomNavBar.setSelectedItemId(R.id.bot_nav_home);
    }

    private void setDefaultFragment() {
        Fragment homePage = new HomePage();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container,
                homePage).commit();
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
                    break;
                case R.id.bot_nav_discover:
                    selectedFragment = new DiscoverPage();
                    break;
                case R.id.bot_nav_discuss:
                    selectedFragment = new DiscussPage();
                    break;
                case R.id.bot_nav_home:
                    selectedFragment = new HomePage();
                    break;
                case R.id.bot_nav_profile:
                    selectedFragment = new ProfilePage();
                    break;
                default:
                    selectedFragment = new HomePage();
            }
            transaction.replace(R.id.fragment_container,
                    selectedFragment).commit();
            return true;
        });
    }

    private void setUpFragmentManager() {
        fragmentManager = getFragmentManager();
    }
}