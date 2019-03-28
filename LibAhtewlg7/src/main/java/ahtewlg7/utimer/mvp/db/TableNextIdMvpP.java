package ahtewlg7.utimer.mvp.db;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.db.entity.NextIdGdBean;
import ahtewlg7.utimer.factory.DbNextIdFactory;
import ahtewlg7.utimer.mvp.ADbMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;

/**
 * Created by lw on 2019/3/7.
 */
public class TableNextIdMvpP extends ADbMvpP<NextIdGdBean, TableNextIdMvpM> {

    public TableNextIdMvpP(IGtdIdKeyMvpV mvpV){
        super(mvpV);
    }

    @Override
    protected TableNextIdMvpM getMvpM() {
        return new TableNextIdMvpM();
    }

    @Override
    protected MySafeSubscriber<Boolean> getSaveSubscriber() {
        return new MySafeSubscriber<Boolean>();
    }

    @Override
    protected MySafeSubscriber<Boolean> getDelSubscriber() {
        return new MySafeSubscriber<Boolean>();
    }

    @Override
    protected MySafeSubscriber<NextIdGdBean> getLoadAllSubscriber() {
        return new MySafeSubscriber<NextIdGdBean>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
            }

            @Override
            public void onNext(NextIdGdBean idKeyGdBean) {
                super.onNext(idKeyGdBean);
                DbNextIdFactory.getInstance().add(idKeyGdBean.getGtdType(), idKeyGdBean.getNextId());
            }

            @Override
            public void onComplete() {
                super.onComplete();
                isLoaded = true;
                if(mvpV != null)
                    mvpV.onAllLoadEnd();
            }
        };
    }

    public interface IGtdIdKeyMvpV extends IDbMvpV{
    }
}
