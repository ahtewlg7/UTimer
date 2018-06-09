package ahtewlg7.utimer.common;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.LoadType;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.mvp.NoteRecyclerViewMvpP;
import ahtewlg7.utimer.storagerw.EntityDbAction;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2017/12/28.
 */

public class NoteEntityAction
        implements NoteEditMvpP.INoteEditMvpM, NoteRecyclerViewMvpP.INoteRecyclerViewMvpM {
    public static final String TAG = NoteEntityAction.class.getSimpleName();

    private EntityDbAction dbAction;
    private DateTimeAction dateTimeAction;
    private FileSystemAction fileSystemAction;

    public NoteEntityAction() {
        dbAction            = new EntityDbAction();
        dateTimeAction      = new DateTimeAction();
        fileSystemAction    = new FileSystemAction();
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
    public boolean isMdFileExist(NoteEntity noteEntity){
        String noteFileAbsPath = getMdFileAbsPath(noteEntity);
        boolean isFileExists = FileUtils.isFileExists(noteFileAbsPath);
        Logcat.i(TAG,"isNoteFileExist noteFileAbsPath = " + noteFileAbsPath + ", isFileExists = " + isFileExists);
        return isFileExists;
    }

    public String getMdFileAbsPath(NoteEntity noteEntity){
        String noteFileSuffix = VcFactoryBuilder.getInstance().getVersionControlFactory().getBaseConfig().getNoteFileSuffix();
        return fileSystemAction.getSdcardPath() + noteEntity.getFileRPath()
                + noteEntity.getNoteName() + noteFileSuffix;
    }

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
    public Flowable<Boolean> toSaveNote(NoteEntity noteEntity) {
        return dbAction.saveEntity(Flowable.just(noteEntity).doOnNext(new Consumer<NoteEntity>() {
            @Override
            public void accept(NoteEntity noteEntity) throws Exception {
                noteEntity.setFileAbsPath(getMdFileAbsPath(noteEntity));
            }
        }));
    }
}
