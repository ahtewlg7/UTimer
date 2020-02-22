package ahtewlg7.utimer.factory;

import android.graphics.Bitmap;
import android.text.TextUtils;

public class ThumbnailFactory extends ABaseBiMapFactory<String, Bitmap> {
    private ThumbnailFactory(){
    }

    public static ThumbnailFactory getInstance(){
        return Builder.INSTANCE;
    }

    @Override
    public boolean ifKeyValid(String s) {
        return !TextUtils.isEmpty(s);
    }

    @Override
    public boolean ifValueValid(Bitmap bitmap) {
        return bitmap != null;
    }

    private static class Builder{
        private static final ThumbnailFactory INSTANCE = new ThumbnailFactory();
    }
}
