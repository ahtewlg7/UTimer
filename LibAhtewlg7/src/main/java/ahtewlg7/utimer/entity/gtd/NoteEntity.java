package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2019/1/23.
 */
public class NoteEntity extends AUtimerEntity<NoteBuilder> implements Serializable {
    public static final String TAG = NoteEntity.class.getSimpleName();

    private StringBuilder detailBuilder;

    protected NoteEntity(@Nonnull NoteBuilder builder) {
        super(builder);
        if(builder.projectEntity != null)
            initByProjectEntity(builder.projectEntity);
        if(!TextUtils.isEmpty(title) && attachFile == null){
            String filePath = new FileSystemAction().getInboxGtdAbsPath();
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
        return detailBuilder == null ? Optional.<String>absent() : Optional.of(detailBuilder.toString());
    }

    @Override
    public IMergerEntity merge(IMergerEntity entity) {
        return entity;
    }

    @Override
    public Optional<String> toTips() {
        return getDetail();
    }

    public void appendDetail(String append){
        if(TextUtils.isEmpty(append)){
            Logcat.i(TAG,"append String empty");
            return;
        }
        if(detailBuilder == null)
            detailBuilder = new StringBuilder(append);
        else
            detailBuilder.append(append);
    }

    private void initByProjectEntity(GtdProjectEntity projectEntity){
        projectEntity.getAttachFile();
    }
}
