package ahtewlg7.utimer.db.dao;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.NoteEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class NoteEntityDaoAction extends AGreenDaoAction<NoteEntityGdBean, String> {
    public static final String TAG = NoteEntityDaoAction.class.getSimpleName();

    private static NoteEntityDaoAction gtdEventEntityDaoAction;

    private NoteEntityDaoAction(){
        super();
    }

    public static NoteEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new NoteEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<NoteEntityGdBean,Long> getCustomDao() {
        return daoSession.getNoteEntityGdBeanDao();
    }

    @Override
    public Optional<NoteEntityGdBean> queryByKey(String uuid){
        if(TextUtils.isEmpty(uuid))
            return Optional.absent();
        List<NoteEntityGdBean> inboxEntityGdBeanList = query(new UuidQueryFilter(uuid));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }
    public Optional<NoteEntityGdBean> queryByRPath(String rPath){
        if(TextUtils.isEmpty(rPath))
            return Optional.absent();
        List<NoteEntityGdBean> inboxEntityGdBeanList = query(new RPathQueryFilter(rPath));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }
    public List<NoteEntityGdBean> queryByCreateTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new CreateTimeQueryFileter(dateTime));
    }
    public List<NoteEntityGdBean> queryByLastAccessTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new LastAccessTimeQueryFileter(dateTime));
    }

    class UuidQueryFilter implements IGreenDaoQueryFiltVisitor<NoteEntityGdBean>{
        private String rPath;

        UuidQueryFilter(String rPath){
            this.rPath = rPath;
        }

        @Override
        public QueryBuilder<NoteEntityGdBean> toFilt(QueryBuilder<NoteEntityGdBean> queryBuilder) {
            return queryBuilder.where(NoteEntityGdBeanDao.Properties.Uuid.eq(rPath));
        }
    }
    class RPathQueryFilter implements IGreenDaoQueryFiltVisitor<NoteEntityGdBean>{
        private String rPath;

        RPathQueryFilter(String rPath){
            this.rPath = rPath;
        }

        @Override
        public QueryBuilder<NoteEntityGdBean> toFilt(QueryBuilder<NoteEntityGdBean> queryBuilder) {
            return queryBuilder.where(NoteEntityGdBeanDao.Properties.AttachFileRPath.eq(rPath));
        }
    }
    class CreateTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<NoteEntityGdBean>{
        private DateTime dateTime;

        CreateTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<NoteEntityGdBean> toFilt(QueryBuilder<NoteEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(NoteEntityGdBeanDao.Properties.CreateTime)
                    .where(NoteEntityGdBeanDao.Properties.CreateTime.eq(dateTime));
        }
    }
    class LastAccessTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<NoteEntityGdBean> {
        private DateTime dateTime;

        LastAccessTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<NoteEntityGdBean> toFilt(QueryBuilder<NoteEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(NoteEntityGdBeanDao.Properties.LastAccessTime)
                    .where(NoteEntityGdBeanDao.Properties.LastAccessTime.eq(dateTime));
        }
    }
}
