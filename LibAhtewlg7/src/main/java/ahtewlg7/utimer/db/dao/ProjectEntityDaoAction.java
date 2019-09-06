package ahtewlg7.utimer.db.dao;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.ProjectEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.ProjectEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class ProjectEntityDaoAction extends AGreenDaoAction<ProjectEntityGdBean, String> {
    private static ProjectEntityDaoAction gtdEventEntityDaoAction;

    private ProjectEntityDaoAction(){
        super();
    }

    public static ProjectEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new ProjectEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<ProjectEntityGdBean,Long> getCustomDao() {
        return daoSession.getProjectEntityGdBeanDao();
    }

    @Override
    public Optional<ProjectEntityGdBean> queryByKey(String uuid){
        if(TextUtils.isEmpty(uuid))
            return Optional.absent();
        List<ProjectEntityGdBean> inboxEntityGdBeanList = query(new UuidQueryFilter(uuid));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }
    public Optional<ProjectEntityGdBean> queryByAbsFilePath(String absPath){
        if(TextUtils.isEmpty(absPath))
            return Optional.absent();
        List<ProjectEntityGdBean> inboxEntityGdBeanList = query(new AbsFilePathQueryFilter(absPath));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }
    public List<ProjectEntityGdBean> queryByCreateTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new CreateTimeQueryFileter(dateTime));
    }
    public List<ProjectEntityGdBean> queryByLastAccessTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new LastAccessTimeQueryFileter(dateTime));
    }

    class UuidQueryFilter implements IGreenDaoQueryFiltVisitor<ProjectEntityGdBean>{
        private String uuid;

        UuidQueryFilter(String uuid){
            this.uuid = uuid;
        }

        @Override
        public QueryBuilder<ProjectEntityGdBean> toFilt(QueryBuilder<ProjectEntityGdBean> queryBuilder) {
            return queryBuilder.where(ProjectEntityGdBeanDao.Properties.Uuid.eq(uuid));
        }
    }
    class AbsFilePathQueryFilter implements IGreenDaoQueryFiltVisitor<ProjectEntityGdBean>{
        private String absFilePath;

        AbsFilePathQueryFilter(String absFilePath){
            this.absFilePath = absFilePath;
        }

        @Override
        public QueryBuilder<ProjectEntityGdBean> toFilt(QueryBuilder<ProjectEntityGdBean> queryBuilder) {
            return queryBuilder.where(ProjectEntityGdBeanDao.Properties.AttachFileAbsPath.eq(absFilePath));
        }
    }
    class CreateTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<ProjectEntityGdBean>{
        private DateTime dateTime;

        CreateTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<ProjectEntityGdBean> toFilt(QueryBuilder<ProjectEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(ProjectEntityGdBeanDao.Properties.CreateTime)
                    .where(ProjectEntityGdBeanDao.Properties.CreateTime.eq(dateTime));
        }
    }
    class LastAccessTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<ProjectEntityGdBean> {
        private DateTime dateTime;

        LastAccessTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<ProjectEntityGdBean> toFilt(QueryBuilder<ProjectEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(ProjectEntityGdBeanDao.Properties.LastAccessTime)
                    .where(ProjectEntityGdBeanDao.Properties.LastAccessTime.eq(dateTime));
        }
    }
}
