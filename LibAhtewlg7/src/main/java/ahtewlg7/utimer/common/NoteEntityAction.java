package ahtewlg7.utimer.common;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.enumtype.UnLoadType;
import ahtewlg7.utimer.mvp.NoteEntityEditMvpP;
import ahtewlg7.utimer.mvp.NoteRecyclerViewMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2017/12/28.
 */

public class NoteEntityAction
        implements NoteEntityEditMvpP.INoteEditMvpM, NoteRecyclerViewMvpP.INoteRecyclerViewMvpM {
    public static final String TAG = NoteEntityAction.class.getSimpleName();

    private DbActionFacade dbAction;
    private DateTimeAction dateTimeAction;
    private FileSystemAction fileSystemAction;

    public NoteEntityAction() {
        dbAction            = new DbActionFacade();
        dateTimeAction      = new DateTimeAction();
        fileSystemAction    = new FileSystemAction();
    }

    public NoteEntity createNoteEntity(){
        Logcat.i(TAG,"createNoteEntity");
        String id = new IdAction().getNoteId();
        DateTime now = dateTimeAction.toNow();

        NoteEntity noteEntity = new NoteEntity();
        noteEntity.setId(id);
        noteEntity.setLoadType(UnLoadType.NEW);
        noteEntity.setNoteName(dateTimeAction.toFormat(now));
        noteEntity.setCreateTime(now);
        noteEntity.setFileRPath(new FileSystemAction().getNoteDocRPath());
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
        String noteFileSuffix = MyRInfo.getStringByID(R.string.config_md_file_suffix);
        return fileSystemAction.getSdcardPath() + noteEntity.getFileRPath() + noteEntity.getNoteName() + noteFileSuffix;
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
        return dbAction.saveNoteEntity(Flowable.just(noteEntity).doOnNext(new Consumer<NoteEntity>() {
            @Override
            public void accept(NoteEntity noteEntity) throws Exception {
                DateTime now    = DateTime.now();
                if(TextUtils.isEmpty(noteEntity.getTitle()))
                    noteEntity.setTitle(noteEntity.getNoteName());
                if(TextUtils.isEmpty(noteEntity.getDetail()) && !TextUtils.isEmpty(noteEntity.getLastModifyContext())){
                    String tmp = noteEntity.getLastModifyContext();
                    int endIndex = tmp.length() > 20 ? 20 : tmp.length();
                    noteEntity.setDetail(tmp.substring(0, endIndex));
                }
                noteEntity.setLastAccessTime(now);
                noteEntity.setFileAbsPath(getMdFileAbsPath(noteEntity));
                Logcat.i(TAG,"toSaveNote, doOnNext: " + noteEntity.toString());
            }
        }));
    }
}
