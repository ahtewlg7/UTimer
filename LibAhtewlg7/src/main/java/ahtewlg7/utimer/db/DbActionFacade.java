package ahtewlg7.utimer.db;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.db.dao.ActionEntityDaoAction;
import ahtewlg7.utimer.db.dao.NoteEntityDaoAction;
import ahtewlg7.utimer.db.dao.ShortHandEntityDaoAction;
import ahtewlg7.utimer.db.entity.ActionEntityGdBean;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;
import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.gtd.un.GtdTaskEntity;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/8.
 */

public class DbActionFacade {
    /*******************************************note**************************************************/
    public Flowable<NoteEntity> loadAllNoteEntity() {
        return Flowable.fromIterable(NoteEntityDaoAction.getInstance().loadAll())
                .map(new Function<NoteEntityGdBean, NoteEntity>() {
                    @Override
                    public NoteEntity apply(NoteEntityGdBean entityGdBean) throws Exception {
                        return new NoteBuilder().setGbBean(entityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<NoteEntity>> getNoteEntityByRPath(@NonNull final Flowable<String> rPathRx) {
        return rPathRx.map(new Function<String, Optional<NoteEntity>>() {
            @Override
            public Optional<NoteEntity> apply(String rPath) throws Exception {
                return getNoteEntityByRPath(rPath);
            }
        }).subscribeOn(Schedulers.io());
    }
    public Optional<NoteEntity> getNoteEntityByRPath(@NonNull final String rPath) {
        Optional<NoteEntityGdBean> beanOptional = null;
        try{
            beanOptional = NoteEntityDaoAction.getInstance().queryByRPath(rPath);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(beanOptional == null || !beanOptional.isPresent())
            return Optional.absent();
        NoteEntity e = (NoteEntity)new NoteBuilder().setGbBean(beanOptional.get()).build();
        return Optional.of(e);
    }

    public Flowable<Boolean> deleteNoteEntity(@NonNull Flowable<NoteEntity> eventFlowable){
        return eventFlowable.map(new Function<NoteEntity, Boolean>() {
            @Override
            public Boolean apply(NoteEntity entityOptional) throws Exception {
                NoteEntityGdBean bean = mapNoteToGdBean(entityOptional);
                NoteEntityDaoAction.getInstance().delete(bean);
                return true;
            }
        });
    }

    public Flowable<Boolean> saveNoteEntity(Flowable<NoteEntity> eventFlowable) {
        return eventFlowable.map(new Function<NoteEntity, Boolean>() {
            @Override
            public Boolean apply(NoteEntity entityOptional) throws Exception {
                NoteEntityGdBean bean = mapNoteToGdBean(entityOptional);
                long index = NoteEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }

    /*******************************************Task**************************************************/
    public Flowable<Optional<GtdTaskEntity>> loadAllTaskEntity() {
        /*return Flowable.fromIterable(TaskEntityDaoAction.getInstance().loadAll())
                .map(new Function<TaskEntityGdBean, Optional<GtdTaskEntity>>() {
                    @Override
                    public Optional<GtdTaskEntity> apply(TaskEntityGdBean taskEntityGdBean) throws Exception {
                        return Optional.fromNullable(JSON.parseObject(taskEntityGdBean.getValue(), GtdTaskEntity.class));
                    }
                })
                .subscribeOn(Schedulers.io());*/
        return Flowable.empty();
    }
    public Flowable<Optional<GtdTaskEntity>> getTaskEntity(@NonNull Flowable<String> nameFlowable) {
        /*return nameFlowable.map(new Function<String, Optional<GtdTaskEntity>>() {
            @Override
            public Optional<GtdTaskEntity> apply(String name) throws Exception {
                Optional<TaskEntityGdBean> beanOptional = TaskEntityDaoAction.getInstance().queryByKey(name);
                if(beanOptional.isPresent())
                    return Optional.fromNullable(JSON.parseObject(beanOptional.get().getValue(), GtdTaskEntity.class));
                return Optional.absent();
            }
        });*/
        return Flowable.empty();
    }

    public Flowable<Boolean> deleteTaskEntity(@NonNull Flowable<Optional<GtdTaskEntity>> eventFlowable){
        /*return eventFlowable.map(new Function<Optional<GtdTaskEntity>, Boolean>() {
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
        });*/
        return Flowable.empty();
    }

    public Flowable<Boolean> saveTaskEntity(Flowable<GtdTaskEntity> eventFlowable) {
        /*return eventFlowable.map(new Function<GtdTaskEntity, Boolean>() {
            @Override
            public Boolean apply(GtdTaskEntity eventEntity) throws Exception {
                TaskEntityGdBean bean = mapTaskToGdBean(eventEntity);
                Logcat.i(TAG,"saveTaskEntity : " + bean.toString());
                long index = TaskEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });*/
        return Flowable.empty();
    }
    /*******************************************Action**************************************************/
    public Flowable<GtdActionEntity> loadAllActionEntity() {
        return Flowable.fromIterable(ActionEntityDaoAction.getInstance().loadAll())
                .map(new Function<ActionEntityGdBean, GtdActionEntity>() {
                    @Override
                    public GtdActionEntity apply(ActionEntityGdBean actionEntityGdBean) throws Exception {
                        return new GtdActionBuilder().setGbBean(actionEntityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<GtdActionEntity>> getUndoActionEntity(@NonNull Flowable<String> nameFlowable) {
        /*return nameFlowable.map(new Function<String, Optional<GtdActionEntity>>() {
            @Override
            public Optional<GtdActionEntity> apply(String name) throws Exception {
                Optional<IdKeyEntityBean> beanOptional = ActionEntityDaoAction.getInstance().queryByKey(name);
                if(beanOptional.isPresent())
                    return Optional.fromNullable(JSON.parseObject(beanOptional.get().getValue(), GtdActionEntity.class));
                return Optional.absent();
            }
        });*/
        return Flowable.empty();
    }

    public Flowable<Boolean> deleteActionEntity(@NonNull Flowable<GtdActionEntity> eventFlowable){
        return eventFlowable.map(new Function<GtdActionEntity, Boolean>() {
            @Override
            public Boolean apply(GtdActionEntity eventEntity) throws Exception {
                return ActionEntityDaoAction.getInstance().deleteByKey(eventEntity.getUuid());
            }
        });
    }

    public Flowable<Boolean> saveActionEntity(Flowable<GtdActionEntity> eventFlowable) {
        return eventFlowable.map(new Function<GtdActionEntity, Boolean>() {
            @Override
            public Boolean apply(GtdActionEntity eventEntity) throws Exception {
                ActionEntityGdBean bean = mapActionToGdBean(eventEntity);
                long index = ActionEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }
    /*******************************************ShortHand**************************************************/
    public Flowable<ShortHandEntity> loadAllShortHandEntity() {
        return Flowable.fromIterable(ShortHandEntityDaoAction.getInstance().loadAll())
                .map(new Function<ShortHandEntityGdBean, ShortHandEntity>() {
                    @Override
                    public ShortHandEntity apply(ShortHandEntityGdBean entityGdBean) throws Exception {
                        return new ShortHandBuilder().setGbBean(entityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<ShortHandEntity>> getShortHandEntityByRPath(@NonNull final Flowable<String> rPathRx) {
        return rPathRx.map(new Function<String, Optional<ShortHandEntity>>() {
            @Override
            public Optional<ShortHandEntity> apply(String rPath) throws Exception {
                return getShortHandEntityByRPath(rPath);
            }
        }).subscribeOn(Schedulers.io());
    }
    public Optional<ShortHandEntity> getShortHandEntityByRPath(@NonNull final String rPath) {
        Optional<ShortHandEntityGdBean> beanOptional = null;
        try{
            beanOptional = ShortHandEntityDaoAction.getInstance().queryByRPath(rPath);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(beanOptional == null || !beanOptional.isPresent())
            return Optional.absent();
        ShortHandEntity e = (ShortHandEntity)new ShortHandBuilder().setGbBean(beanOptional.get()).build();
        return Optional.of(e);
    }

    public Flowable<Boolean> deleteShortHandEntity(@NonNull Flowable<ShortHandEntity> eventFlowable){
        return eventFlowable.map(new Function<ShortHandEntity, Boolean>() {
                    @Override
                    public Boolean apply(ShortHandEntity entity) throws Exception {
                        return ShortHandEntityDaoAction.getInstance().deleteByKey(entity.getUuid());
                    }
                });
    }

    public Flowable<Boolean> saveShortHandEntity(Flowable<ShortHandEntity> eventFlowable) {
        return eventFlowable.map(new Function<ShortHandEntity, Boolean>() {
            @Override
            public Boolean apply(ShortHandEntity entityOptional) throws Exception {
                ShortHandEntityGdBean bean = mapShorthandToGdBean(entityOptional);
                long index = ShortHandEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }

    /***********************************************************************************************/
    private ActionEntityGdBean mapActionToGdBean(@NonNull GtdActionEntity entity){
        ActionEntityGdBean bean = new ActionEntityGdBean();
        bean.setUuid(entity.getUuid());
        bean.setTitle(entity.getTitle());
        bean.setActionState(entity.getActionState());
        if(entity.getWarningTimeList() != null)
            bean.setWarningTimeList(entity.getWarningTimeList());
        if(entity.getDetail().isPresent())
            bean.setDetail(entity.getDetail().get());

        if(entity.getW5h2Entity() != null && entity.getW5h2Entity().getWhat() != null)
            bean.setW5h2What(entity.getW5h2Entity().getWhat());
        if(entity.getW5h2Entity() != null && entity.getW5h2Entity().getWhen() != null)
            bean.setW5h2When(entity.getW5h2Entity().getWhen());
        if(entity.getW5h2Entity() != null && entity.getW5h2Entity().getHowMuch() != null)
            bean.setW5h2HowMuch(entity.getW5h2Entity().getHowMuch());
        return bean;
    }
    private ShortHandEntityGdBean mapShorthandToGdBean(@NonNull ShortHandEntity entity){
        ShortHandEntityGdBean bean = new ShortHandEntityGdBean();
        bean.setUuid(entity.getUuid());
        bean.setTitle(entity.getTitle());
        if(entity.getDetail().isPresent())
            bean.setDetail(entity.getDetail().get());
        bean.setCreateTime(entity.getCreateTime());
        bean.setLastAccessTime(entity.getLastAccessTime());
        if(entity.getAttachFileRPath().isPresent())
            bean.setAttachFileRPath(entity.getAttachFileRPath().get());
        return bean;
    }
    private NoteEntityGdBean mapNoteToGdBean(@NonNull NoteEntity entity){
        NoteEntityGdBean bean = new NoteEntityGdBean();
        bean.setUuid(entity.getUuid());
        bean.setTitle(entity.getTitle());
        if(entity.getDetail().isPresent())
            bean.setDetail(entity.getDetail().get());
        bean.setCreateTime(entity.getCreateTime());
        bean.setLastAccessTime(entity.getLastAccessTime());
        if(entity.getAttachFileRPath().isPresent())
            bean.setAttachFileRPath(entity.getAttachFileRPath().get());
        return bean;
    }

    /*private TaskEntityGdBean mapTaskToGdBean(@NonNull GtdTaskEntity entity){
        TaskEntityGdBean gtdEventEntityGdBean = new TaskEntityGdBean();
        gtdEventEntityGdBean.setTitle(entity.getTitle());
        gtdEventEntityGdBean.setActive(entity.isActive());
        gtdEventEntityGdBean.setValue(entity.toJson());
        return gtdEventEntityGdBean;
    }
    */
}
