package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class BusinessOwnerEvent {
    public DataStructure.UserProfileDetails profile;

    public BusinessOwnerEvent(DataStructure.UserProfileDetails profile) {
        this.profile = profile;
    }
}
