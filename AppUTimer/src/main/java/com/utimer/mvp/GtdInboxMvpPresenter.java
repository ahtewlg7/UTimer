package com.utimer.mvp;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.mvp.IGtdInboxMvpM;
import ahtewlg7.utimer.mvp.IGtdInboxMvpP;
import ahtewlg7.utimer.mvp.IGtdInboxMvpV;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class GtdInboxMvpPresenter implements IGtdInboxMvpP{
    public static final String TAG = GtdInboxMvpPresenter.class.getSimpleName();

    private GtdInboxEntity gtdInboxEntity;
    private GtdProjectEntity gtdProjectEntity;
    private IGtdInboxMvpV gtdInboxMvpV;
    private IGtdInboxMvpM gtdInboxMvpM;

    public GtdInboxMvpPresenter(GtdInboxEntity gtdInbxEntitiy, IGtdInboxMvpV gtdInboxMvpV) {
        this.gtdInboxEntity = gtdInbxEntitiy;
        this.gtdInboxMvpV   = gtdInboxMvpV;
        gtdInboxMvpM        = new GtdInboxModel();
    }

    @Override
    public void toWorkAsProject(@NonNull String projectId) {
        if(gtdInboxEntity == null) {
            gtdInboxMvpV.onInboxUninited();
            Logcat.i(TAG,"toWorkAsProject err : inbox is null");
            return;
        }
        gtdInboxMvpM.getGtdProject(projectId)
            .map(new Function<Optional<GtdProjectEntity>, GtdProjectEntity>() {
                @Override
                public GtdProjectEntity apply(Optional<GtdProjectEntity> gtdProjectEntityOptional) throws Exception {
                    GtdProjectEntity tmpProjectEntity = new GtdProjectEntity();
                    tmpProjectEntity.setId(new IdAction().getGtdId());
                    return gtdProjectEntityOptional.or(tmpProjectEntity);
                }
            })
            .doOnNext(new Consumer<GtdProjectEntity>() {
                @Override
                public void accept(GtdProjectEntity projectEntity) throws Exception {
                    gtdProjectEntity = projectEntity;
                }
            })
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new GtdInboxSafeSubscriber());
    }

    @Override
    public void toSaveAsProject() {
        if(gtdProjectEntity == null || !gtdProjectEntity.isValid())
            gtdInboxMvpV.onProjectSaveFail();
        if(gtdInboxMvpM.toSaveProject(gtdProjectEntity))
            gtdInboxMvpV.onProjectSaveSucc();
        else
            gtdInboxMvpV.onProjectSaveFail();
    }

    class GtdInboxSafeSubscriber extends MySafeSubscriber<GtdProjectEntity>{
        @Override
        public void onSubscribe(Subscription s) {
            super.onSubscribe(s);
            gtdInboxMvpV.onProjectingStart();
        }

        @Override
        public void onNext(GtdProjectEntity gtdProjectEntity) {
            super.onNext(gtdProjectEntity);
            boolean result = gtdInboxMvpM.toProjectAnInbox(gtdProjectEntity, gtdInboxEntity);
            if(result)
                gtdInboxMvpV.onProjectingSucc(gtdProjectEntity);
            else
                gtdInboxMvpV.onProjectingFail();
        }

        @Override
        public void onError(Throwable t) {
            super.onError(t);
            gtdInboxMvpV.onProjectingErr(t);
        }

        @Override
        public void onComplete() {
            super.onComplete();
            gtdInboxMvpV.onProjectingEnd();
        }
    }
}
