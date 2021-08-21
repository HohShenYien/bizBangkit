package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class LogInEvent {
    public final DataStructure.UserCredentials credentials;

    public LogInEvent(DataStructure.UserCredentials credential) {
        credentials = credential;
    }
}
