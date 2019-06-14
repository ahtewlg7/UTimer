package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import com.google.common.base.Optional;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;


public class GtdProjectEntity extends AUtimerEntity<GtdProjectBuilder> implements Serializable {
    public static final String TAG = GtdProjectEntity.class.getSimpleName();

    private StringBuilder detailBuilder;

    protected GtdProjectEntity(@Nonnull GtdProjectBuilder builder) {
        super(builder);
    }

    @Override
    public GtdType getGtdType() {
        return GtdType.PROJECT;
    }

    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }

    @Override
    public Optional<String> getDetail() {
        return detailBuilder == null ? Optional.<String>absent() : Optional.of(detailBuilder.toString());
    }

    //todo
    @Override
    public void update(IMergerEntity entity) {
    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getProjectNoteAbsPath();
            attachFile = new DirAttachFile(filePath, fileName);
        }
        boolean result = attachFile.createOrExist();
        Logcat.i(TAG,"ensureAttachFileExist result = " + result);
        return result;
    }
}
