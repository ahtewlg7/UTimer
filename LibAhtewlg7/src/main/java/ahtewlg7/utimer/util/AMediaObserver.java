package ahtewlg7.utimer.util;

import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;

import ahtewlg7.utimer.entity.material.IMediaInfo;
import ahtewlg7.utimer.factory.MediaCursorFactory;
import io.reactivex.subjects.PublishSubject;

public abstract class AMediaObserver extends ContentObserver  {
    protected abstract @NonNull Uri getRegisterUri();
    protected abstract @NonNull Cursor toQueryCursor();
    protected abstract IMediaInfo toParseCursor(Cursor c);

    protected PublishSubject<IMediaInfo> publishSubject;
    protected MediaCursorFactory mediaCursorFactory;

    public AMediaObserver(){
        super(new Handler());
        publishSubject      = PublishSubject.create();
        mediaCursorFactory  = new MediaCursorFactory();
        Utils.getApp().getContentResolver().registerContentObserver(getRegisterUri(), true, this);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        Cursor c = toQueryCursor();
        if (c.moveToNext()){
            IMediaInfo iMediaInfo = toParseCursor(c);
            publishSubject.onNext(iMediaInfo);
        }
        c.close();
    }

    public PublishSubject<IMediaInfo> toListenerMediaChange(){
        return publishSubject;
    }
}
