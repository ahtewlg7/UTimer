package ahtewlg7.utimer.common;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.LoadType;
import ahtewlg7.utimer.mvp.NoteContextSaveMvpP;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.mvp.NoteRecyclerViewMvpP;
import ahtewlg7.utimer.storagerw.EntityDbAction;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2017/12/28.
 */

public class NoteEntityAction
        implements NoteEditMvpP.INoteEditMvpM, NoteContextSaveMvpP.INoteContextSaveMvpM
        , NoteRecyclerViewMvpP.INoteRecyclerViewMvpM {
    public static final String TAG = NoteEntityAction.class.getSimpleName();

    private EntityDbAction dbAction;
    private DateTimeAction dateTimeAction;
    private NoteContextFsAction noteContextFsAction;

    public NoteEntityAction() {
        dbAction            = new EntityDbAction();
        dateTimeAction      = new DateTimeAction();
        noteContextFsAction = new NoteContextFsAction();
    }

    public NoteEntity createNoteEntity(){
        Logcat.i(TAG,"createNoteEntity");
        String id = new IdAction().getNoteId();
        DateTime now = dateTimeAction.toNow();

        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setId(id);
        noteEntity.setLoadType(LoadType.NEW);
        noteEntity.setNoteName(dateTimeAction.toFormat(now));
        noteEntity.setCreateTime(now);
        noteEntity.setLastAccessTime(now);
        return noteEntity;
    }

    //=====================================IBaseRecyclerViewMvpM========================================
    @Override
    public Flowable<Optional<NoteEntity>> loadAllEntity() {
        return dbAction.loadAllNoteEntity();
    }

    @Override
    public Flowable<Optional<NoteEntity>> loadEntity(@NonNull Flowable<Optional<String>> idObservable) {
        return dbAction.getNoteEntity(idObservable);
    }

    @Override
    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<NoteEntity>> flowable) {
        return dbAction.deleteNoteEntity(flowable);
    }

    //=====================================INoteEditMvpM========================================
    @Override
    public Flowable<Optional<NoteEntity>> toLoadOrCreateNote(@NonNull Flowable<Optional<String>> noteIdFlowable) {
        return loadEntity(noteIdFlowable)
                .map(new Function<Optional<NoteEntity>, Optional<NoteEntity>>() {
                    @Override
                    public Optional<NoteEntity> apply(Optional<NoteEntity> iNoteEntityOptional) throws Exception {
                        if(!iNoteEntityOptional.isPresent())
                            return Optional.of(createNoteEntity());
                        return iNoteEntityOptional;
                    }
                });
    }

    @Override
    public boolean toReadContext(NoteEntity noteEntity) {
        return noteContextFsAction.readNoteContext(noteEntity);
    }

    @Override
    public void handleTextChange(NoteEntity noteEntity, TextViewTextChangeEvent textViewTextChangeEvent) {
        Logcat.i(TAG, "handleTextChange onNext, before = " + textViewTextChangeEvent.before() +
                ", start = " + textViewTextChangeEvent.start() + ", text = " + textViewTextChangeEvent.text() +
                ", count = " + textViewTextChangeEvent.count());
        String context = textViewTextChangeEvent.text().toString();
        noteEntity.setRawContext(context);
        if(!TextUtils.isEmpty(context))//todo : check context if change
            noteEntity.setContextChanged(true);
        //todo : mdContext
    }

    @Override
    public Flowable<Boolean> toSaveNote(NoteEntity noteEntity) {
        return dbAction.saveEntity(Flowable.just(noteEntity).doOnNext(new Consumer<NoteEntity>() {
            @Override
            public void accept(NoteEntity noteEntity) throws Exception {
                noteEntity.setFileAbsPath(noteContextFsAction.getNoteFileAbsPath(noteEntity));
            }
        }));
    }

    //==========================================INoteContextSaveMvpM============================================
    @Override
    public Flowable<Boolean> toSaveContext(NoteEntity noteEntity) {
        return Flowable.just(noteEntity)
                .map(new Function<NoteEntity, Boolean>() {
                    @Override
                    public Boolean apply(NoteEntity noteEntity) throws Exception {
                        boolean result = noteContextFsAction.writeNoteContext(noteEntity);
                        Logcat.i(TAG,"toSaveContext map , result = " + result + ", noteEntity = " + noteEntity.toString()) ;
                        return result;
                    }
                });
    }

    @Override
    public Flowable<Boolean> toDeleteContext(String filePath) {
        return Flowable.just(filePath)
                .subscribeOn(Schedulers.io())
                .map(new Function<String, Boolean>() {
                    @Override
                    public Boolean apply(String s) throws Exception {
                        boolean result = noteContextFsAction.deleteNoteContext(s);
                        Logcat.i(TAG,"toDeleteContext map , result = " + result) ;
                        return result;
                    }
                });
    }
}
