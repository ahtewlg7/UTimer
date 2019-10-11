package ahtewlg7.utimer.mvp.rw;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.factory.ProjectByUuidFactory;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/3/27.
 */
public class TableProjectRwMvpM extends AUtimerRwMvpM<GtdProjectEntity> {
    protected FileSystemAction fileSystemAction;

    public TableProjectRwMvpM(){
        super();
        fileSystemAction    = new FileSystemAction();
    }

    @Deprecated
    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<GtdProjectEntity> entityRx) {
        return dbActionFacade.saveProjectEntity(entityRx.doOnNext(new Consumer<GtdProjectEntity>() {
            @Override
            public void accept(GtdProjectEntity entity) throws Exception {
                ProjectByUuidFactory.getInstance().update(entity.getUuid(), entity);
            }
        })).subscribeOn(Schedulers.io());
    }
    @Deprecated
    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<GtdProjectEntity> entityRx) {
        return dbActionFacade.deleteProjectEntity(entityRx.doOnNext(new Consumer<GtdProjectEntity>() {
            @Override
            public void accept(GtdProjectEntity entity) throws Exception {
                boolean result = toDelEntity(entity);
                if(result)
                    ProjectByUuidFactory.getInstance().remove(entity.getUuid());
            }
        })).subscribeOn(Schedulers.io());
    }

    @Override
    public Flowable<GtdProjectEntity> loadAll() {
        return dbActionFacade.loadAllProjectEntity().filter(new Predicate<GtdProjectEntity>() {
                    @Override
                    public boolean test(GtdProjectEntity gtdProjectEntity) throws Exception {
                        return gtdProjectEntity.ifValid();
                    }
                }).subscribeOn(Schedulers.io());
    }

    /*public Flowable<Optional<GtdProjectEntity>> getGtdProjectEntity(Flowable<File> fileRx){
        return fileRx.map(new Function<File, Optional<GtdProjectEntity>>() {
            @Override
            public Optional<GtdProjectEntity> apply(File file) throws Exception {
                Optional<GtdProjectEntity> entityOptional = dbActionFacade.getProjectEntityByAbsPath(file.getAbsolutePath());
                MdAttachFile attachFile = new MdAttachFile(file);
                if(entityOptional.isPresent()){
                    if(attachFile.ifValid())
                        entityOptional.get().updateAttachFileInfo(attachFile);
                    return entityOptional;
                }
                if(attachFile.ifValid()) {
                    GtdProjectEntity entity = new GtdProjectBuilder().setAttachFile(attachFile).build();
                    return Optional.of(entity);
                }
                return Optional.absent();
            }
        }).subscribeOn(Schedulers.io());
    }*/

    private boolean toDelEntity(GtdProjectEntity entity) {
        boolean result = false;
        try{
            if(entity != null && entity.getAttachFile().ifValid())
                result = FileUtils.deleteFile(entity.getAttachFile().getFile());
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
