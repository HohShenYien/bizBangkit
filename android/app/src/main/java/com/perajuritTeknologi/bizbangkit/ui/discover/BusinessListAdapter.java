package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ViewHolder>{
    private ArrayList<DataStructure.SimpleBusiness> businesses;

    public BusinessListAdapter(ArrayList<DataStructure.SimpleBusiness> inputList) {
        businesses = inputList;
    }

    @NonNull
    @Override
    public BusinessListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_list_layout, parent, false);
        return new BusinessListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessListAdapter.ViewHolder holder, int position) {
        holder.containerView.setTag(businesses.get(position));
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout containerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerView = itemView.findViewById(R.id.discover_list_layout);

            containerView.setOnClickListener(v -> {
                DataStructure.SimpleBusiness this_business = (DataStructure.SimpleBusiness) containerView.getTag();
                EventBus.getDefault().post(new EnterBusinessDetail(this_business.businessId));
            });
        }
    }
}
