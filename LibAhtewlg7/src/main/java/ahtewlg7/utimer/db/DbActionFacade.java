package ahtewlg7.utimer.db;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.db.dao.DeedEntityDaoAction;
import ahtewlg7.utimer.db.dao.MaterialEntityDaoAction;
import ahtewlg7.utimer.db.dao.ProjectEntityDaoAction;
import ahtewlg7.utimer.db.entity.DeedEntityGdBean;
import ahtewlg7.utimer.db.entity.MaterialEntityGdBean;
import ahtewlg7.utimer.db.entity.ProjectEntityGdBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedBuilder;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectBuilder;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.MaterialEntity;
import ahtewlg7.utimer.entity.gtd.MaterialEntityBuilder;
import ahtewlg7.utimer.enumtype.DeedState;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/1/8.
 */

public class DbActionFacade {
    /*******************************************material**************************************************/
    public Flowable<GtdProjectEntity> loadAllProjectEntity() {
        return Flowable.fromIterable(ProjectEntityDaoAction.getInstance().loadAll())
                .map(new Function<ProjectEntityGdBean, GtdProjectEntity>() {
                    @Override
                    public GtdProjectEntity apply(ProjectEntityGdBean entityGdBean) throws Exception {
                        return new GtdProjectBuilder().setGdBean(entityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Optional<GtdProjectEntity> getProjectEntityByAbsPath(@NonNull final String absPath) {
        Optional<ProjectEntityGdBean> beanOptional = null;
        try{
            beanOptional = ProjectEntityDaoAction.getInstance().queryByAbsFilePath(absPath);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(beanOptional == null || !beanOptional.isPresent())
            return Optional.absent();
        GtdProjectEntity e = (GtdProjectEntity)new GtdProjectBuilder().setGdBean(beanOptional.get()).build();
        return Optional.of(e);
    }
    public Flowable<Boolean> deleteProjectEntity(@NonNull Flowable<GtdProjectEntity> eventFlowable){
        return eventFlowable.map(new Function<GtdProjectEntity, Boolean>() {
            @Override
            public Boolean apply(GtdProjectEntity entityOptional) throws Exception {
                ProjectEntityGdBean bean = mapToGdBean(entityOptional);
                ProjectEntityDaoAction.getInstance().delete(bean);
                return true;
            }
        });
    }

    public Flowable<Boolean> saveProjectEntity(Flowable<GtdProjectEntity> eventFlowable) {
        return eventFlowable.map(new Function<GtdProjectEntity, Boolean>() {
            @Override
            public Boolean apply(GtdProjectEntity entityOptional) throws Exception {
                ProjectEntityGdBean bean = mapToGdBean(entityOptional);
                long index = ProjectEntityDaoAction.getInstance().insert(bean);
                return index >= 0;
            }
        });
    }
    /*******************************************material**************************************************/
    public Flowable<MaterialEntity> loadAllMaterialEntity() {
        return Flowable.fromIterable(MaterialEntityDaoAction.getInstance().loadAll())
                .map(new Function<MaterialEntityGdBean, MaterialEntity>() {
                    @Override
                    public MaterialEntity apply(MaterialEntityGdBean entityGdBean) throws Exception {
                        return new MaterialEntityBuilder().setGbBean(entityGdBean).build();
                    }
                })
                .subscribeOn(Schedulers.io());
    }
    public Optional<MaterialEntity> getMaterialEntityByAbsPath(@NonNull final String absPath) {
        Optional<MaterialEntityGdBean> beanOptional = null;
        try{
            beanOptional = MaterialEntityDaoAction.getInstance().queryByAbsFilePath(absPath);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(beanOptional == null || !beanOptional.isPresent())
            return Optional.absent();
        MaterialEntity e = (MaterialEntity)new MaterialEntityBuilder().setGbBean(beanOptional.get()).build();
        return Optional.of(e);
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

    public Flowable<Boolean> saveMaterialEntity(Flowable<MaterialEntity> eventFlowable) {
        return eventFlowable.map(new Function<MaterialEntity, Boolean>() {
            @Override
            public Boolean apply(MaterialEntity entityOptional) throws Exception {
                MaterialEntityGdBean bean = mapToGdBean(entityOptional);
                long index = MaterialEntityDaoAction.getInstance().insert(bean);
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
    /***********************************************************************************************/
    private ProjectEntityGdBean mapToGdBean(@NonNull GtdProjectEntity entity){
        ProjectEntityGdBean bean = new ProjectEntityGdBean();
        bean.setUuid(entity.getUuid());
        bean.setTitle(entity.getTitle());
        bean.setAccessTimes(entity.getAccessTimes());
        if(entity.getAttachFileAbsPath().isPresent())
            bean.setAttachFileAbsPath(entity.getAttachFileAbsPath().get());
        if(entity.getCreateTime() != null)
            bean.setCreateTime(entity.getCreateTime());
        else
            bean.setCreateTime(DateTime.now());
        if(entity.getLastAccessTime() != null)
            bean.setLastAccessTime(entity.getLastAccessTime());
        else
            bean.setLastAccessTime(DateTime.now());
        if(entity.getDetail().isPresent())
            bean.setDetail(entity.getDetail().get());
        return bean;
    }
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
