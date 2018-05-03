package ahtewlg7.utimer.common;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.mvp.NoteContextSaveMvpP;
import ahtewlg7.utimer.storagerw.EntityDbAction;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2017/12/28.
 */

public class NoteEntityAction
        implements NoteEditMvpP.INoteEditMvpM, NoteContextSaveMvpP.INoteSaveMvpM {
    public static final String TAG = NoteEntityAction.class.getSimpleName();

    private EntityDbAction dbAction;
    private DateTimeAction dateTimeAction;
    private NoteContextFsAction noteContextFsAction;

    public NoteEntityAction() {
        dbAction            = new EntityDbAction();
        dateTimeAction      = new DateTimeAction();
        noteContextFsAction = new NoteContextFsAction();
    }

    public Flowable<Optional<INoteEntity>> loadEntity(Flowable<String> idObservable) {
        return dbAction.getNoteEntity(idObservable);
    }

    public INoteEntity createNoteEntity(){
        Logcat.i(TAG,"createNoteEntity");
        String id = new IdAction().getNoteId();
        DateTime now = dateTimeAction.toNow();

        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setId(id);
        noteEntity.setNoteName(dateTimeAction.toFormat(now));
        noteEntity.setCreateTime(now);
        noteEntity.setLastAccessTime(now);
        return noteEntity;
    }

    @Override
    public Flowable<Optional<INoteEntity>> toLoadOrCreateNote(String noteId) {
        if(TextUtils.isEmpty(noteId))
            return Flowable.just(Optional.<INoteEntity>absent());
        return loadEntity(Flowable.just(noteId))
                .map(new Function<Optional<INoteEntity>, Optional<INoteEntity>>() {
                    @Override
                    public Optional<INoteEntity> apply(Optional<INoteEntity> iNoteEntityOptional) throws Exception {
                        if(iNoteEntityOptional.isPresent())
                            return Optional.of(createNoteEntity());
                        return iNoteEntityOptional;
                    }
                });
    }

    @Override
    public boolean toReadContext(INoteEntity noteEntity) {
        return noteContextFsAction.readNoteContext(noteEntity);
    }

    @Override
    public void toMdContext(INoteEntity noteEntity) {

    }



    @Override
    public Flowable<Boolean> toSaveNote(INoteEntity noteEntity) {
        return Flowable.just(noteEntity)
                .map(new Function<INoteEntity, Boolean>() {
                    @Override
                    public Boolean apply(INoteEntity noteEntity) throws Exception {
                        boolean result = dbAction.saveEntity(noteEntity);
                        Logcat.i(TAG,"toSaveNote map , result = " + result) ;
                        return result;
                    }
                });
    }

    @Override
    public Flowable<Boolean> toSaveContext(INoteEntity noteEntity) {
        return Flowable.just(noteEntity)
                .map(new Function<INoteEntity, Boolean>() {
                    @Override
                    public Boolean apply(INoteEntity noteEntity) throws Exception {
                        boolean result = noteContextFsAction.writeNoteContext(noteEntity);
                        Logcat.i(TAG,"toSaveContext map , result = " + result) ;
                        return result;
                    }
                });
    }
}