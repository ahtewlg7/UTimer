package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/27.
 */
public class ShortHandRwMvpM extends AUtimerRwMvpM<ShortHandEntity> {

    protected FileSystemAction fileSystemAction;

    public ShortHandRwMvpM(){
        super();
        fileSystemAction = new FileSystemAction();
    }

    public String getInboxRDir(){
        return fileSystemAction.getInboxGtdRPath();
    }
    public String getInboxAbsDir(){
        return fileSystemAction.getInboxGtdAbsPath();
    }


    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<ShortHandEntity> entityRx) {
        return dbActionFacade.saveShortHandEntity(entityRx);
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<ShortHandEntity> entityRx) {
        return dbActionFacade.deleteShortHandEntity(entityRx);
    }

    @Override
    public Flowable<ShortHandEntity> loadAll() {
        return filterInvalidEntity(getShortHand(getShortHandFiles()));
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

    private Flowable<Optional<ShortHandEntity>> getShortHand(final Flowable<File> fileRx){
        return fileRx.map(new Function<File, Optional<ShortHandEntity>>() {
                @Override
                public Optional<ShortHandEntity> apply(File file) throws Exception {
                    Optional<ShortHandEntity> shortHandEntityOptional =
                            dbActionFacade.getShortHandEntityByRPath(fileSystemAction.getRPath(file));
                    if(shortHandEntityOptional.isPresent())
                        return shortHandEntityOptional;
                    MdAttachFile attachFile = new MdAttachFile(file);
                    if(!attachFile.ifValid())
                        return Optional.absent();
                    ShortHandEntity entity = new ShortHandBuilder().setAttachFile(attachFile).build();
                    return Optional.of(entity);
                }
            });
    }
}
