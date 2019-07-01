package ahtewlg7.utimer.mvp.rw;

import androidx.annotation.NonNull;

import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/7.
 */
public abstract class AUtimerRwMvpP<V, M extends AUtimerRwMvpM<V>> {
    protected boolean isLoaded = false;
    protected IDbMvpV mvpV;
    protected M mvpM;

    protected abstract M getMvpM();
    protected abstract MySafeSubscriber<Boolean> getSaveSubscriber();
    protected abstract MySafeSubscriber<Boolean> getDelSubscriber();
    protected abstract MySafeSubscriber<V> getLoadAllSubscriber();

    public AUtimerRwMvpP(IDbMvpV mvpV){
        this.mvpV = mvpV;
        mvpM = getMvpM();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void toSave(@NonNull Flowable<V> entityRx){
        mvpM.toSave(entityRx).subscribe(getSaveSubscriber());
    }
    public void toDel(@NonNull Flowable<V> entityRx){
        mvpM.toDel(entityRx).subscribe(getDelSubscriber());
    }
    public void toLoadAll() {
        mvpM.loadAll().subscribe(getLoadAllSubscriber());
    }

    public interface IDbMvpV {
        public void onAllLoadStarted();
        public void onAllLoadEnd();
    }
}
