package com.perajuritTeknologi.bizbangkit;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.perajuritTeknologi.bizbangkit.event.ImageEvent;
import com.perajuritTeknologi.bizbangkit.event.ProfileEvent;
import com.perajuritTeknologi.bizbangkit.event.TabChanged;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity {
    public DataStructure.UserProfileDetails userProfile;
    public Bitmap userImg;
    private AppBarConfiguration mAppBarConfiguration;
    private View sideNavHeader;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        setUpToolBar();
        setUpNavigation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void setUpToolBar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpNavigation() {
        DrawerLayout drawer = findViewById(R.id.my_drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        sideNavHeader = navigationView.getHeaderView(0);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_settings, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void setUserDetail() {
        TextView sideNavTitle = sideNavHeader.findViewById(R.id.side_nav_title);
        sideNavTitle.setText(userProfile.username);
        TextView sideNavEmail = sideNavTitle.findViewById(R.id.sideNavUserEmail);
        sideNavEmail.setText(userProfile.email);
    }

    private void setUpUserImg() {
        ImageView sideNavUserImg = sideNavHeader.findViewById(R.id.sideNavUserImage);
        sideNavUserImg.setImageBitmap(userImg);
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_yes_or_no);
        dialog.setCancelable(true);
        TextView exitConfirmationText = dialog.findViewById(R.id.dialogYesOrNoText);
        exitConfirmationText.setText("Are you sure you want to exit BizBangkit?");
        Button exitConfirmationYesButton = dialog.findViewById(R.id.dialogYesButton);
        exitConfirmationYesButton.setOnClickListener(view -> {
            super.onBackPressed();
        });
        Button exitConfirmationNoButton = dialog.findViewById(R.id.dialogNoButton);
        exitConfirmationNoButton.setOnClickListener(view -> {
            dialog.cancel();
        });
        dialog.show();
    }

    // events
    @Subscribe
    public void onMessageEvent(TabChanged event) {
        toolbar.setTitle(event.message);
    }

    @Subscribe
    public void onProfileEvent(ProfileEvent event) {
        this.userProfile = event.profile;
        APICaller.getImg(userProfile.profilePicture, "user", "main-activity");
        setUserDetail();
    }

    @Subscribe
    public void onUserImageEvent(ImageEvent event) {
        if (event.event_id.compareTo("main-activity") == 0) {
            this.userImg = event.image.image;
            setUpUserImg();
        }
    }
}
