package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.ImageEvent;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class BusinessDetailInvestors extends Fragment {

    private View root;
    private ListView investorList;
    private DataStructure.BusinessProfileDetails business;
    private ArrayList<DataStructure.Investor> investors;
    private InvestorAdapter adapter;
    private int imgId; // id of the investor images
    private TextView info;

    public BusinessDetailInvestors (ArrayList<DataStructure.Investor> investors,
                                    DataStructure.BusinessProfileDetails business) {
        this.business = business;
        this.investors = investors;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_business_detail_investors, container, false);
        imgId = 0;

        getComponents();
        setUpAdapter();
        setUpList();
        getUserImg();

        return root;
    }

    private void getComponents() {
        investorList = root.findViewById(R.id.discover_details_investor_list);
        info = root.findViewById(R.id.discover_business_details_info);
    }

    private void setUpList() {
        investorList.setAdapter(adapter);
        if (investors.size() == 0) {
            info.setText("No investor yet...");
        } else {
            info.setVisibility(View.GONE);
        }
    }

    private void setUpAdapter() {
        adapter = new InvestorAdapter(investors, root.getContext(), business);
    }

    private void getUserImg() {
        while (imgId < investors.size() && investors.get(imgId).userPicPath == null) {
            imgId++;
        }
        if (imgId < investors.size()) {
            APICaller.getImg(investors.get(imgId).userPicPath, Integer.toString(imgId), "Discover-investors");
        }
    }

    private void updateUserImg(Bitmap img, int ind) {
        investors.get(ind).userPic = img;
        adapter.resetList(investors);
        adapter.notifyDataSetChanged();

        imgId++;
        getUserImg();
    }

    @Subscribe
    public void receiveUserImg(ImageEvent event) {
        if (event.event_id.compareTo("Discover-investors") == 0) {
            updateUserImg(event.image.image, Integer.parseInt(event.image.image_id));
        }
    }
}
