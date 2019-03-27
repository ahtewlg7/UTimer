package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.NextIdGdBeanDao;
import ahtewlg7.utimer.db.entity.NextIdGdBean;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2016/9/6.
 */
public class DbNextIdEntityDaoAction extends AGreenDaoAction<NextIdGdBean, GtdType> {
    public static final String TAG = DbNextIdEntityDaoAction.class.getSimpleName();

    private static DbNextIdEntityDaoAction idKeyEntityDaoAction;

    private DbNextIdEntityDaoAction(){
        super();
    }

    public static DbNextIdEntityDaoAction getInstance(){
        if(idKeyEntityDaoAction == null)
            idKeyEntityDaoAction = new DbNextIdEntityDaoAction();
        return idKeyEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<NextIdGdBean,Long> getCustomDao() {
        return daoSession.getNextIdGdBeanDao();
    }

    @Override
    @Deprecated
    public Optional<NextIdGdBean> queryByKey(GtdType gtdType){
        if(gtdType == null)
            return Optional.absent();
        List<NextIdGdBean> actEntityGdBeanList = query(new KeyQueryFilter(gtdType));
        if(actEntityGdBeanList == null || actEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(actEntityGdBeanList.get(0));
    }


    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<NextIdGdBean>{
        private GtdType key;

        KeyQueryFilter(GtdType key){
            this.key = key;
        }

        @Override
        public QueryBuilder<NextIdGdBean> toFilt(QueryBuilder<NextIdGdBean> queryBuilder) {
            return queryBuilder.where(NextIdGdBeanDao.Properties.GtdType.eq(key));
        }
    }
}
