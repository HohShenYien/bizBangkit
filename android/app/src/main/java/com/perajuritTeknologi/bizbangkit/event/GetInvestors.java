package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

import java.util.ArrayList;

public class GetInvestors {
    public final ArrayList<DataStructure.Investor> investors;

    public GetInvestors(ArrayList<DataStructure.Investor> investors) {
        this.investors = investors;
    }
}
