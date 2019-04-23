package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.factory.NoteByUuidFactory;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2019/3/27.
 */
public class NoteRwMvpM extends AUtimerRwMvpM<NoteEntity> {
    protected FileSystemAction fileSystemAction;

    public NoteRwMvpM(){
        super();
        fileSystemAction    = new FileSystemAction();
    }

    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<NoteEntity> entityRx) {
        return dbActionFacade.saveNoteEntity(entityRx);
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<NoteEntity> entityRx) {
        return dbActionFacade.deleteNoteEntity(entityRx.doOnNext(new Consumer<NoteEntity>() {
            @Override
            public void accept(NoteEntity entity) throws Exception {
                boolean result = toDelEntity(entity);
                if(result)
                    NoteByUuidFactory.getInstance().remove(entity.getUuid());
            }
        }));
    }

    @Deprecated
    @Override
    public Flowable<NoteEntity> loadAll() {
        return Flowable.empty();
    }

    public Flowable<NoteEntity> loadAll(GtdProjectEntity projectEntity) {
        return filterInvalidEntity(getNoteEntity(getNoteFiles(projectEntity)));
    }

    private Flowable<NoteEntity> filterInvalidEntity(@NonNull Flowable<Optional<NoteEntity>> shortHandRx){
        return shortHandRx.filter(new Predicate<Optional<NoteEntity>>() {
                    @Override
                    public boolean test(Optional<NoteEntity> entityOptional) throws Exception {
                        return entityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<NoteEntity>, NoteEntity>() {
                    @Override
                    public NoteEntity apply(Optional<NoteEntity> entityOptional) throws Exception {
                        NoteEntity entity = entityOptional.get();
                        NoteByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        return entity;

                    }
                });
    }

    private Flowable<File> getNoteFiles(GtdProjectEntity projectEntity){
        return Flowable.just(projectEntity).flatMap(new Function<GtdProjectEntity, Publisher<File>>() {
            @Override
            public Publisher<File> apply(GtdProjectEntity projectEntity) throws Exception {
                if(projectEntity.getAttachFile() == null || !projectEntity.getAttachFile().ifValid())
                    throw new FileNotFoundException("Project file is missing");
                return Flowable.fromArray(projectEntity.getAttachFile().getFile().listFiles());
            }
        });
    }
    private Flowable<Optional<NoteEntity>> getNoteEntity(Flowable<File> fileRx){
        return fileRx.map(new Function<File, Optional<NoteEntity>>() {
            @Override
            public Optional<NoteEntity> apply(File file) throws Exception {
                Optional<NoteEntity> entityOptional =
                        dbActionFacade.getNoteEntityByRPath(fileSystemAction.getRPath(file));
                MdAttachFile attachFile = new MdAttachFile(file);
                if(entityOptional.isPresent()){
                    if(attachFile.ifValid())
                        entityOptional.get().updateAttachFileInfo(attachFile);
                    return entityOptional;
                }
                if(attachFile.ifValid()) {
                    NoteEntity entity = new NoteBuilder().setAttachFile(attachFile).build();
                    return Optional.of(entity);
                }
                return Optional.absent();
            }
        });
    }
    private boolean toDelEntity(NoteEntity entity) {
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
