package ahtewlg7.utimer.mvp;

public interface IGtdInboxMvpV {
    public void onInboxUninited();
    public void onProjectingStart();
    public void onProjectingSucc();
    public void onProjectingFail();
    public void onProjectingEnd();
    public void onProjectingErr(Throwable t);
}
