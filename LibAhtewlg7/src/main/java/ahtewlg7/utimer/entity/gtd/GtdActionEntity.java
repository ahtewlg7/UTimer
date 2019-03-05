package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.AGtdUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;


public class GtdActionEntity extends AGtdUtimerEntity<GtdActionBuilder> implements Serializable {
    public static final String TAG = GtdActionEntity.class.getSimpleName();

    private String action;
    private List<DateTime> timeList;
    private StringBuilder detailBuilder;

    protected GtdActionEntity(@Nonnull GtdActionBuilder builder) {
        super(builder);
//        if(builder.timeList != null)
//            initByEditElement(builder.timeList);
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
    public IMergerEntity merge(IMergerEntity entity) {
        return entity;
    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getProjectGtdAbsPath();
            attachFile = new DirAttachFile(filePath, fileName);
        }
        boolean result = attachFile.createOrExist();
        Logcat.i(TAG,"ensureAttachFileExist result = " + result);
        return result;
    }

    private void initByEditElement(EditElement editElement){
        if(!TextUtils.isEmpty(editElement.getMdCharSequence()))
            action = editElement.getMdCharSequence().toString();
        else
            action = editElement.getRawText();
    }
}
