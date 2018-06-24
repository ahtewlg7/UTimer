package ahtewlg7.utimer.util;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MySimpleObserver<T> implements Observer<T> {
    public static final String TAG = MySimpleObserver.class.getSimpleName();

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        Logcat.i(TAG,"onError : " + e.getMessage());
    }

    @Override
    public void onComplete() {

    }
}
