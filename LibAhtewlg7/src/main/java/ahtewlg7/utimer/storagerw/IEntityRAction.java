package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by lw on 2018/1/9.
 */

public interface IEntityRAction {
    public Flowable<NoteEntity> loadNoteEntity();
    public Observable<NoteEntity> getNoteEntity(@NonNull Observable<String> idObservable);

    public Flowable<AGtdEntity> loadGtdEntity();
    public Observable<AGtdEntity> getGtdEntity(@NonNull Observable<String> idObservable);


}
