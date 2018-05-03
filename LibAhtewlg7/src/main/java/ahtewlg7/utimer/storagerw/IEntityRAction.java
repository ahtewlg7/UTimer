package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/1/9.
 */

public interface IEntityRAction {
    public Flowable<Optional<INoteEntity>> loadNoteEntity();
    public Flowable<Optional<INoteEntity>> getNoteEntity(@NonNull Flowable<String> idObservable);

    public Flowable<AGtdEntity> loadGtdEntity();
    public Flowable<AGtdEntity> getGtdEntity(@NonNull Flowable<String> idObservable);


}
