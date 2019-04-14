package ahtewlg7.utimer.mvp.db;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.db.entity.NextIdGdBean;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.factory.DbNextIdFactory;
import ahtewlg7.utimer.mvp.ADbMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/7.
 */
class TableShortHandMvpP extends ADbMvpP<NextIdGdBean, TableNextIdMvpM> {

    public TableShortHandMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toSaveAll(){
        toSave(getNextIdBeanRx());
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
                DbNextIdFactory.getInstance().put(idKeyGdBean.getGtdType(), idKeyGdBean.getNextId());
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

    private Flowable<NextIdGdBean> getNextIdBeanRx(){
        return Flowable.just(getActionNextIdBean());
    }
    private NextIdGdBean getActionNextIdBean(){
        NextIdGdBean gdBean = new NextIdGdBean();
        gdBean.setGtdType(GtdType.ACTION);
        gdBean.setNextId(DbNextIdFactory.getInstance().getValue(GtdType.ACTION));
        return gdBean;
    }
}
