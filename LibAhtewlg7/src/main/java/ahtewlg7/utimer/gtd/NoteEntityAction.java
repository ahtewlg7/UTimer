package ahtewlg7.utimer.gtd;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.un.NoteEntity;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2017/12/28.
 */

public class NoteEntityAction{
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
        noteEntity.setNoteName(dateTimeAction.toFormat(now));
        noteEntity.setCreateTime(now);
        noteEntity.setFileRPath(new FileSystemAction().getNoteDocRPath());
        noteEntity.setLastAccessTime(now);
        return noteEntity;
    }

    //=====================================IBaseRecyclerViewMvpM========================================
    public Flowable<Optional<NoteEntity>> loadAllEntity() {
//        return dbAction.loadAllNoteEntity();
        return null;
    }

    public Flowable<Optional<NoteEntity>> loadEntity(@NonNull Flowable<Optional<String>> idObservable) {
//        return dbAction.getNoteEntity(idObservable);
        return null;
    }

    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<NoteEntity>> flowable) {
//        return dbAction.deleteNoteEntity(flowable);
        return null;
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

    public Flowable<Boolean> toSaveNote(NoteEntity noteEntity) {
        /*return dbAction.saveNoteEntity(Flowable.just(noteEntity).doOnNext(new Consumer<NoteEntity>() {
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
        }));*/
        return null;
    }
}
