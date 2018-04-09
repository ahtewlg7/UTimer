package com.utimer.mvp;

import android.text.TextUtils;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.mvp.IGtdInfoMvpM;
import ahtewlg7.utimer.mvp.IGtdInfoMvpP;
import ahtewlg7.utimer.mvp.IGtdInfoMvpV;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2018/3/17.
 */

public class GtdInfoMvpPresenter implements IGtdInfoMvpP {
    public static final String TAG = GtdInfoMvpPresenter.class.getSimpleName();

    protected AGtdEntity currGtdEntity;
    protected IGtdInfoMvpM<AGtdEntity> gtdInfoMvpM;
    protected IGtdInfoMvpV gtdInfoMvpV;

    public GtdInfoMvpPresenter(IGtdInfoMvpV gtdInfoMvpV){
        this.gtdInfoMvpV = gtdInfoMvpV;
        gtdInfoMvpM      = new GtdInfoModel();
    }

    @Override
    public void laodGtdEntity(String id) {
        if (TextUtils.isEmpty(id)) {
            gtdInfoMvpV.onIdEnptyErr();
            return;
        }
        gtdInfoMvpM.loadEntity(id).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new FlowableSubscriber<AGtdEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    s.request(Long.MAX_VALUE);
                    gtdInfoMvpV.onInfoStart();
                    currGtdEntity = null;
                }

                @Override
                public void onNext(AGtdEntity gtdEntity) {
                    currGtdEntity = gtdEntity;
                    gtdInfoMvpV.updateView(gtdEntity);
                }

                @Override
                public void onError(Throwable t) {
                    gtdInfoMvpV.onInfoErr();
                }

                @Override
                public void onComplete() {
                    gtdInfoMvpV.onInfoEnd();
                }
            });
    }

}
