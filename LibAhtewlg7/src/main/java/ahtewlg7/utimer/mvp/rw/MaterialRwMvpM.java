package ahtewlg7.utimer.mvp.rw;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.AAttachFile;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.MaterialEntity;
import ahtewlg7.utimer.entity.gtd.MaterialEntityBuilder;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.factory.MaterialEntityByUuidFactory;
import ahtewlg7.utimer.factory.ProjectFileTreeGraphFactory;
import ahtewlg7.utimer.graphs.FileTreeGraph;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/3/27.
 */
public class MaterialRwMvpM extends AUtimerRwMvpM<MaterialEntity> {
    protected FileSystemAction fileSystemAction;

    public MaterialRwMvpM(){
        super();
        fileSystemAction    = new FileSystemAction();
    }

    @Deprecated
    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<MaterialEntity> entityRx) {
        return dbActionFacade.saveMaterialEntity(entityRx.doOnNext(new Consumer<MaterialEntity>() {
            @Override
            public void accept(MaterialEntity entity) throws Exception {
                MaterialEntityByUuidFactory.getInstance().update(entity.getUuid(), entity);
            }
        }));
    }
    @Deprecated
    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<MaterialEntity> entityRx) {
        return dbActionFacade.deleteMaterialEntity(entityRx.doOnNext(new Consumer<MaterialEntity>() {
            @Override
            public void accept(MaterialEntity entity) throws Exception {
                boolean result = toDelEntity(entity);
                if(result)
                    MaterialEntityByUuidFactory.getInstance().remove(entity.getUuid());
            }
        }));
    }

    @Override
    public Flowable<MaterialEntity> loadAll() {
        return loadAll(new File(fileSystemAction.getWorkingDocAbsPath()));
    }
    public Flowable<MaterialEntity> loadAll(@NonNull File rootFile) {
        return filterInvalidEntity(getMaterialEntity(
                Flowable.just(rootFile).doOnNext(new Consumer<File>() {
                    @Override
                    public void accept(File root) throws Exception {
                        ProjectFileTreeGraphFactory.getInstance().addFileTreeGraph(root);
                    }
                })
                .flatMap(new Function<File, Publisher<File>>() {
                    @Override
                    public Publisher<File> apply(File file) throws Exception {
                        return Flowable.fromIterable(ProjectFileTreeGraphFactory.getInstance().getFileTreeGraph(file).getAllLeafFile());
                    }
                })
                .subscribeOn(Schedulers.computation())));
    }

    private Flowable<MaterialEntity> filterInvalidEntity(@NonNull Flowable<Optional<MaterialEntity>> shortHandRx){
        return shortHandRx.filter(new Predicate<Optional<MaterialEntity>>() {
                    @Override
                    public boolean test(Optional<MaterialEntity> entityOptional) throws Exception {
                        return entityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<MaterialEntity>, MaterialEntity>() {
                    @Override
                    public MaterialEntity apply(Optional<MaterialEntity> entityOptional) throws Exception {
                        MaterialEntity entity = entityOptional.get();
                        MaterialEntityByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        return entity;

                    }
                });
    }

    /*private Flowable<File> getNoteFiles(GtdProjectEntity projectEntity){
        return Flowable.just(projectEntity).flatMap(new Function<GtdProjectEntity, Publisher<File>>() {
            @Override
            public Publisher<File> apply(GtdProjectEntity projectEntity) throws Exception {
                if(projectEntity.getAttachFile() == null || !projectEntity.getAttachFile().ifValid())
                    throw new FileNotFoundException("Project file is missing");
                return Flowable.fromArray(projectEntity.getAttachFile().getFile().listFiles());
            }
        });
    }*/
    private Flowable<Optional<MaterialEntity>> getMaterialEntity(Flowable<File> fileRx){
        return fileRx.map(new Function<File, Optional<MaterialEntity>>() {
            @Override
            public Optional<MaterialEntity> apply(File file) throws Exception {
                Optional<MaterialEntity> entityOptional = dbActionFacade.getMaterialEntityByAbsPath(file.getAbsolutePath());
                MdAttachFile attachFile = new MdAttachFile(file);
                if(entityOptional.isPresent()){
                    if(attachFile.ifValid())
                        entityOptional.get().updateAttachFileInfo(attachFile);
                    return entityOptional;
                }
                if(attachFile.ifValid()) {
                    MaterialEntity entity = new MaterialEntityBuilder().setAttachFile(attachFile).build();
                    return Optional.of(entity);
                }
                return Optional.absent();
            }
        });
    }

    public Flowable<List<MaterialEntity>> toLoadProjectMaterial(@NonNull Flowable<GtdProjectEntity> projectRx){
        return projectRx.map(new Function<GtdProjectEntity, List<MaterialEntity>>() {
            @Override
            public List<MaterialEntity> apply(GtdProjectEntity gtdProjectEntity) throws Exception {
                AAttachFile aAttachRootFile = gtdProjectEntity.getAttachFile();
                if(aAttachRootFile == null || !aAttachRootFile.ifValid())
                    throw new FileNotFoundException("Project file is missing");
                Optional<FileTreeGraph> fileTreeGraphOptional =  ProjectFileTreeGraphFactory.getInstance().addFileTreeGraph(aAttachRootFile.getFile());
                if(!fileTreeGraphOptional.isPresent())
                    return Lists.newArrayList();
                List<MaterialEntity> materialEntityList = Lists.newArrayList();
                List<File> materialFileList             = fileTreeGraphOptional.get().getAllLeafFile();
                for(File file : materialFileList){
                    Optional<MaterialEntity> materialEntityOptional = dbActionFacade.getMaterialEntityByAbsPath(file.getAbsolutePath());
                    if(materialEntityOptional.isPresent())
                        materialEntityList.add(materialEntityOptional.get());
                }
                return materialEntityList;
            }
        }).subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<MaterialEntity>> toLoadMaterial(@NonNull Flowable<File> materialFileRx){
        return materialFileRx.map(new Function<File, Optional<MaterialEntity>>() {
            @Override
            public Optional<MaterialEntity> apply(File file) throws Exception {
                return dbActionFacade.getMaterialEntityByAbsPath(file.getAbsolutePath());
            }
        }).subscribeOn(Schedulers.io());
    }
    private boolean toDelEntity(MaterialEntity entity) {
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
