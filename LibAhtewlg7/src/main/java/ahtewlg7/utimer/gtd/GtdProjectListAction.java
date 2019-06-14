package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.GtdProjectBuilder;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class GtdProjectListAction {
    public static final String TAG = GtdProjectListAction.class.getSimpleName();

    protected DbActionFacade dbActionFacade;
    protected FileSystemAction fileSystemAction;

    public GtdProjectListAction(){
        dbActionFacade   = new DbActionFacade();
        fileSystemAction = new FileSystemAction();
    }

    public String getProjectRDir(){
        return fileSystemAction.getProjectGtdRPath();
    }
    public String getProjectAbsDir(){
        return fileSystemAction.getProjectNoteAbsPath();
    }

    public Flowable<GtdProjectEntity> loadAllEntity() {
//        return Flowable.update(filterInvalidEntity(getFsProject(getProjectDirs())),
//                              filterInvalidEntity(getDbShortHand(getProjectDirs())))
        return filterInvalidEntity(getFsProject(getProjectDirs()));
    }

    public Flowable<Boolean> saveEntity(Flowable<ShortHandEntity> flowable) {
        return dbActionFacade.saveShortHandEntity(flowable);
    }

    /*public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<GtdProjectEntity>> flowable) {
        return dbActionFacade.deleteShortHandEntity(flowable);
    }*/

    private Flowable<GtdProjectEntity> filterInvalidEntity(@NonNull Flowable<Optional<GtdProjectEntity>> shortHandRx){
        return shortHandRx.filter(new Predicate<Optional<GtdProjectEntity>>() {
                    @Override
                    public boolean test(Optional<GtdProjectEntity> entityOptional) throws Exception {
                        return entityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<GtdProjectEntity>, GtdProjectEntity>() {
                    @Override
                    public GtdProjectEntity apply(Optional<GtdProjectEntity> entityOptional) throws Exception {
                        return entityOptional.get();
                    }
                });
    }

    private Flowable<File> getProjectDirs(){
        return Flowable.just(getProjectAbsDir()).flatMap(new Function<String, Publisher<File>>() {
            @Override
            public Publisher<File> apply(String path) throws Exception {
                File projectDir = FileUtils.getFileByPath(path);
                if(!projectDir.exists())
                    throw new FileNotFoundException("project dir is missing");
                return Flowable.fromArray(projectDir.listFiles());
            }
        });
    }
    private Flowable<Optional<GtdProjectEntity>> getFsProject(Flowable<File> shortHandRx){
        return shortHandRx.map(new Function<File, Optional<GtdProjectEntity>>() {
            @Override
            public Optional<GtdProjectEntity> apply(File file) throws Exception {
                DirAttachFile attachFile = new DirAttachFile(file);
                if(!attachFile.ifValid())
                    return Optional.absent();
                GtdProjectEntity p = (GtdProjectEntity)new GtdProjectBuilder().setAttachFile(attachFile).build();
                return Optional.of(p);
            }
        });
    }
    private Flowable<Optional<ShortHandEntity>> getDbShortHand(Flowable<File> shortHandRx){
        /*return dbActionFacade.getShortHandEntityByTitle(shortHandRx.map(new Function<File, String>() {
                    @Override
                    public String apply(File file) throws Exception {
                        return file.getName();
                    }
                }));*/
        return null;
    }
}
