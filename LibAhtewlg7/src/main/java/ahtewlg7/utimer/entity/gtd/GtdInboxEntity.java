package ahtewlg7.utimer.entity.gtd;

import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdInboxEntity extends AGtdTaskEntity {
    public static final String TAG = GtdInboxEntity.class.getSimpleName();

    private boolean actionable;

    public GtdInboxEntity() {
        super();
    }

    public GtdInboxEntity(GtdOnlineEntity gtdOnlineEntity){
        super();
        setId(gtdOnlineEntity.getId());
    }

    public GtdInboxEntity(GtdNewEntity gtdNewEntity){
        super();
        setId(gtdNewEntity.getId());
    }

    @Override
    public GtdType getTaskType() {
        return GtdType.INBOX;
    }

    public boolean isActionable() {
        return actionable;
    }

    public void setActionable(boolean actionable) {
        this.actionable = actionable;
    }

    @Override
    public String toString() {
        return super.toString() + ", actionable = " + actionable;
    }
}
