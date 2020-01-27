package ahtewlg7.utimer.db.media;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

import ahtewlg7.utimer.entity.material.MediaInfo;
import io.reactivex.subjects.PublishSubject;

public abstract class AMediaObserver extends ContentObserver  {
    protected abstract @NonNull Uri getRegisterUri();
    protected abstract Cursor toQueryCursor();
    protected abstract MediaInfo toParseCursor(Cursor c);

    protected PublishSubject<MediaInfo> publishSubject;
    protected MediaCursorParser mediaCursorParser;

    public AMediaObserver(){
        super(new Handler());
        publishSubject    = PublishSubject.create();
        mediaCursorParser = new MediaCursorParser();
        Utils.getApp().getContentResolver().registerContentObserver(getRegisterUri(), true, this);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Cursor c = toQueryCursor();
        if(c == null)
            return;
        if (c.moveToNext()){
            MediaInfo iMediaInfo = toParseCursor(c);
            publishSubject.onNext(iMediaInfo);
        }
        c.close();
    }

    public PublishSubject<MediaInfo> toListenerMediaChange(){
        return publishSubject;
    }
}
