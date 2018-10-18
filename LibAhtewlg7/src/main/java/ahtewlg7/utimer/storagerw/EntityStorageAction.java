package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.NoteEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/1/14.
 */

public class EntityStorageAction implements IEntityWAction, IEntityRAction{
    public static final String TAG = EntityStorageAction.class.getSimpleName();

    private String workingPath;

    public EntityStorageAction(){
        workingPath = new FileSystemAction().getNoteDocAbsPath();
    }

    @Override
    public Flowable<Optional<NoteEntity>> loadNoteEntity() {
        return null;
    }

    @Override
    public Flowable<Optional<NoteEntity>> getNoteEntity(@NonNull Flowable<String> idObservable) {
        return null;
    }

    @Override
    public boolean saveEntity(NoteEntity entity) {
        return false;
    }
}
