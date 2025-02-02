package ahtewlg7.utimer.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;

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

    public static String toBase64String(Bitmap bmp){
        byte[] tmp = toByteArry(bmp);
        if(tmp == null)
            return null;
        return  Base64.encodeToString(tmp, Base64.DEFAULT);
    }

    public static Bitmap toBitmap(byte[] bytes){
        if(bytes == null)
            return null;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    public static Bitmap toBitmap(String base64){
        if(TextUtils.isEmpty(base64))
            return null;
        return toBitmap(Base64.decode(base64, Base64.DEFAULT));
    }
}
