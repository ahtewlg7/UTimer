package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.mvp.NoteContextSaveMvpP;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.annotations.NonNull;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service
        implements NoteContextSaveMvpP.INoteSaveMvpV{
    public static final String TAG = BinderService.class.getSimpleName();

    private NoteContextSaveMvpP noteSaveMvpP;

    @Override
    public void onCreate() {
        super.onCreate();
        Logcat.i(TAG, "onCreate");

        noteSaveMvpP = new NoteContextSaveMvpP(this);
    }

    public void toSaveNote(@NonNull INoteEntity noteEntity){
        noteSaveMvpP.toSaveNoteContext(noteEntity);
    }
    //+++++++++++++++++++++++++++++++++++++++INoteSaveMvpV++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void onNoteSaveFailed(INoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveFailed");
    }

    @Override
    public void onNoteSaveSucc(INoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveSucc : " + noteEntity.toString());
    }

    @Override
    public void onNoteSaveErr(INoteEntity noteEntity, Throwable e) {
        Logcat.i(TAG,"onNoteSaveErr : " + e.getMessage());
    }

    @Override
    public void onNoteSaveComplete(INoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveComplete");
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public IBinder onBind(Intent arg0) {
        return new BaseServiceBinder();
    }

    public class BaseServiceBinder extends Binder {
        public BinderService getService() {
            return BinderService.this;
        }
    }

}
