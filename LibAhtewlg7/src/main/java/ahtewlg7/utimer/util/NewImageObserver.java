package ahtewlg7.utimer.util;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

import ahtewlg7.utimer.entity.material.IMediaInfo;

public class NewImageObserver extends AMediaObserver {

    public NewImageObserver(){
        super();
    }

    @NonNull
    @Override
    protected Uri getRegisterUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @NonNull
    @Override
    protected Cursor toQueryCursor() {
        return Utils.getApp().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "_id DESC LIMIT 1");
    }

    @Override
    protected IMediaInfo toParseCursor(Cursor c) {
        return mediaCursorFactory.getMusicInfo(c);
    }
}
