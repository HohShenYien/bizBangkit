package com.perajuritTeknologi.bizbangkit.ui.profiles;

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

public class InvestedAdapter extends RecyclerView.Adapter<InvestedAdapter.ViewHolder>{
    private ArrayList<DataStructure.SimpleBusiness> businesses;
    private ProfileDetailsFragment parentFragment;

    public InvestedAdapter(ProfileDetailsFragment parentFragment) {
        this.parentFragment = parentFragment;
        businesses = new ArrayList<>();
    }

    @NonNull
    @Override
    public InvestedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_list_layout, parent, false);
        return new InvestedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InvestedAdapter.ViewHolder holder, int position) {
        holder.containerView.setTag(businesses.get(position));

        DataStructure.SimpleBusiness business = businesses.get(position);
        ImageView logo = holder.containerView.findViewById(R.id.discover_business_list_logo);
        TextView title = holder.containerView.findViewById(R.id.discover_business_title);
        TextView phase = holder.containerView.findViewById(R.id.discover_business_list_phase);
        ProgressBar progressBar = holder.containerView.findViewById(R.id.discover_business_list_progress);
        TextView percent = holder.containerView.findViewById(R.id.discover_business_list_percent);
        TextView invested = holder.containerView.findViewById(R.id.invested_amount);

        int totalNeededInThisPhase = (int) (business.phase == 1 ? 0.08 * business.valuation :
                0.4 * business.valuation);
        int gathered = (int) (business.purchasedPercent / 100f * business.valuation);
        int progress = gathered * 100 / totalNeededInThisPhase;
        int investedAmount = business.invested * business.valuation / 100;

        title.setText(business.businessName);
        if (business.logo == null) {
            logo.setImageResource(R.drawable.default_business_logo);
        } else {
            logo.setImageBitmap(business.logo);
        }
        phase.setText("Phase " + business.phase + ": RM" + gathered + " / RM" + totalNeededInThisPhase);
        progressBar.setProgress(progress);
        percent.setText(progress + "%");
        invested.setText("Invested: RM" + investedAmount);
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
        }
    }

    public void setItems(ArrayList<DataStructure.SimpleBusiness> businesses) {
        this.businesses = businesses;
    }
}
