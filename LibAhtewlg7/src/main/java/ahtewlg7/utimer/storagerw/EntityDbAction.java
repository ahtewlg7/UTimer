package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;

import org.reactivestreams.Publisher;

import ahtewlg7.utimer.db.dao.GtdEntityDaoAction;
import ahtewlg7.utimer.db.dao.NoteEntityDaoAction;
import ahtewlg7.utimer.db.entity.GtdEntityGdBean;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.DbErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.LoadType;
import ahtewlg7.utimer.exception.DataBaseException;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/8.
 */

public class EntityDbAction{
    public static final String TAG = EntityDbAction.class.getSimpleName();

    public Flowable<Optional<NoteEntity>> loadAllNoteEntity() {
        return Flowable.fromIterable(NoteEntityDaoAction.getInstance().loadAll())
                .map(new Function<NoteEntityGdBean, Optional<NoteEntity>>() {
                    @Override
                    public Optional<NoteEntity> apply(NoteEntityGdBean noteEntityGdBean) throws Exception {
                        NoteEntity noteEntity = JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
                        noteEntity.setLoadType(LoadType.DB);
                        return Optional.fromNullable(noteEntity);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Optional<NoteEntity>> getNoteEntity(@NonNull Flowable<Optional<String>> idFlowable) {
        return idFlowable.map(new Function<Optional<String>, Optional<NoteEntity>>() {
            @Override
            public Optional<NoteEntity> apply(Optional<String> idOptional) throws Exception {
                if(!idOptional.isPresent() || TextUtils.isEmpty(idOptional.get()))
                    return Optional.absent();
                NoteEntityGdBean noteEntityGdBean = NoteEntityDaoAction.getInstance().queryById(idOptional.get());
                if(noteEntityGdBean == null)
                    return Optional.absent();
                NoteEntity noteEntity = JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
                noteEntity.setLoadType(LoadType.DB);
                return Optional.fromNullable(noteEntity);
            }
        });
    }

    public Flowable<AGtdEntity> loadGtdEntity() {
        return GtdEntityDaoAction.getInstance().loadAllRx()
                .groupBy(new Function<GtdEntityGdBean, GtdType>() {
                    @Override
                    public GtdType apply(GtdEntityGdBean gtdEntityGdBean) throws Exception {
                        Logcat.i(TAG,"loadGtdEntity groupBy = " + gtdEntityGdBean.toString());
                        return gtdEntityGdBean.getGtdType();
                    }
                })
                .flatMap(new Function<GroupedFlowable<GtdType, GtdEntityGdBean>, Publisher<AGtdEntity>>() {
                    @Override
                    public Publisher<AGtdEntity> apply(GroupedFlowable<GtdType, GtdEntityGdBean> groupedFlowable) throws Exception {
                        return groupedFlowable.map(new Function<GtdEntityGdBean, AGtdEntity>() {
                            @Override
                            public AGtdEntity apply(GtdEntityGdBean gtdEntityGdBean) throws Exception {
                                return parseGtdBean(gtdEntityGdBean);
                            }
                        });
                    }
                });
    }


    public Flowable<AGtdEntity> getGtdEntity(Flowable<String> idObservable) {
        return  idObservable.map(new Function<String, AGtdEntity>() {
            @Override
            public AGtdEntity apply(String id) throws Exception {
                if(TextUtils.isEmpty(id))
                    throw new DataBaseException(DbErrCode.ERR_DB_ID_EMPTY);
                GtdEntityGdBean gtdEntityGdBean = GtdEntityDaoAction.getInstance().queryById(id);
                return parseGtdBean(gtdEntityGdBean);
            }
        });
    }

    public boolean saveEntity(NoteEntity entity) {
        if(entity == null)
            return false;
        NoteEntityGdBean noteEntityGdBean = new NoteEntityGdBean();
        noteEntityGdBean.setKey(entity.getId());
        noteEntityGdBean.setValue(JSON.toJSONString(entity));
        Logcat.i(TAG,"saveEntity ： noteEntityGdBean = " + noteEntityGdBean.toString());
        long index = NoteEntityDaoAction.getInstance().insert(noteEntityGdBean);
        return index >= 0;
    }

    public boolean saveEntity(AGtdEntity entity) {
        if(entity == null)
            return false;
        Logcat.i(TAG,"saveEntity");
        GtdEntityGdBean gtdEntityGdBean = new GtdEntityGdBean();
        gtdEntityGdBean.setKey(entity.getId());
        gtdEntityGdBean.setGtdType(entity.getTaskType());
        gtdEntityGdBean.setLastAccessTime(entity.getLastModifyDateTime());
        gtdEntityGdBean.setValue(JSON.toJSONString(entity));
        long index = GtdEntityDaoAction.getInstance().insert(gtdEntityGdBean);
        return index >= 0;
    }
    
    private AGtdEntity parseGtdBean(@NonNull GtdEntityGdBean gtdEntityGdBean){
        AGtdEntity gtdEntity = null;
        Logcat.i(TAG,"parseGtdBean gtdEntityGdBean = " + gtdEntityGdBean.toString());
        switch(gtdEntityGdBean.getGtdType()){
            case INBOX:
                gtdEntity = JSON.parseObject(gtdEntityGdBean.getValue(), GtdInboxEntity.class);
                break;
            case PROJECT:
                gtdEntity = JSON.parseObject(gtdEntityGdBean.getValue(), GtdProjectEntity.class);
                break;
        }
        return gtdEntity;
    }
}
