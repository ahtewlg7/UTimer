package ahtewlg7.utimer.db.dao;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.MaterialEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.MaterialEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class MaterialEntityDaoAction extends AGreenDaoAction<MaterialEntityGdBean, String> {
    private static MaterialEntityDaoAction gtdEventEntityDaoAction;

    private MaterialEntityDaoAction(){
        super();
    }

    public static MaterialEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new MaterialEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<MaterialEntityGdBean,Long> getCustomDao() {
        return daoSession.getMaterialEntityGdBeanDao();
    }

    @Override
    public Optional<MaterialEntityGdBean> queryByKey(String uuid){
        if(TextUtils.isEmpty(uuid))
            return Optional.absent();
        List<MaterialEntityGdBean> inboxEntityGdBeanList = query(new UuidQueryFilter(uuid));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }
    public Optional<MaterialEntityGdBean> queryByAbsFilePath(String absPath){
        if(TextUtils.isEmpty(absPath))
            return Optional.absent();
        List<MaterialEntityGdBean> inboxEntityGdBeanList = query(new AbsFilePathQueryFilter(absPath));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }
    public List<MaterialEntityGdBean> queryByCreateTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new CreateTimeQueryFileter(dateTime));
    }
    public List<MaterialEntityGdBean> queryByLastAccessTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new LastAccessTimeQueryFileter(dateTime));
    }

    class UuidQueryFilter implements IGreenDaoQueryFiltVisitor<MaterialEntityGdBean>{
        private String uuid;

        UuidQueryFilter(String uuid){
            this.uuid = uuid;
        }

        @Override
        public QueryBuilder<MaterialEntityGdBean> toFilt(QueryBuilder<MaterialEntityGdBean> queryBuilder) {
            return queryBuilder.where(MaterialEntityGdBeanDao.Properties.Uuid.eq(uuid));
        }
    }
    class AbsFilePathQueryFilter implements IGreenDaoQueryFiltVisitor<MaterialEntityGdBean>{
        private String absFilePath;

        AbsFilePathQueryFilter(String absFilePath){
            this.absFilePath = absFilePath;
        }

        @Override
        public QueryBuilder<MaterialEntityGdBean> toFilt(QueryBuilder<MaterialEntityGdBean> queryBuilder) {
            return queryBuilder.where(MaterialEntityGdBeanDao.Properties.AbsFilePath.eq(absFilePath));
        }
    }
    class CreateTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<MaterialEntityGdBean>{
        private DateTime dateTime;

        CreateTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<MaterialEntityGdBean> toFilt(QueryBuilder<MaterialEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(MaterialEntityGdBeanDao.Properties.CreateTime)
                    .where(MaterialEntityGdBeanDao.Properties.CreateTime.eq(dateTime));
        }
    }
    class LastAccessTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<MaterialEntityGdBean> {
        private DateTime dateTime;

        LastAccessTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<MaterialEntityGdBean> toFilt(QueryBuilder<MaterialEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(MaterialEntityGdBeanDao.Properties.LastAccessTime)
                    .where(MaterialEntityGdBeanDao.Properties.LastAccessTime.eq(dateTime));
        }
    }
}
