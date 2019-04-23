package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.comparator.ITimeComparator;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2019/1/23.
 */
public class NoteEntity extends AUtimerEntity<NoteBuilder>
        implements Serializable , ITimeComparator {
    private String rPath;

    protected NoteEntity(@Nonnull NoteBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        if(builder.projectEntity != null)
            initByProjectEntity(builder.projectEntity);
        if(!TextUtils.isEmpty(title) && attachFile == null){
            String filePath = new FileSystemAction().getNoteDocAbsPath();
            attachFile      = new MdAttachFile(filePath, title);
        }
    }

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.NOTE;
    }

    @Override
    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail)? Optional.<String>absent() : Optional.of(detail);
    }

    @Override
    public void update(IMergerEntity entity) {
    }

    @Override
    public Optional<String> toTips() {
        return getDetail();
    }

    @Override
    public boolean ensureAttachFileExist() {
        boolean result = attachFile.createOrExist();
        return result;
    }

    //++++++++++++++++++++++++++++++++++++++ITimeComparator++++++++++++++++++++++++++++++++++++
    @Override
    public Optional<DateTime> getComparatorTime() {
        return Optional.fromNullable(lastAccessTime);
    }

    private void initByProjectEntity(GtdProjectEntity projectEntity){
        if(projectEntity == null || !projectEntity.ifValid()){
            return;
        }
        if(TextUtils.isEmpty(title))
            title = new DateTimeAction().toFormatNow().toString();

        if(attachFile == null){
            String filePath = new FileSystemAction().getProjectGtdAbsPath() + projectEntity.getTitle();
            attachFile      = new MdAttachFile(filePath, title);
        }
    }

    private void  initByGbBean(NoteEntityGdBean gdBean){
        uuid            = gdBean.getUuid();
        title           = gdBean.getTitle();
        detail          = gdBean.getDetail();
        accessTimes     = gdBean.getAccessTimes();
        createTime      = gdBean.getCreateTime();
        lastAccessTime  = gdBean.getLastAccessTime();
        rPath           = gdBean.getAttachFileRPath();
    }
}
