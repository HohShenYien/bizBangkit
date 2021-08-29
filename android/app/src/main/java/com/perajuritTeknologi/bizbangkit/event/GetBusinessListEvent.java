package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

import java.util.ArrayList;

public class GetBusinessListEvent {
    public final ArrayList<DataStructure.SimpleBusiness> businesses;

    public GetBusinessListEvent(ArrayList<DataStructure.SimpleBusiness> results) {
        this.businesses = results;
    }
}
