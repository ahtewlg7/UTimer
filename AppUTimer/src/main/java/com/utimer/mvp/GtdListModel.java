package com.utimer.mvp;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.GTD.GtdEntityFactory;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import io.reactivex.Flowable;

public class GtdListModel implements IRecyclerViewMvpM<AGtdEntity> {
    public static final String TAG = GtdListModel.class.getSimpleName();

    private GtdEntityFactory gtdEntityFactory;

    public GtdListModel(){
        gtdEntityFactory = new GtdEntityFactory();
    }

    @Override
    public Flowable<AGtdEntity> loadAllEntity() {
        return gtdEntityFactory.loadAll();
    }

    @Override
    public Flowable<AGtdEntity> loadEntity(@NonNull Flowable<String> idObservable) {
        return gtdEntityFactory.loadEntity(idObservable);
    }
}
