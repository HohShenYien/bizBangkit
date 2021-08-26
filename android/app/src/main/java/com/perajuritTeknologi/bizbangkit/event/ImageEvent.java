package com.perajuritTeknologi.bizbangkit.event;

import android.graphics.Bitmap;

import com.perajuritTeknologi.bizbangkit.DataStructure;

public class ImageEvent {
    public final DataStructure.ImageAndId image;
    public final String event_id;

    public ImageEvent(DataStructure.ImageAndId image, String event_id) {
        this.image = image;
        this.event_id = event_id;
    }
}
