package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class GetBusinessDetails {
    public final DataStructure.BusinessProfileDetails details;

    public GetBusinessDetails(DataStructure.BusinessProfileDetails details) {
        this.details = details;
    }
}
