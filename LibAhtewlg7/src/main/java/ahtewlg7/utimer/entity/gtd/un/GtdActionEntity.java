package ahtewlg7.utimer.entity.gtd.un;

import androidx.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.taskContext.IPosition;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdType;

public class GtdActionEntity extends AGtdEntity {
    public static final String TAG = GtdActionEntity.class.getSimpleName();

    private IPosition position;
    private DeedState actionType;
    private GtdTaskEntity gtdTaskEntity;

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.DEED;
    }


    public IPosition getPosition() {
        return position;
    }

    public void setPostion(IPosition position) {
        this.position = position;
    }

    public DeedState getActionType() {
        return actionType;
    }

    public void setActionType(DeedState actionType) {
        this.actionType = actionType;
    }

    public GtdTaskEntity getGtdTaskEntity() {
        return gtdTaskEntity;
    }

    public void setGtdTaskEntity(GtdTaskEntity gtdTaskEntity) {
        this.gtdTaskEntity = gtdTaskEntity;
    }

    @Override
    public @NonNull String toJson() {
        //todo
        return "";
    }

    @Override
    public boolean isDone() {
        return w5h2Entity != null && w5h2Entity.getHowMuch() != null && w5h2Entity.getHowMuch().getPlanPercent() == 100;
    }

    @Override
    public Optional<String> toTips() {
        if(w5h2Entity == null && !ifValid())
            return Optional.absent();
        StringBuilder builder = new StringBuilder();
        if(TextUtils.isEmpty(title))
            builder.append(title);
        if(TextUtils.isEmpty(detail))
            builder.append(detail);
        builder.append(", done = ").append(isDone());
        if(w5h2Entity != null && w5h2Entity.toTips().isPresent())
            builder.append(w5h2Entity.toTips().get());
        return Optional.fromNullable(TextUtils.isEmpty(builder.toString()) ? null : builder.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        if(position != null)
            builder.append("，position" ).append(position.toString());
        builder.append(super.toString());
        return builder.toString();
    }
}
