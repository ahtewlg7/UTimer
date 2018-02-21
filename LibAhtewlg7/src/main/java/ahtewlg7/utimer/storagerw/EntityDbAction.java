package ahtewlg7.utimer.storagerw;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

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
import ahtewlg7.utimer.exception.DataBaseException;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/8.
 */

public class EntityDbAction implements IEntityWAction, IEntityRAction{
    public static final String TAG = EntityDbAction.class.getSimpleName();

    @Override
    public Observable<NoteEntity> loadNoteEntity() {
        return Observable.fromIterable(NoteEntityDaoAction.getInstance().loadAll())
                .map(new Function<NoteEntityGdBean, NoteEntity>() {
                    @Override
                    public NoteEntity apply(NoteEntityGdBean noteEntityGdBean) throws Exception {
                        return JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<NoteEntity> getNoteEntity(@NonNull Observable<String> idObservable) {
        return idObservable.map(new Function<String, NoteEntity>() {
            @Override
            public NoteEntity apply(String id) throws Exception {
                if(TextUtils.isEmpty(id))
                 throw new DataBaseException(DbErrCode.ERR_DB_ID_EMPTY);
                NoteEntityGdBean noteEntityGdBean = NoteEntityDaoAction.getInstance().queryById(id);
                if(noteEntityGdBean == null)
                    throw new DataBaseException(DbErrCode.ERR_DB_BEAN_NULL);
                return JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
            }
        });
    }


    @Override
    public Observable<AGtdEntity> loadGtdEntity() {
        return Observable.fromIterable(GtdEntityDaoAction.getInstance().loadAll())
                .groupBy(new Function<GtdEntityGdBean, GtdType>() {
                    @Override
                    public GtdType apply(GtdEntityGdBean gtdEntityGdBean) throws Exception {
                        return gtdEntityGdBean.getGtdType();
                    }
                })
                .flatMap(new Function<GroupedObservable<GtdType, GtdEntityGdBean>, ObservableSource<AGtdEntity>>() {
                    @Override
                    public ObservableSource<AGtdEntity> apply(GroupedObservable<GtdType, GtdEntityGdBean> groupedObservable) throws Exception {
                        return groupedObservable.map(new Function<GtdEntityGdBean, AGtdEntity>() {
                            @Override
                            public AGtdEntity apply(GtdEntityGdBean gtdEntityGdBean) throws Exception {
                                return parseGtdBean(gtdEntityGdBean);
                            }
                        });
                    }
                });
    }


    @Override
    public Observable<AGtdEntity> getGtdEntity(Observable<String> idObservable) {
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

    @Override
    public boolean saveEntity(NoteEntity entity) {
        if(entity == null)
            return false;
        Logcat.i(TAG,"save noteEntity");
        NoteEntityGdBean noteEntityGdBean = new NoteEntityGdBean();
        noteEntityGdBean.setKey(entity.getId());
        noteEntityGdBean.setValue(JSON.toJSONString(entity));
        NoteEntityDaoAction.getInstance().insert(noteEntityGdBean);
        return true;
    }

    @Override
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
