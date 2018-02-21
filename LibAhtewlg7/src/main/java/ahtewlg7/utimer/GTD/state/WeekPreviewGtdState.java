package ahtewlg7.utimer.GTD.state;

import java.util.UUID;

import ahtewlg7.utimer.GTD.WeekPreviewEntityFactory;
import ahtewlg7.utimer.entity.gtd.GtdWeekPreviewEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.exception.GtdMachineException;

/**
 * Created by lw on 2017/10/3.
 */

public class WeekPreviewGtdState extends TrashGtdState {
    public static final String TAG = WeekPreviewGtdState.class.getSimpleName();

    public WeekPreviewGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    @Override
    GtdWeekPreviewEntity toPreviewWeekly()throws RuntimeException {
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(gtdEntity.getTaskType() != GtdType.INBOX || gtdEntity.getTaskType() != GtdType.PROJECT)
            super.toPreviewWeekly();
        else if(new WeekPreviewEntityFactory().isPreviewed(gtdEntity))
            throw new GtdMachineException(GtdErrCode.ERR_WEEK_PREVIEW_DUPLICATE);

        String taskId = UUID.randomUUID().toString();
        GtdWeekPreviewEntity gtdWeekPreviewEntity = new GtdWeekPreviewEntity();
        gtdWeekPreviewEntity.setId(taskId);
        gtdWeekPreviewEntity.setParentTaskEntity(gtdEntity);
        return gtdWeekPreviewEntity;
    }
}
