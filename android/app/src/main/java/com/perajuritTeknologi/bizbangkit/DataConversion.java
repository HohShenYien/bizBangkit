package com.perajuritTeknologi.bizbangkit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.IOException;

public class DataConversion {
    // for changing special data types mostly to string to be stored in database

    public static String BitmapToBase64String(Bitmap bitmap) {
        ByteArrayOutputStream baos = new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap Base64StringToBitmap(String string) {
        byte[] encodeByte = Base64.decode(string, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        return bitmap;
    }

    // correcting rotations of the image
    public static Matrix getBitmapRotation(FileDescriptor fileDescriptor) {
        int degree = 0;
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(fileDescriptor);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);
        if (orientation != -1) {
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) degree = 90;
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) degree = 180;
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) degree = 270;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return matrix;
    }

}
