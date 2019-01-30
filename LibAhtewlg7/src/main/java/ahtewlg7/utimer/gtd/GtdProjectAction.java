package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import java.io.File;
import java.io.FileNotFoundException;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class GtdProjectAction {
    public static final String TAG = GtdProjectAction.class.getSimpleName();

    protected GtdProjectEntity projectEntity;
    protected FileSystemAction fileSystemAction;

    public GtdProjectAction(GtdProjectEntity projectEntity){
        this.projectEntity = projectEntity;
        fileSystemAction   = new FileSystemAction();
    }

    public Optional<String> getCurrProjectRDir(){
        if(projectEntity.ifValid() && projectEntity.getAttachFile() != null
                && projectEntity.getAttachFile().ifValid())
            return Optional.of(fileSystemAction.getRPath(projectEntity.getAttachFile().getFile()));
        return Optional.absent();
    }
    public Optional<String> getCurrProjectAbsDir(){
        if(projectEntity.ifValid() && projectEntity.getAttachFile() != null
                && projectEntity.getAttachFile().ifValid())
            return Optional.of(projectEntity.getAttachFile().getFile().getAbsolutePath());
        return Optional.absent();
    }

    public Flowable<NoteEntity> loadAllNote() {
        return filterInvalidEntity(getFsNote(getNoteFiles()));
    }
    private Flowable<NoteEntity> filterInvalidEntity(@NonNull Flowable<Optional<NoteEntity>> projectNoteRx){
        return projectNoteRx.filter(new Predicate<Optional<NoteEntity>>() {
                    @Override
                    public boolean test(Optional<NoteEntity> entityOptional) throws Exception {
                        return entityOptional.isPresent();
                        }
                })
                .map(new Function<Optional<NoteEntity>, NoteEntity>() {
                    @Override
                    public NoteEntity apply(Optional<NoteEntity> entityOptional) throws Exception {
                        return entityOptional.get();
                    }
                });
    }

    private Flowable<File> getNoteFiles(){
        return Flowable.just(projectEntity).flatMap(new Function<GtdProjectEntity, Publisher<File>>() {
            @Override
            public Publisher<File> apply(GtdProjectEntity projectEntity) throws Exception {
                if(projectEntity.getAttachFile() == null || !projectEntity.getAttachFile().ifValid())
                    throw new FileNotFoundException("Project file is missing");
                return Flowable.fromArray(projectEntity.getAttachFile().getFile().listFiles());
            }
        });
    }
    private Flowable<Optional<NoteEntity>> getFsNote(Flowable<File> projectNoteRx){
        return projectNoteRx.map(new Function<File, Optional<NoteEntity>>() {
            @Override
            public Optional<NoteEntity> apply(File file) throws Exception {
                MdAttachFile attachFile = new MdAttachFile(file);
                if(!attachFile.ifValid())
                    return Optional.absent();
                NoteEntity e = (NoteEntity)new NoteBuilder().setAttachFile(attachFile).build();
                return Optional.fromNullable(e);
            }
        });
    }

}
