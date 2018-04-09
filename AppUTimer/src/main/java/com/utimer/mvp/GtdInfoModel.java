package com.utimer.mvp;

import ahtewlg7.utimer.GTD.GtdEntityFactory;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.mvp.IGtdInfoMvpM;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

public class GtdInfoModel implements IGtdInfoMvpM<AGtdEntity> {
    public static final String TAG = GtdInfoModel.class.getSimpleName();

    private GtdEntityFactory gtdEntityFactory;

    public GtdInfoModel(){
        gtdEntityFactory = new GtdEntityFactory();
    }

    @Override
    public Flowable<AGtdEntity> loadEntity(@NonNull String entityID) {
        return gtdEntityFactory.loadEntity(Flowable.just(entityID));
    }
}
