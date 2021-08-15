package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.perajuritTeknologi.bizbangkit.R;

import net.cachapa.expandablelayout.ExpandableLayout;

public class BusinessDetailFinance extends Fragment {
    private View root;
    private ExpandableLayout costLayout, revenueLayout, incomeLayout;
    private LinearLayout costTab, revenueTab, incomeTab;
    private ImageView arrowCost, arrowRevenue, arrowIncome;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover_businesss_detail_finance, container, false);

        setUpComponents();
        collapseAll();
        setUpClickToExpandTabs();

        return root;
    }

    private void setUpComponents() {
        costLayout = root.findViewById(R.id.expandable_layout_cost);
        revenueLayout = root.findViewById(R.id.expandable_layout_revenue);
        incomeLayout = root.findViewById(R.id.expandable_layout_income);

        costTab = root.findViewById(R.id.business_detail_cost_tab);
        revenueTab = root.findViewById(R.id.business_detail_revenue_tab);
        incomeTab = root.findViewById(R.id.business_detail_income_tab);

        arrowCost = root.findViewById(R.id.business_detail_finance_chevron1);
        arrowRevenue = root.findViewById(R.id.business_detail_finance_chevron2);
        arrowIncome = root.findViewById(R.id.business_detail_finance_chevron3);
    }

    private void setUpClickToExpandTabs() {
        costTab.setOnClickListener(v -> {
            if (costLayout.isExpanded()) {
                arrowCost.setImageResource(R.drawable.arrow_down_ic);
                costLayout.collapse();
            } else {
                arrowCost.setImageResource(R.drawable.arrow_up_ic);
                costLayout.expand();
            }

        });
        revenueTab.setOnClickListener(v -> {
            if (revenueLayout.isExpanded()) {
                arrowRevenue.setImageResource(R.drawable.arrow_down_ic);
                revenueLayout.collapse();
            } else {
                arrowRevenue.setImageResource(R.drawable.arrow_up_ic);
                revenueLayout.expand();
            }
        });
        incomeTab.setOnClickListener(v -> {
            if (incomeLayout.isExpanded()) {
                arrowIncome.setImageResource(R.drawable.arrow_down_ic);
                incomeLayout.collapse();
            } else {
                arrowIncome.setImageResource(R.drawable.arrow_up_ic);
                incomeLayout.expand();
            }
        });
    }

    private void collapseAll() {
        costLayout.collapse();
        revenueLayout.collapse();
        incomeLayout.collapse();
    }

}
