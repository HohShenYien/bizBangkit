package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class ProfileEvent {
    public DataStructure.UserProfileDetails profile;

    public ProfileEvent(DataStructure.UserProfileDetails profile) {
        this.profile = profile;
    }
}
