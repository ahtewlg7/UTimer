package ahtewlg7.utimer.db;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;

import ahtewlg7.utimer.db.dao.ActionEntityDaoAction;
import ahtewlg7.utimer.db.dao.NoteEntityDaoAction;
import ahtewlg7.utimer.db.dao.ShortHandEntityDaoAction;
import ahtewlg7.utimer.db.dao.TaskEntityDaoAction;
import ahtewlg7.utimer.db.entity.ActionEntityGdBean;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;
import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.db.entity.TaskEntityGdBean;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.gtd.GtdTaskEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.enumtype.UnLoadType;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/8.
 */

public class DbActionFacade {
    public static final String TAG = DbActionFacade.class.getSimpleName();


    /*******************************************note**************************************************/
    public Flowable<Optional<NoteEntity>> loadAllNoteEntity() {
        return Flowable.fromIterable(NoteEntityDaoAction.getInstance().loadAll())
                .map(new Function<NoteEntityGdBean, Optional<NoteEntity>>() {
                    @Override
                    public Optional<NoteEntity> apply(NoteEntityGdBean noteEntityGdBean) throws Exception {
                        NoteEntity noteEntity = JSON.parseObject(noteEntityGdBean.getValue(),NoteEntity.class);
                        noteEntity.setLoadType(UnLoadType.DB);
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
                Optional<NoteEntityGdBean> noteEntityGdBean = NoteEntityDaoAction.getInstance().queryByKey(idOptional.get());
                if(!noteEntityGdBean.isPresent())
                    return Optional.absent();
                NoteEntity noteEntity = JSON.parseObject(noteEntityGdBean.get().getValue(),NoteEntity.class);
                noteEntity.setLoadType(UnLoadType.DB);
                Logcat.i(TAG,"getNoteEntity : "+ noteEntity.toString());
                return Optional.fromNullable(noteEntity);
            }
        });
    }
    public Flowable<Boolean> deleteNoteEntity(@NonNull Flowable<Optional<NoteEntity>> noteFlowable){
        return noteFlowable.map(new Function<Optional<NoteEntity>, Boolean>() {
            @Override
            public Boolean apply(Optional<NoteEntity> noteEntityOptional) throws Exception {
                if(noteEntityOptional.isPresent()){
                    NoteEntityGdBean noteEntityGdBean = mapNoteToGdBean(noteEntityOptional.get());
                    Logcat.i(TAG,"deleteNoteEntity ： noteEntityGdBean = " + noteEntityGdBean.toString());
                    NoteEntityDaoAction.getInstance().delete(noteEntityGdBean);
                    return true;
                }
                return false;
            }
        });
    }

    public Flowable<Boolean> saveNoteEntity(Flowable<NoteEntity> noteEntityFlowable) {
        return noteEntityFlowable.map(new Function<NoteEntity, Boolean>() {
            @Override
            public Boolean apply(NoteEntity noteEntity) throws Exception {
                NoteEntityGdBean noteEntityGdBean = mapNoteToGdBean(noteEntity);
                Logcat.i(TAG,"saveEntity : " + noteEntityGdBean.toString());
                long index = NoteEntityDaoAction.getInstance().insert(noteEntityGdBean);
                return index >= 0;
            }
        });
    }

    /*******************************************Task**************************************************/
    public Flowable<Optional<GtdTaskEntity>> loadAllTaskEntity() {
        return Flowable.fromIterable(TaskEntityDaoAction.getInstance().loadAll())
                .map(new Function<TaskEntityGdBean, Optional<GtdTaskEntity>>() {
                    @Override
                    public Optional<GtdTaskEntity> apply(TaskEntityGdBean taskEntityGdBean) throws Exception {
                        return Optional.fromNullable(JSON.parseObject(taskEntityGdBean.getValue(), GtdTaskEntity.class));
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<GtdTaskEntity>> getTaskEntity(@NonNull Flowable<String> nameFlowable) {
        return nameFlowable.map(new Function<String, Optional<GtdTaskEntity>>() {
            @Override
            public Optional<GtdTaskEntity> apply(String name) throws Exception {
                Optional<TaskEntityGdBean> beanOptional = TaskEntityDaoAction.getInstance().queryByKey(name);
                if(beanOptional.isPresent())
                    return Optional.fromNullable(JSON.parseObject(beanOptional.get().getValue(), GtdTaskEntity.class));
                return Optional.absent();
            }
        });
    }

    public Flowable<Boolean> deleteTaskEntity(@NonNull Flowable<Optional<GtdTaskEntity>> eventFlowable){
        return eventFlowable.map(new Function<Optional<GtdTaskEntity>, Boolean>() {
            @Override
            public Boolean apply(Optional<GtdTaskEntity> gtdEventEntityOptional) throws Exception {
                if(gtdEventEntityOptional.isPresent()){
                    TaskEntityGdBean bean = mapTaskToGdBean(gtdEventEntityOptional.get());
                    Logcat.i(TAG,"deleteTaskEntity ：" + bean.toString());
                    TaskEntityDaoAction.getInstance().delete(bean);
                    return true;
                }
                return false;
            }
        });
    }

    public Flowable<Boolean> saveTaskEntity(Flowable<GtdTaskEntity> eventFlowable) {
        return eventFlowable.map(new Function<GtdTaskEntity, Boolean>() {
            @Override
            public Boolean apply(GtdTaskEntity eventEntity) throws Exception {
                TaskEntityGdBean bean = mapTaskToGdBean(eventEntity);
                Logcat.i(TAG,"saveTaskEntity : " + bean.toString());
                long index = TaskEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }
    /*******************************************Action**************************************************/
    public Flowable<Optional<GtdActionEntity>> loadAllUndoActionEntity() {
        return Flowable.fromIterable(ActionEntityDaoAction.getInstance().loadAll())
                .map(new Function<ActionEntityGdBean, Optional<GtdActionEntity>>() {
                    @Override
                    public Optional<GtdActionEntity> apply(ActionEntityGdBean actionEntityGdBean) throws Exception {
                        return Optional.fromNullable(JSON.parseObject(actionEntityGdBean.getValue(), GtdActionEntity.class));
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<GtdActionEntity>> getUndoActionEntity(@NonNull Flowable<String> nameFlowable) {
        return nameFlowable.map(new Function<String, Optional<GtdActionEntity>>() {
            @Override
            public Optional<GtdActionEntity> apply(String name) throws Exception {
                Optional<ActionEntityGdBean> beanOptional = ActionEntityDaoAction.getInstance().queryByKey(name);
                if(beanOptional.isPresent())
                    return Optional.fromNullable(JSON.parseObject(beanOptional.get().getValue(), GtdActionEntity.class));
                return Optional.absent();
            }
        });
    }

    public Flowable<Boolean> deleteUndoctionEntity(@NonNull Flowable<Optional<GtdActionEntity>> eventFlowable){
        return eventFlowable.map(new Function<Optional<GtdActionEntity>, Boolean>() {
            @Override
            public Boolean apply(Optional<GtdActionEntity> entityOptional) throws Exception {
                if(entityOptional.isPresent()){
                    ActionEntityGdBean bean = mapActionToGdBean(entityOptional.get());
                    Logcat.i(TAG,"deleteActionEntity ：" + bean.toString());
                    ActionEntityDaoAction.getInstance().delete(bean);
                    return true;
                }
                return false;
            }
        });
    }

    public Flowable<Boolean> saveActionEntity(Flowable<GtdActionEntity> eventFlowable) {
        return eventFlowable.map(new Function<GtdActionEntity, Boolean>() {
            @Override
            public Boolean apply(GtdActionEntity eventEntity) throws Exception {
                ActionEntityGdBean bean = mapActionToGdBean(eventEntity);
                Logcat.i(TAG,"saveActionEntity : " + bean.toString());
                long index = ActionEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }
    /*******************************************ShortHand**************************************************/
    public Flowable<Optional<ShortHandEntity>> loadAllShortHandEntity() {
        return Flowable.fromIterable(ShortHandEntityDaoAction.getInstance().loadAll())
                .map(new Function<ShortHandEntityGdBean, Optional<ShortHandEntity>>() {
                    @Override
                    public Optional<ShortHandEntity> apply(ShortHandEntityGdBean entityGdBean) throws Exception {
                        return Optional.of(new ShortHandBuilder().setGbBean(entityGdBean).build());
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<ShortHandEntity>> getShortHandEntityByTitle(@NonNull final Flowable<Optional<String>> nameFlowable) {
        return nameFlowable.map(new Function<Optional<String>, Optional<ShortHandEntity>>() {
            @Override
            public Optional<ShortHandEntity> apply(Optional<String> nameOptional) throws Exception {
                if(!nameOptional.isPresent() || TextUtils.isEmpty(nameOptional.get()))
                    return Optional.absent();
                Optional<ShortHandEntityGdBean> beanOptional = ShortHandEntityDaoAction.getInstance().queryByKey(nameOptional.get());
                if(!beanOptional.isPresent())
                    return Optional.absent();
                return Optional.of(new ShortHandBuilder().setGbBean(beanOptional.get()).build());
            }
        });
    }

    public Flowable<Boolean> deleteShortHandEntity(@NonNull Flowable<Optional<ShortHandEntity>> eventFlowable){
        return eventFlowable.map(new Function<Optional<ShortHandEntity>, Boolean>() {
            @Override
            public Boolean apply(Optional<ShortHandEntity> entityOptional) throws Exception {
                if(entityOptional.isPresent()){
                    ShortHandEntityGdBean bean = mapShorthandToGdBean(entityOptional.get());
                    Logcat.i(TAG,"deleteInboxEntity ：" + bean.toString());
                    ShortHandEntityDaoAction.getInstance().delete(bean);
                    return true;
                }
                return false;
            }
        });
    }

    public Flowable<Boolean> saveShortHandEntity(Flowable<Optional<ShortHandEntity>> eventFlowable) {
        return eventFlowable.map(new Function<Optional<ShortHandEntity>, Boolean>() {
            @Override
            public Boolean apply(Optional<ShortHandEntity> entityOptional) throws Exception {
                if(entityOptional.isPresent()){
                    ShortHandEntityGdBean bean = mapShorthandToGdBean(entityOptional.get());
                    Logcat.i(TAG,"saveInboxEntity : " + bean.toString());
                    long index = ShortHandEntityDaoAction.getInstance().insert(bean);
                    return index >= 0;
                }
                return false;
            }
        });
    }

    /***********************************************************************************************/
    private NoteEntityGdBean mapNoteToGdBean(@NonNull NoteEntity entity){
        NoteEntityGdBean noteEntityGdBean = new NoteEntityGdBean();
        noteEntityGdBean.setKey(entity.getId());
        noteEntityGdBean.setValue(JSON.toJSONString(entity));
        return noteEntityGdBean;
    }

    private TaskEntityGdBean mapTaskToGdBean(@NonNull GtdTaskEntity entity){
        TaskEntityGdBean gtdEventEntityGdBean = new TaskEntityGdBean();
        gtdEventEntityGdBean.setTitle(entity.getTitle());
        gtdEventEntityGdBean.setActive(entity.isActive());
        gtdEventEntityGdBean.setValue(entity.toJson());
        return gtdEventEntityGdBean;
    }
    private ActionEntityGdBean mapActionToGdBean(@NonNull GtdActionEntity entity){
        ActionEntityGdBean bean = new ActionEntityGdBean();
        bean.setTitle(entity.getTitle());
        bean.setValue(entity.toJson());
        return bean;
    }
    private ShortHandEntityGdBean mapShorthandToGdBean(@NonNull ShortHandEntity entity){
        ShortHandEntityGdBean bean = new ShortHandEntityGdBean();
        bean.setTitle(entity.getTitle());
        bean.setIsActived(entity.isActived());
        bean.setCreateTime(entity.getCreateTime());
        bean.setLastAccessTime(entity.getLastAccessTime());
        bean.setLastModifyTime(entity.getLastModifyTime());
        return bean;
    }
}
