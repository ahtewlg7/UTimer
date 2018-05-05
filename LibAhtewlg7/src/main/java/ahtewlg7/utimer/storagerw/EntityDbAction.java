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
import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.DbErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
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

    public Flowable<Optional<INoteEntity>> loadAllNoteEntity() {
        return Flowable.fromIterable(NoteEntityDaoAction.getInstance().loadAll())
                .map(new Function<NoteEntityGdBean, Optional<INoteEntity>>() {
                    @Override
                    public Optional<INoteEntity> apply(NoteEntityGdBean noteEntityGdBean) throws Exception {
                        INoteEntity noteEntity = JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
                        return Optional.fromNullable(noteEntity);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Optional<INoteEntity>> getNoteEntity(@NonNull Flowable<String> idFlowable) {
        return idFlowable.map(new Function<String, Optional<INoteEntity>>() {
            @Override
            public Optional<INoteEntity> apply(String id) throws Exception {
                if(TextUtils.isEmpty(id))
                    return Optional.absent();
                NoteEntityGdBean noteEntityGdBean = NoteEntityDaoAction.getInstance().queryById(id);
                if(noteEntityGdBean == null)
                    return Optional.absent();
                INoteEntity noteEntity = JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
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

    public boolean saveEntity(INoteEntity entity) {
        if(entity == null)
            return false;
        Logcat.i(TAG,"save noteEntity");
        NoteEntityGdBean noteEntityGdBean = new NoteEntityGdBean();
        noteEntityGdBean.setKey(entity.getId());
        noteEntityGdBean.setValue(JSON.toJSONString(entity));
        NoteEntityDaoAction.getInstance().insert(noteEntityGdBean);
        return true;
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
        GtdEntityDaoAction.getInstance().insert(gtdEntityGdBean);
        return true;
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
