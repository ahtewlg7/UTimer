package ahtewlg7.utimer.gtd;

import com.google.common.base.Optional;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;

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
}
