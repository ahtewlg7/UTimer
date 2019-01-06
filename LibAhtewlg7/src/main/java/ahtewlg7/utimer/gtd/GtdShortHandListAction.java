package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.util.DateTimeAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class GtdShortHandListAction {
    public static final String TAG = GtdShortHandListAction.class.getSimpleName();

    protected DbActionFacade dbActionFacade;
    protected FileSystemAction fileSystemAction;

    public GtdShortHandListAction(){
        dbActionFacade   = new DbActionFacade();
        fileSystemAction = new FileSystemAction();
    }

    public String getInboxRDir(){
        return fileSystemAction.getInboxGtdRPath();
    }
    public String getInboxAbsDir(){
        return fileSystemAction.getInboxGtdAbsPath();
    }

    public Flowable<ShortHandEntity> loadAllEntity() {
//        return Flowable.merge(filterInvalidEntity(getFsShortHand(getShortHandFiles())),
//                              filterInvalidEntity(getDbShortHand(getShortHandFiles())))
        return filterInvalidEntity(getFsShortHand(getShortHandFiles()));
    }

    public ShortHandEntity newEntity() {
        return null;
    }

    public Flowable<Boolean> saveEntity(Flowable<Optional<ShortHandEntity>> flowable) {
        return dbActionFacade.saveShortHandEntity(flowable);
    }

    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<ShortHandEntity>> flowable) {
        return dbActionFacade.deleteShortHandEntity(flowable);
    }

    public Flowable<ShortHandEntity> getTodayShortHandList(){
        return loadAllEntity().filter(new Predicate<ShortHandEntity>() {
            @Override
            public boolean test(ShortHandEntity entity) throws Exception {
                return new DateTimeAction().isToday(entity.getCreateTime());
            }
        });
    }
    private Flowable<ShortHandEntity> filterInvalidEntity(@NonNull Flowable<Optional<ShortHandEntity>> shortHandRx){
        return shortHandRx.filter(new Predicate<Optional<ShortHandEntity>>() {
                    @Override
                    public boolean test(Optional<ShortHandEntity> entityOptional) throws Exception {
                        return entityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<ShortHandEntity>, ShortHandEntity>() {
                    @Override
                    public ShortHandEntity apply(Optional<ShortHandEntity> entityOptional) throws Exception {
                        return entityOptional.get();
                    }
                });
    }

    private Flowable<File> getShortHandFiles(){
        return Flowable.just(getInboxAbsDir()).flatMap(new Function<String, Publisher<File>>() {
            @Override
            public Publisher<File> apply(String path) throws Exception {
                File shortHandDir = FileUtils.getFileByPath(path);
                if(!shortHandDir.exists())
                    throw new FileNotFoundException("ShortHand file is missing");
                return Flowable.fromArray(shortHandDir.listFiles());
            }
        });
    }
    private Flowable<Optional<ShortHandEntity>> getFsShortHand(Flowable<File> shortHandRx){
        return shortHandRx.map(new Function<File, Optional<ShortHandEntity>>() {
            @Override
            public Optional<ShortHandEntity> apply(File file) throws Exception {
                MdAttachFile attachFile = new MdAttachFile(file);
                if(!attachFile.ifValid())
                    return Optional.absent();
                ShortHandEntity e = (ShortHandEntity)new ShortHandBuilder().setAttachFile(attachFile).build();
                return Optional.fromNullable(e);
            }
        });
    }
    private Flowable<Optional<ShortHandEntity>> getDbShortHand(Flowable<File> shortHandRx){
        return dbActionFacade.getShortHandEntityByTitle(shortHandRx.map(new Function<File, String>() {
                    @Override
                    public String apply(File file) throws Exception {
                        return file.getName();
                    }
                }));
    }
}
