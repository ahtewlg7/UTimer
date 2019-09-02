package ahtewlg7.utimer.db;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.db.dao.DeedEntityDaoAction;
import ahtewlg7.utimer.db.dao.MaterialEntityDaoAction;
import ahtewlg7.utimer.db.dao.NoteEntityDaoAction;
import ahtewlg7.utimer.db.dao.ShortHandEntityDaoAction;
import ahtewlg7.utimer.db.entity.DeedEntityGdBean;
import ahtewlg7.utimer.db.entity.MaterialEntityGdBean;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;
import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedBuilder;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.gtd.MaterialBuilder;
import ahtewlg7.utimer.entity.gtd.MaterialEntity;
import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/8.
 */

public class DbActionFacade {
    /*******************************************note**************************************************/
    public Flowable<MaterialEntity> loadAllMaterialEntity() {
        return Flowable.fromIterable(MaterialEntityDaoAction.getInstance().loadAll())
                .map(new Function<MaterialEntityGdBean, MaterialEntity>() {
                    @Override
                    public MaterialEntity apply(MaterialEntityGdBean entityGdBean) throws Exception {
                        return new MaterialBuilder().setGbBean(entityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Flowable<Optional<MaterialEntity>> getMaterialEntityByAbsPath(@NonNull final Flowable<String> rPathRx) {
        return rPathRx.map(new Function<String, Optional<MaterialEntity>>() {
            @Override
            public Optional<MaterialEntity> apply(String absPath) throws Exception {
                Optional<MaterialEntityGdBean> beanOptional = null;
                try{
                    beanOptional = MaterialEntityDaoAction.getInstance().queryByAbsFilePath(absPath);
                }catch (Exception e){
                    e.printStackTrace();
                }
                if(beanOptional == null || !beanOptional.isPresent())
                    return Optional.absent();
                MaterialEntity e = (MaterialEntity)new MaterialBuilder().setGbBean(beanOptional.get()).build();
                return Optional.of(e);
            }
        }).subscribeOn(Schedulers.io());
    }
    public Flowable<Boolean> deleteMaterialEntity(@NonNull Flowable<MaterialEntity> eventFlowable){
        return eventFlowable.map(new Function<MaterialEntity, Boolean>() {
            @Override
            public Boolean apply(MaterialEntity entityOptional) throws Exception {
                MaterialEntityGdBean bean = mapToGdBean(entityOptional);
                MaterialEntityDaoAction.getInstance().delete(bean);
                return true;
            }
        });
    }

    public Flowable<Boolean> saveMaterialEntity(Flowable<NoteEntity> eventFlowable) {
        return eventFlowable.map(new Function<NoteEntity, Boolean>() {
            @Override
            public Boolean apply(NoteEntity entityOptional) throws Exception {
                NoteEntityGdBean bean = mapToGdBean(entityOptional);
                long index = NoteEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }
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
    public Flowable<Optional<NoteEntity>> getEntityByAbsPath(@NonNull final Flowable<String> rPathRx) {
        return rPathRx.map(new Function<String, Optional<NoteEntity>>() {
            @Override
            public Optional<NoteEntity> apply(String rPath) throws Exception {
                return getEntityByAbsPath(rPath);
            }
        }).subscribeOn(Schedulers.io());
    }
    public Optional<NoteEntity> getEntityByAbsPath(@NonNull final String rPath) {
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
                NoteEntityGdBean bean = mapToGdBean(entityOptional);
                NoteEntityDaoAction.getInstance().delete(bean);
                return true;
            }
        });
    }

    public Flowable<Boolean> saveNoteEntity(Flowable<NoteEntity> eventFlowable) {
        return eventFlowable.map(new Function<NoteEntity, Boolean>() {
            @Override
            public Boolean apply(NoteEntity entityOptional) throws Exception {
                NoteEntityGdBean bean = mapToGdBean(entityOptional);
                long index = NoteEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }

    /*******************************************Deed**************************************************/
    public Flowable<GtdDeedEntity> loadAllDeedEntity() {
        return Flowable.fromIterable(DeedEntityDaoAction.getInstance().loadAll())
                .doOnNext(new Consumer<DeedEntityGdBean>() {
                    @Override
                    public void accept(DeedEntityGdBean deedEntityGdBean) throws Exception {
                        boolean ifAutoFill = false;
                        if(deedEntityGdBean.getCreateTime() == null) {
                            deedEntityGdBean.setCreateTime(DateTime.now());
                            ifAutoFill = true;
                        }
                        if(deedEntityGdBean.getStartTime() == null &&
                            (deedEntityGdBean.getActionState() == DeedState.DONE || deedEntityGdBean.getActionState() == DeedState.TRASH)){
                            deedEntityGdBean.setStartTime(DateTime.now());
                            ifAutoFill = true;
                        }
                        if(deedEntityGdBean.getEndTime() == null  &&
                            (deedEntityGdBean.getActionState() == DeedState.DONE || deedEntityGdBean.getActionState() == DeedState.TRASH)){
                            deedEntityGdBean.setEndTime(DateTime.now());
                            ifAutoFill = true;
                        }
                        if(ifAutoFill)
                            DeedEntityDaoAction.getInstance().insert(deedEntityGdBean);
                    }
                })
                .map(new Function<DeedEntityGdBean, GtdDeedEntity>() {
                    @Override
                    public GtdDeedEntity apply(DeedEntityGdBean actionEntityGdBean) throws Exception {
                        return new GtdDeedBuilder().setGbBean(actionEntityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    public Flowable<Boolean> deleteDeedEntity(@NonNull Flowable<GtdDeedEntity> eventFlowable){
        return eventFlowable.map(new Function<GtdDeedEntity, Boolean>() {
            @Override
            public Boolean apply(GtdDeedEntity eventEntity) throws Exception {
                return DeedEntityDaoAction.getInstance().deleteByKey(eventEntity.getUuid());
            }
        });
    }

    public Flowable<Boolean> saveDeedEntity(Flowable<GtdDeedEntity> eventFlowable) {
        return eventFlowable.map(new Function<GtdDeedEntity, Boolean>() {
            @Override
            public Boolean apply(GtdDeedEntity eventEntity) throws Exception {
                DeedEntityGdBean bean = mapToGdBean(eventEntity);
                long index = DeedEntityDaoAction.getInstance().insert(bean);
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
                ShortHandEntityGdBean bean = mapToGdBean(entityOptional);
                long index = ShortHandEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }

    /***********************************************************************************************/
    private DeedEntityGdBean mapToGdBean(@NonNull GtdDeedEntity entity){
        DeedEntityGdBean bean = new DeedEntityGdBean();
        bean.setUuid(entity.getUuid());
        bean.setTitle(entity.getTitle());
        bean.setActionState(entity.getDeedState());
        if(entity.getCreateTime() != null)
            bean.setCreateTime(entity.getCreateTime());
        else
            bean.setCreateTime(DateTime.now());
        if(entity.getStartTime() != null)
            bean.setStartTime(entity.getStartTime());
        else if(entity.getDeedState() == DeedState.DONE || entity.getDeedState() == DeedState.TRASH)
            bean.setStartTime(DateTime.now());
        if(entity.getEndTime() != null)
            bean.setEndTime(entity.getEndTime());
        else if(entity.getDeedState() == DeedState.DONE || entity.getDeedState() == DeedState.TRASH)
            bean.setEndTime(DateTime.now());
        if(entity.getWarningTimeList() != null)
            bean.setWarningTimeList(entity.getWarningTimeList());
        if(entity.getDetail().isPresent())
            bean.setDetail(entity.getDetail().get());
        return bean;
    }
    private ShortHandEntityGdBean mapToGdBean(@NonNull ShortHandEntity entity){
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
    private NoteEntityGdBean mapToGdBean(@NonNull NoteEntity entity){
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
    private MaterialEntityGdBean mapToGdBean(@NonNull MaterialEntity entity){
        MaterialEntityGdBean bean = new MaterialEntityGdBean();
        bean.setUuid(entity.getUuid());
        bean.setTitle(entity.getTitle());
        if(entity.getDetail().isPresent())
            bean.setDetail(entity.getDetail().get());
        bean.setCreateTime(entity.getCreateTime());
        bean.setLastAccessTime(entity.getLastAccessTime());
        if(!TextUtils.isEmpty(entity.getAbsPath()))
            bean.setAbsFilePath(entity.getAbsPath());
        return bean;
    }
}
