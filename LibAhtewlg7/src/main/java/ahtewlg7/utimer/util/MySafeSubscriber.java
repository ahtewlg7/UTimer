package ahtewlg7.utimer.util;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class MySafeSubscriber implements Subscriber {
    public static final String TAG = MySafeSubscriber.class.getSimpleName();

    private Subscription subscription;
    @Override
    public void onSubscribe(Subscription s) {
        s.request(1);
        subscription = s;
    }

    @Override
    public void onNext(Object o) {
        subscription.request(1);
    }

    @Override
    public void onError(Throwable t) {

    }

    @Override
    public void onComplete() {

    }
}
