package ahtewlg7.utimer.mvp;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import io.reactivex.Flowable;

public interface IGtdInboxMvpM {
    public Flowable<Optional<GtdProjectEntity>> getGtdProject(String id);
    public boolean toProjectAnInbox(GtdProjectEntity gtdProjectEntity, GtdInboxEntity gtdInboxEntity);
}
