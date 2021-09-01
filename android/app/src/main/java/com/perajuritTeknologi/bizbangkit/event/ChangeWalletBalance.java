package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class ChangeWalletBalance {
    public final DataStructure.EWalletBalance details;

    public ChangeWalletBalance(DataStructure.EWalletBalance details) {
        this.details = details;
    }
}
