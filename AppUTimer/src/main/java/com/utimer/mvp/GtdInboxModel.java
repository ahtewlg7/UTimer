package com.utimer.mvp;

import android.text.TextUtils;

import com.google.common.base.Optional;

import ahtewlg7.utimer.GTD.GtdEntityFactory;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.mvp.IGtdInboxMvpM;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;

public class GtdInboxModel implements IGtdInboxMvpM {
    public static final String TAG = GtdInboxModel.class.getSimpleName();

    private GtdEntityFactory gtdEntityFactory;

    public GtdInboxModel(){
        gtdEntityFactory = new GtdEntityFactory();
    }

    @Override
    public Flowable<Optional<GtdProjectEntity>> getGtdProject(String id) {
        if(TextUtils.isEmpty(id)){
            Optional<GtdProjectEntity> gtdProjectEntityOptional = Optional.absent();
            return Flowable.just(gtdProjectEntityOptional);
        }
        return gtdEntityFactory.loadEntity(Flowable.just(id)).filter(new Predicate<AGtdEntity>() {
            @Override
            public boolean test(AGtdEntity gtdEntity) throws Exception {
                return gtdEntity.getTaskType() == GtdType.PROJECT;
            }
        })
        .map(new io.reactivex.functions.Function<AGtdEntity, Optional<GtdProjectEntity>>() {
            @Override
            public Optional<GtdProjectEntity> apply(AGtdEntity gtdEntity) throws Exception {
                return Optional.of((GtdProjectEntity) gtdEntity);
            }
        });
    }

    @Override
    public boolean toProjectAnInbox(GtdProjectEntity gtdProjectEntity, GtdInboxEntity gtdInboxEntity) {
        if(gtdProjectEntity == null || !gtdProjectEntity.isValid()) {
            Logcat.i(TAG,"toProjectAnInbox : gtdProjectEntity is not valid");
            return false;
        }
        if(gtdInboxEntity == null || !gtdInboxEntity.isValid()) {
            Logcat.i(TAG,"toProjectAnInbox : gtdInboxEntity is not valid");
            return false;
        }

        Logcat.i(TAG,"toProjectAnInbox gtdProjectEntity = " + gtdProjectEntity.toString()
                + ", gtdInboxEntity = " + gtdInboxEntity.toString());
        gtdProjectEntity.addSubEntity(gtdInboxEntity);
        return true;
    }

    @Override
    public boolean toSaveProject(GtdProjectEntity gtdProjectEntity) {
        return gtdEntityFactory.saveGtdTaskEntity(gtdProjectEntity);
    }
}
