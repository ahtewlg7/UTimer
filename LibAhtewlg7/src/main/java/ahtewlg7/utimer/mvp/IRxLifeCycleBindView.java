package ahtewlg7.utimer.mvp;

import com.trello.rxlifecycle2.LifecycleProvider;

import io.reactivex.annotations.NonNull;

public interface IRxLifeCycleBindView {
    public @NonNull LifecycleProvider getRxLifeCycleBindView();
}
