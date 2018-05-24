package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/1/9.
 */

public interface IEntityRAction {
    public Flowable<Optional<NoteEntity>> loadNoteEntity();
    public Flowable<Optional<NoteEntity>> getNoteEntity(@NonNull Flowable<String> idObservable);

    public Flowable<AGtdEntity> loadGtdEntity();
    public Flowable<AGtdEntity> getGtdEntity(@NonNull Flowable<String> idObservable);


}
