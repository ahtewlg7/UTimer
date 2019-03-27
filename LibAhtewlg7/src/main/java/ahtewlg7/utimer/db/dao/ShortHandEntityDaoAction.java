package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.ShortHandEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class ShortHandEntityDaoAction extends AGreenDaoAction<ShortHandEntityGdBean, String> {
    public static final String TAG = ShortHandEntityDaoAction.class.getSimpleName();

    private static ShortHandEntityDaoAction gtdEventEntityDaoAction;

    private ShortHandEntityDaoAction(){
        super();
    }

    public static ShortHandEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new ShortHandEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<ShortHandEntityGdBean,Long> getCustomDao() {
        return daoSession.getShortHandEntityGdBeanDao();
    }

    @Override
    public Optional<ShortHandEntityGdBean> queryByKey(String title){
        if(TextUtils.isEmpty(title))
            return Optional.absent();
        List<ShortHandEntityGdBean> inboxEntityGdBeanList = query(new KeyQueryFilter(title));
        if(inboxEntityGdBeanList == null || inboxEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(inboxEntityGdBeanList.get(0));
    }

    public List<ShortHandEntityGdBean> queryByCreateTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new CreateTimeQueryFileter(dateTime));
    }
    public List<ShortHandEntityGdBean> queryByLastAccessTime(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new LastAccessTimeQueryFileter(dateTime));
    }

    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<ShortHandEntityGdBean>{
        private String key;

        KeyQueryFilter(String key){
            this.key = key;
        }

        @Override
        public QueryBuilder<ShortHandEntityGdBean> toFilt(QueryBuilder<ShortHandEntityGdBean> queryBuilder) {
            return queryBuilder.where(ShortHandEntityGdBeanDao.Properties.Title.eq(key));
        }
    }
    class CreateTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<ShortHandEntityGdBean>{
        private DateTime dateTime;

        CreateTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<ShortHandEntityGdBean> toFilt(QueryBuilder<ShortHandEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(ShortHandEntityGdBeanDao.Properties.CreateTime)
                    .where(ShortHandEntityGdBeanDao.Properties.CreateTime.eq(dateTime));
        }
    }
    class LastAccessTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<ShortHandEntityGdBean> {
        private DateTime dateTime;

        LastAccessTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<ShortHandEntityGdBean> toFilt(QueryBuilder<ShortHandEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(ShortHandEntityGdBeanDao.Properties.LastAccessTime)
                    .where(ShortHandEntityGdBeanDao.Properties.LastAccessTime.eq(dateTime));
        }
    }
}
