package com.perajuritTeknologi.bizbangkit.event;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class GetWalletBalance {
    public final DataStructure.EWalletBalance details;

    public GetWalletBalance(DataStructure.EWalletBalance details) {
        this.details = details;
    }
}
