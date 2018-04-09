package com.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.utimer.entity.GtdSectionEntity;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.mvp.IGtdRecyclerViewMvpV;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.view.BaseSectionEntity;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2018/3/8.
 */

public class GtdRecylerMvpPresenter implements IRecyclerViewMvpP<BaseSectionEntity>{
    public static final String TAG = GtdRecylerMvpPresenter.class.getSimpleName();

    protected IGtdRecyclerViewMvpV<AGtdEntity> mvpView;
    protected IRecyclerViewMvpM<AGtdEntity> mvpModel;

    private List<BaseSectionEntity> gtdSectionEntityList;

    public GtdRecylerMvpPresenter(@NonNull IGtdRecyclerViewMvpV mvpView){
        this.mvpView = mvpView;
        mvpModel     = new GtdListModel();
        gtdSectionEntityList = Lists.newArrayList();
    }

    @Override
    public void loadAllData(){
        mvpView.mapBean(mvpModel.loadAllEntity()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new GtdRecylerInitSafeSubscriber());
    }

    @Override
    public void addData(@NonNull Object obj) {
        String entityId = (String)obj;
        mvpModel.loadEntity(Flowable.just(entityId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new GtdRecylerAddDataSafeSubscriber());
    }

    @Override
    public void onItemClick(BaseSectionEntity entity) {
        GtdSectionEntity mySectionEntity = (GtdSectionEntity)entity;
        Logcat.i(TAG, "onItemClick : " + mySectionEntity.toString());
        if(mySectionEntity.getGtdType() == GtdType.INBOX){
            if(!mySectionEntity.isHeader)
                mvpView.toStartGtdActivity(mySectionEntity.t.getId(), GtdType.INBOX);
            else
                mvpView.onHeadClick(mySectionEntity);
        }/*else
            Toast.makeText(GtdFragment.this.getActivity(), "this is item", Toast.LENGTH_LONG).show();*///todo
    }

    class GtdRecylerInitSafeSubscriber extends MySafeSubscriber<BaseSectionEntity>{
        List<GtdType> sectionList = new ArrayList<GtdType>();
        @Override
        public void onSubscribe(Subscription s) {
            super.onSubscribe(s);
            Logcat.i(TAG,"loadAllData onSubscribe");
            mvpView.onRecyclerViewInitStart();
        }

        @Override
        public void onNext(BaseSectionEntity gtdSectionEntity) {
            super.onNext(gtdSectionEntity);
            Logcat.i(TAG,"loadAllData onNext");
            GtdType valueGtdType = ((GtdSectionEntity)gtdSectionEntity).getGtdType();
            if(!sectionList.contains(valueGtdType))
                sectionList.add(valueGtdType);
            gtdSectionEntityList.add(gtdSectionEntity);
        }

        @Override
        public void onError(Throwable t) {
            Logcat.i(TAG,"loadAllData onError : " + t.getMessage());
            mvpView.onRecyclerViewInitErr();
        }

        @Override
        public void onComplete() {
            Logcat.i(TAG,"loadAllData onComplete");
            mvpView.onRecyclerViewInitEnd();
            if(!sectionList.contains(GtdType.INBOX))
                gtdSectionEntityList.add(new GtdSectionEntity(true,GtdType.INBOX,false));
            mvpView.initRecyclerView(gtdSectionEntityList);
        }
    }

    class GtdRecylerAddDataSafeSubscriber extends MySafeSubscriber<AGtdEntity>{
        @Override
        public void onSubscribe(Subscription s) {
            super.onSubscribe(s);
        }

        @Override
        public void onNext(AGtdEntity gtdEntity) {
            super.onNext(gtdEntity);
            GtdSectionEntity gtdSectionEntity = new GtdSectionEntity(gtdEntity);
            gtdSectionEntityList.add(gtdSectionEntity);
            mvpView.resetRecyclerView(gtdSectionEntityList);
        }

        @Override
        public void onError(Throwable t) {
        }

        @Override
        public void onComplete() {
        }
    }
}
