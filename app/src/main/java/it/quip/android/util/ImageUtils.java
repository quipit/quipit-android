package it.quip.android.util;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class ImageUtils {

    private static final int SOME_COMPRESSION = 90;

    public static byte[] getBytes(Bitmap b) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        b.compress(Bitmap.CompressFormat.JPEG, SOME_COMPRESSION, stream);
        return stream.toByteArray();
    }

}
