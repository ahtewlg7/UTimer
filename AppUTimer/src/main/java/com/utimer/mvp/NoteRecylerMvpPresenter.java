package com.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.utimer.entity.NoteSectionEntity;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.GTD.NoteEntityFactory;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.mvp.INoteRecyclerViewMvpV;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpM;
import ahtewlg7.utimer.mvp.IRecyclerViewMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.view.BaseSectionEntity;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2018/3/8.
 */

public class NoteRecylerMvpPresenter implements IRecyclerViewMvpP<BaseSectionEntity>{
    public static final String TAG = NoteRecylerMvpPresenter.class.getSimpleName();

    protected INoteRecyclerViewMvpV<NoteEntity> mvpView;
    protected IRecyclerViewMvpM<NoteEntity> mvpModel;

    private List<String> noteEntityIdList;
    private List<BaseSectionEntity> noteSectionEntityList;

    public NoteRecylerMvpPresenter(@NonNull INoteRecyclerViewMvpV<NoteEntity> mvpView){
        this.mvpView = mvpView;
        mvpModel     = new NoteEntityFactory();
        noteSectionEntityList = Lists.newArrayList();
    }

    public void initNoteIdList(@NonNull List<String> noteEntityIdList){
        this.noteEntityIdList = noteEntityIdList;
    }


    @Override
    public void loadAllData(){
        mvpView.mapBean(mvpModel.loadEntity(Flowable.fromIterable(noteEntityIdList)))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<BaseSectionEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    Logcat.i(TAG,"loadAllData onSubscribe");
                    s.request(Long.MAX_VALUE);
                    mvpView.onRecyclerViewInitStart();
                }

                @Override
                public void onNext(BaseSectionEntity baseSectionEntity) {
                    Logcat.i(TAG,"loadAllData onNext");
                    noteSectionEntityList.add(baseSectionEntity);
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
                    mvpView.initRecyclerView(noteSectionEntityList);
                }
            });
    }

    @Override
    public void addData(@NonNull Object obj) {
        String entityId = (String)obj;
        mvpModel.loadEntity(Flowable.just(entityId))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NoteEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        Logcat.i(TAG,"loadAllData onSubscribe");
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(NoteEntity noteEntity) {
                        NoteSectionEntity noteSectionEntity = new NoteSectionEntity(noteEntity);
                        noteSectionEntityList.add(noteSectionEntity);
                        mvpView.resetRecyclerView(noteSectionEntityList);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onItemClick(BaseSectionEntity entity) {
        NoteSectionEntity mySectionEntity = (NoteSectionEntity)entity;
        Logcat.i(TAG, "onItemClick : " + mySectionEntity.toString());
        mvpView.toStartNoteActivity(mySectionEntity.t.getId());
    }
}
