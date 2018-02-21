package com.utimer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.utimer.R;
import com.utimer.view.recyclerview.GtdSectionRecylerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import ahtewlg7.utimer.GTD.GtdEntityFactory;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/24.
 */

public class GtdFragment extends AFunctionFragement {
    public static final String TAG = GtdFragment.class.getSimpleName();

    public static final String EXTRA_KEY_GTD_ID  = MyRInfo.getStringByID(R.string.extra_gtd_id);
    public static final String EXTRA_KEY_NOTE_ID = MyRInfo.getStringByID(R.string.extra_note_id);

    private String noteEntityId, gtdEntityId;
    private GtdEntityFactory gtdEntityFactory;
    private List<GtdSectionRecylerView.GtdSectionEntity> gtdSectionEntityList;

    @BindView(R.id.fragment_note_recyclerview)
    GtdSectionRecylerView sectionRecylerView;

    private SectionItemClickListener sectionItemClickListener;
    private SectionItemChildClickListener sectionItemChildClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gtdSectionEntityList            = new ArrayList<GtdSectionRecylerView.GtdSectionEntity>();
        gtdEntityFactory                = new GtdEntityFactory();
        sectionItemClickListener        = new SectionItemClickListener();
        sectionItemChildClickListener   = new SectionItemChildClickListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        toInitAdapter();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateView(data);
    }

    @Override
    @NonNull
    public String getIndicateTitle() {
        return "Gtd";
    }

    @Override
    public int getIndicateIconRid() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_note;
    }

    private void toInitAdapter(){
        final List<GtdType> sectionList = new ArrayList<GtdType>();
        loadAllGtd().observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GtdSectionRecylerView.GtdSectionEntity>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logcat.i(TAG,"toInitAdapter onSubscribe");
                    }

                    @Override
                    public void onNext(GtdSectionRecylerView.GtdSectionEntity value) {
                        Logcat.i(TAG,"toInitAdapter onNext");
                        GtdType valueGtdType = value.getGtdType();
                        if(!sectionList.contains(valueGtdType))
                            sectionList.add(valueGtdType);
                        gtdSectionEntityList.add(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logcat.i(TAG,"toInitAdapter onError : " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Logcat.i(TAG,"toInitAdapter onComplete");
                        if(!sectionList.contains(GtdType.INBOX))
                            gtdSectionEntityList.add(new GtdSectionRecylerView.GtdSectionEntity(true,GtdType.INBOX,false));
                        sectionRecylerView.init(GtdFragment.this.getActivity(), gtdSectionEntityList, sectionItemClickListener , sectionItemChildClickListener);
                    }
                });
    }

    private Observable<GtdSectionRecylerView.GtdSectionEntity> loadAllGtd(){
       return gtdEntityFactory.loadAll().groupBy(new Function<AGtdEntity, GtdType>() {
                    @Override
                    public GtdType apply(AGtdEntity gtdEntityGdBean) throws Exception {
                        return gtdEntityGdBean.getTaskType();
                    }
                })
               .flatMap(new Function<GroupedObservable<GtdType, AGtdEntity>, ObservableSource<GtdSectionRecylerView.GtdSectionEntity>>() {
                   @Override
                   public ObservableSource<GtdSectionRecylerView.GtdSectionEntity> apply(GroupedObservable<GtdType, AGtdEntity> groupedObservable) throws Exception {
                        return groupedObservable.map(new Function<AGtdEntity, GtdSectionRecylerView.GtdSectionEntity>() {
                            @Override
                            public GtdSectionRecylerView.GtdSectionEntity apply(AGtdEntity gtdEntity) throws Exception {
                                return new GtdSectionRecylerView.GtdSectionEntity(gtdEntity);
                            }
                        })
                        .startWith(new GtdSectionRecylerView.GtdSectionEntity(true,groupedObservable.getKey(),true));
                   }
               })
               .subscribeOn(Schedulers.io());
    }
    private void startNoteActivity(String gtdEntityId, String noteEntityId){
        Intent intent = new Intent(GtdFragment.this.getActivity(), MdEditorActivity.class);
        if(!TextUtils.isEmpty(gtdEntityId))
            intent.putExtra(MdEditorActivity.EXTRA_KEY_GTD_ID, gtdEntityId);
        if(!TextUtils.isEmpty(noteEntityId))
            intent.putExtra(MdEditorActivity.EXTRA_KEY_NOTE_ID, noteEntityId);
        startActivityForResult(intent, MdEditorActivity.ACTIVITY_START_RESULT);
    }
    private void updateView(final @NonNull Intent intent){
        Observable<String> intentObservale = Observable.defer(new Callable<ObservableSource<? extends String>>() {
                    @Override
                    public ObservableSource<? extends String> call() throws Exception {
                        return Observable.just(intent.getStringExtra(EXTRA_KEY_GTD_ID));
                    }
                }).filter(new Predicate<String>() {
                    @Override
                    public boolean test(String entityId) throws Exception {
                        Logcat.i(TAG,"updateView : gtdEntityId = " + gtdEntityId);
                        return !TextUtils.isEmpty(entityId) && !entityId.equalsIgnoreCase(gtdEntityId);
                    }
                });
        gtdEntityFactory.getEntity(intentObservale)
                .map(new Function<AGtdEntity, GtdSectionRecylerView.GtdSectionEntity>() {
                    @Override
                    public GtdSectionRecylerView.GtdSectionEntity apply(AGtdEntity gtdEntity) throws Exception {
                        return new GtdSectionRecylerView.GtdSectionEntity(gtdEntity);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GtdSectionRecylerView.GtdSectionEntity>() {
                    @Override
                    public void accept(GtdSectionRecylerView.GtdSectionEntity gtdSectionEntity) throws Exception {
                        gtdSectionEntityList.add(gtdSectionEntity);
                        sectionRecylerView.resetNewData(gtdSectionEntityList);
                    }
                });
    }

    class SectionItemClickListener implements BaseQuickAdapter.OnItemClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            GtdSectionRecylerView.GtdSectionEntity mySectionEntity = gtdSectionEntityList.get(position);
            Logcat.i(TAG, "onItemClick : " + mySectionEntity.toString());
            if(mySectionEntity.getGtdType() == GtdType.INBOX){
                if(mySectionEntity.isHeader){
                    noteEntityId = null;
                    gtdEntityId  = null;
                }else{
                    noteEntityId = ((GtdInboxEntity)(mySectionEntity.t)).getNoteEntityList().get(0);//todo
                    gtdEntityId  = mySectionEntity.t.getId();
                }
                startNoteActivity(gtdEntityId, noteEntityId);
            }else
                Toast.makeText(GtdFragment.this.getActivity(), "this is item", Toast.LENGTH_LONG).show();
        }
    }
    class SectionItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            Logcat.i(TAG,"onItemChildClick : position = " + position);
        }
    }
}
