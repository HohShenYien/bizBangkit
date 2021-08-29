package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessListEvent;
import com.perajuritTeknologi.bizbangkit.event.ImageEvent;
import com.perajuritTeknologi.bizbangkit.page.DiscoverPage;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


public class ListStyleFragment extends Fragment {
    private View root;
    private RecyclerView listView;
    private BusinessListAdapter businessListAdapter;
    private ArrayList<DataStructure.SimpleBusiness> businesses;
    private boolean endOfList; // set to true when arraylist returned is smaller than size expected
    private int imgInd; // to get the images for the data

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_listlayout, container, false);

        businesses = new ArrayList<>();
        endOfList = false;
        imgInd = 0;

        setUpComponents();
        setUpList();
        getNewContent();

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
        listView = root.findViewById(R.id.discover_lists);
        businessListAdapter = new BusinessListAdapter(this);
    }

    private void setUpList() {
        listView.setAdapter(businessListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        listView.setLayoutManager(linearLayoutManager);
    }

    public void getNewContent() {
        if (!endOfList) {
            APICaller.getBusinessList(businesses.size(), 50);
        }
    }

    private void addContentsToAdapter(ArrayList<DataStructure.SimpleBusiness> newBusinesses) {
        businesses.addAll(newBusinesses);
        if (imgInd == businesses.size() - newBusinesses.size()) {
            getBusinessLogo();
        }
        businessListAdapter.setItems(businesses);
        businessListAdapter.notifyDataSetChanged();
        if (newBusinesses.size() < 20) {
            endOfList = true;
        }
    }

    @Subscribe
    public void handleNewContents(GetBusinessListEvent event) {
        addContentsToAdapter(event.businesses);
    }

    @Subscribe
    public void handleLogos(ImageEvent event) {
        if (event.event_id.compareTo("ListLogos") == 0) {
            businesses.get(imgInd).logo = event.image.image;
            businessListAdapter.setItems(businesses);
            businessListAdapter.notifyDataSetChanged();
            imgInd++;
            getBusinessLogo();
        }
    }

    private void getBusinessLogo() {
        while (imgInd < businesses.size()) {
            if (businesses.get(imgInd).logoPath.startsWith("./files/")) {
                APICaller.getImg(businesses.get(imgInd).logoPath, imgInd + "a", "ListLogos");
                break;
            }
            imgInd++;
        }
    }
}
