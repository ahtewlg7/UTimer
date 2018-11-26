package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.io.File;
import java.io.FileNotFoundException;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.factory.ShortHandFactory;
import ahtewlg7.utimer.util.DateTimeAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
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

    public Flowable<Optional<ShortHandEntity>> loadAllEntity() {
        return getShortHandFiles().flatMap(new Function<File, Publisher<Optional<ShortHandEntity>>>() {
                    @Override
                    public Publisher<Optional<ShortHandEntity>> apply(final File file) throws Exception {
                        return loadEntity(Flowable.just(Optional.of(file.getName())));
                    }
                })
                .doOnSubscribe(new Consumer<Subscription>() {
                    @Override
                    public void accept(Subscription subscription) throws Exception {
                        ShortHandFactory.getInstance().clearAll();
                    }
                })
                .doOnNext(new Consumer<Optional<ShortHandEntity>>() {
                    @Override
                    public void accept(Optional<ShortHandEntity> optional) throws Exception {
                        if(optional.isPresent())
                            ShortHandFactory.getInstance().addValue(optional.get());
                        else
                            ShortHandFactory.getInstance().newAndAddVaule();
                    }
                });
    }

    public Flowable<Optional<ShortHandEntity>> loadEntity(@NonNull Flowable<Optional<String>> titleFlowable){
        return dbActionFacade.getShortHandEntityByTitle(titleFlowable);
    }

    public ShortHandEntity newEntity() {
        return ShortHandFactory.getInstance().newValue();
    }

    public Flowable<Boolean> saveEntity(Flowable<Optional<ShortHandEntity>> flowable) {
        return dbActionFacade.saveShortHandEntity(flowable);
    }

    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<ShortHandEntity>> flowable) {
        return dbActionFacade.deleteShortHandEntity(flowable);
    }

    public Flowable<Optional<ShortHandEntity>> getTodayShortHandList(){
        return loadAllEntity().filter(new Predicate<Optional<ShortHandEntity>>() {
            @Override
            public boolean test(Optional<ShortHandEntity> optional) throws Exception {
                return optional.isPresent() && new DateTimeAction().isToday(optional.get().getCreateTime());
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
}
