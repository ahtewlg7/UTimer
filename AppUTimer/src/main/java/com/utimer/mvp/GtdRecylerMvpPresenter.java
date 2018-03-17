package com.utimer.mvp;

import android.support.annotation.NonNull;

import com.utimer.entity.GtdSectionEntity;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.List;

import ahtewlg7.utimer.GTD.GtdEntityFactory;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpP;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpV;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.view.BaseSectionEntity;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/3/8.
 */

public class GtdRecylerMvpPresenter implements IRecyclerViewMvpP<BaseSectionEntity>{
    public static final String TAG = GtdRecylerMvpPresenter.class.getSimpleName();

    protected IRecyclerViewMvpV<AGtdEntity> mvpView;
    protected IRecyclerViewMvpM<AGtdEntity> mvpModel;

    private List<BaseSectionEntity> gtdSectionEntityList;

    public GtdRecylerMvpPresenter(@NonNull IRecyclerViewMvpV mvpView){
        this.mvpView = mvpView;
        mvpModel     = new GtdEntityFactory();
    }

    @Override
    public void loadAllData(){
        final List<GtdType> sectionList = new ArrayList<GtdType>();
        mvpView.mapBean(mvpModel.loadAll()).observeOn(AndroidSchedulers.mainThread())
            .subscribe(new FlowableSubscriber<BaseSectionEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    Logcat.i(TAG,"loadAllData onSubscribe");
                }

                @Override
                public void onNext(BaseSectionEntity gtdSectionEntity) {
                    Logcat.i(TAG,"loadAllData onNext");
                    GtdType valueGtdType = ((GtdSectionEntity)gtdSectionEntity).getGtdType();
                    if(!sectionList.contains(valueGtdType))
                        sectionList.add(valueGtdType);
                    gtdSectionEntityList.add(gtdSectionEntity);
                }

                @Override
                public void onError(Throwable t) {
                    Logcat.i(TAG,"loadAllData onError : " + t.getMessage());
                }

                @Override
                public void onComplete() {
                    Logcat.i(TAG,"loadAllData onComplete");
                    if(!sectionList.contains(GtdType.INBOX))
                        gtdSectionEntityList.add(new GtdSectionEntity(true,GtdType.INBOX,false));
                    mvpView.initView(gtdSectionEntityList);
                }
            });
    }

    @Override
    public void addData(@NonNull Object obj) {
        String entityId = (String)obj;
        mvpModel.getEntity(Observable.just(entityId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AGtdEntity>() {
                    @Override
                    public void accept(AGtdEntity gtdEntity) throws Exception {
                        GtdSectionEntity gtdSectionEntity = new GtdSectionEntity(gtdEntity);
                        gtdSectionEntityList.add(gtdSectionEntity);
                        mvpView.resetView(gtdSectionEntityList);
                    }
                });
    }

    @Override
    public void onItemClick(BaseSectionEntity entity) {
        GtdSectionEntity mySectionEntity = (GtdSectionEntity)entity;
        Logcat.i(TAG, "onItemClick : " + mySectionEntity.toString());
        String noteEntityId = null;
        String gtdEntityId  = null;
        if(mySectionEntity.getGtdType() == GtdType.INBOX){
            if(!mySectionEntity.isHeader){
                noteEntityId = ((GtdInboxEntity)(mySectionEntity.t)).getNoteEntityList().get(0);//todo
                gtdEntityId  = mySectionEntity.t.getId();
            }
            mvpView.toStartNoteActivity(gtdEntityId, noteEntityId);
        }/*else
            Toast.makeText(GtdFragment.this.getActivity(), "this is item", Toast.LENGTH_LONG).show();*///todo
    }
}
