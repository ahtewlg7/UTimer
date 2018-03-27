package ahtewlg7.utimer.util;

import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

public class MySafeFlowableOnSubscribe<T>  implements FlowableOnSubscribe<T> {
    public static final String TAG = MySafeFlowableOnSubscribe.class.getSimpleName();

    private FlowableEmitter<T> flowableEmitter;

    protected boolean ifShouldHoldEmit(){
        return flowableEmitter != null && flowableEmitter.requested() == 0;
    }

    @Override
    public void subscribe(FlowableEmitter<T> e) throws Exception {
        flowableEmitter = e;
    }
}
