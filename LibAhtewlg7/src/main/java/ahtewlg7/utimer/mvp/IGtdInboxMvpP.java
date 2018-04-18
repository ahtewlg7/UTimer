package ahtewlg7.utimer.mvp;

import io.reactivex.annotations.NonNull;

public interface IGtdInboxMvpP {
    public void toWorkAsProject(@NonNull String projectId);
    public void toSaveAsProject();
}
