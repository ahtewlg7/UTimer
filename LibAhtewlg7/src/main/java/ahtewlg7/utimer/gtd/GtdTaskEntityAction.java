package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.un.GtdTaskEntity;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class GtdTaskEntityAction {
    public static final String TAG = GtdTaskEntityAction.class.getSimpleName();

    private DbActionFacade dbAction;
    private FileSystemAction fileSystemAction;

    public GtdTaskEntityAction() {
        dbAction = new DbActionFacade();
        fileSystemAction = new FileSystemAction();
    }

    public boolean isEventFileExist(GtdTaskEntity gtdEventEntity){
        String noteFileAbsPath = getEventFileAbsPath(gtdEventEntity);
        boolean isFileExists = FileUtils.isFileExists(noteFileAbsPath);
        Logcat.i(TAG,"isNoteFileExist noteFileAbsPath = " + noteFileAbsPath + ", isFileExists = " + isFileExists);
        return isFileExists;
    }

    public String getEventFileAbsPath(GtdTaskEntity gtdEventEntity){
        return fileSystemAction.getSdcardPath() + /*gtdEventEntity.getFileRPath() +*/ gtdEventEntity.getTitle();
    }

    public Flowable<Optional<GtdTaskEntity>> loadAddEvent(){
//        return dbAction.loadAllEventEntity();
        return null;
    }

    public Flowable<Optional<GtdTaskEntity>> loadEvent(Flowable<Optional<String>> nameFlowable){
//        return dbAction.getEventEntity(nameFlowable);
        return null;
    }

    public Flowable<Boolean> deleteEvent(Flowable<Optional<GtdTaskEntity>> nameFlowable){
//        return dbAction.deleteEventEntity(nameFlowable);
        return null;
    }

    public Flowable<GtdTaskEntity> filterOptional(@NonNull Flowable<Optional<GtdTaskEntity>> eventOptionalFlowable){
        return eventOptionalFlowable.filter(new Predicate<Optional<GtdTaskEntity>>() {
                    @Override
                    public boolean test(Optional<GtdTaskEntity> gtdEventEntityOptional) throws Exception {
                        return gtdEventEntityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<GtdTaskEntity>, GtdTaskEntity>() {
                    @Override
                    public GtdTaskEntity apply(Optional<GtdTaskEntity> gtdEventEntityOptional) throws Exception {
                        return gtdEventEntityOptional.get();
                    }
                });
    }
}
