package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import io.reactivex.Observable;

/**
 * Created by lw on 2018/1/14.
 */

public class EntityStorageAction implements IEntityWAction, IEntityRAction{
    public static final String TAG = EntityStorageAction.class.getSimpleName();

    private String workingPath;

    public EntityStorageAction(){
        workingPath = new FileSystemAction().getDocNoteAbsPath();
    }

    @Override
    public Observable<NoteEntity> loadNoteEntity() {
        return null;
    }

    @Override
    public Observable<NoteEntity> getNoteEntity(@NonNull Observable<String> idObservable) {
        return null;
    }

    @Override
    public Observable<AGtdEntity> loadGtdEntity() {
        return null;
    }

    @Override
    public Observable<AGtdEntity> getGtdEntity(@NonNull Observable<String> idObservable) {
        return null;
    }

    @Override
    public boolean saveEntity(NoteEntity entity) {
        return false;
    }

    @Override
    public boolean saveEntity(AGtdEntity entity) {
        return false;
    }
}
