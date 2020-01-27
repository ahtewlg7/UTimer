package ahtewlg7.utimer.db.media;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.material.MediaInfo;

public class NewImageObserver extends AMediaObserver {
    private DateTime preQueryTime;

    public NewImageObserver(){
        super();
        preQueryTime        = DateTime.now();
    }

    public void setPreQueryTime(DateTime preQueryTime){
        this.preQueryTime = preQueryTime;
    }

    @NonNull
    @Override
    protected Uri getRegisterUri() {
        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected Cursor toQueryCursor() {
        return Utils.getApp().getContentResolver()
                .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                        MediaStore.Images.Media.DATE_MODIFIED + ">= ?",  new String[]{String.valueOf(preQueryTime.getMillis()/1000)},
                        MediaStore.Images.Media.DATE_MODIFIED + " DESC");
    }

    @Override
    protected MediaInfo toParseCursor(Cursor c) {
        return mediaCursorParser.getImageMediaInfo(c);
    }
}
