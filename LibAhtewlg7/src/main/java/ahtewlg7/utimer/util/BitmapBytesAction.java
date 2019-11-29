package ahtewlg7.utimer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;

public class BitmapBytesAction {

    public static byte[] toByteArry(Bitmap bmp){
        if(bmp == null)
            return null;
        int bytes = bmp.getByteCount();
        ByteBuffer buf = ByteBuffer.allocate(bytes);
        bmp.copyPixelsToBuffer(buf);
        return buf.array();
    }

    public static Bitmap toBitmap(byte[] bytes){
        if(bytes == null)
            return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
