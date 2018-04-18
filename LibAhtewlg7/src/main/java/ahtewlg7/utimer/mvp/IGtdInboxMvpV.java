package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;

public interface IGtdInboxMvpV {
    public void onInboxUninited();
    public void onProjectingStart();
    public void onProjectingSucc(GtdProjectEntity gtdProjectEntity);
    public void onProjectingFail();
    public void onProjectingEnd();
    public void onProjectingErr(Throwable t);

    public void onProjectSaveSucc();
    public void onProjectSaveFail();
}
