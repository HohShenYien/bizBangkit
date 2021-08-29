package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class GetPersonalBusinessDetails {
    public final DataStructure.BusinessProfileDetails details;

    public GetPersonalBusinessDetails(DataStructure.BusinessProfileDetails details) {
        this.details = details;
    }
}
