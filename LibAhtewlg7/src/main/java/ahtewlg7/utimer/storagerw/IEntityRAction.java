package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/1/9.
 */

public interface IEntityRAction {
    public Flowable<NoteEntity> loadNoteEntity();
    public Flowable<NoteEntity> getNoteEntity(@NonNull Flowable<String> idObservable);

    public Flowable<AGtdEntity> loadGtdEntity();
    public Flowable<AGtdEntity> getGtdEntity(@NonNull Flowable<String> idObservable);


}
