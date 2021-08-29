package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.EnterBusinessDetail;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class BusinessListAdapter extends RecyclerView.Adapter<BusinessListAdapter.ViewHolder>{
    private ArrayList<DataStructure.SimpleBusiness> businesses;
    private ListStyleFragment parentFragment;

    public BusinessListAdapter(ListStyleFragment parentFragment) {
        this.parentFragment = parentFragment;
        businesses = new ArrayList<>();
    }

    @NonNull
    @Override
    public BusinessListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_list_layout, parent, false);
        return new BusinessListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessListAdapter.ViewHolder holder, int position) {
        if (position < businesses.size() - 20) {
            parentFragment.getNewContent();
        }
        holder.containerView.setTag(businesses.get(position));

        DataStructure.SimpleBusiness business = businesses.get(position);
        ImageView logo = holder.containerView.findViewById(R.id.discover_business_list_logo);
        TextView title = holder.containerView.findViewById(R.id.discover_business_title);
        TextView phase = holder.containerView.findViewById(R.id.discover_business_list_phase);
        ProgressBar progressBar = holder.containerView.findViewById(R.id.discover_business_list_progress);
        TextView percent = holder.containerView.findViewById(R.id.discover_business_list_percent);

        int totalNeededInThisPhase = (int) (business.phase == 1 ? 0.08 * business.valuation :
                0.4 * business.valuation);
        int gathered = (int) (business.purchasedPercent / 100f * business.valuation);
        int progress = gathered * 100 / totalNeededInThisPhase;

        title.setText(business.businessName);
        if (business.logo == null) {
            logo.setImageResource(R.drawable.default_business_logo);
        } else {
            logo.setImageBitmap(business.logo);
        }
        phase.setText("Phase " + business.phase + ": RM" + gathered + " / RM" + totalNeededInThisPhase);
        progressBar.setProgress(progress);
        percent.setText(progress + "%");
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

    public void setItems(ArrayList<DataStructure.SimpleBusiness> businesses) {
        this.businesses = businesses;
    }
}
