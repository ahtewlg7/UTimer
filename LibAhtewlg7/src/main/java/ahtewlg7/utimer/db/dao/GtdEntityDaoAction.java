package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.GtdEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.GtdEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class GtdEntityDaoAction extends AGreenDaoAction<GtdEntityGdBean> {
    public static final String TAG = GtdEntityDaoAction.class.getSimpleName();

    private static GtdEntityDaoAction gtdEndityDaoAction;

    private GtdEntityDaoAction(){
        super();
    }

    public static GtdEntityDaoAction getInstance(){
        if(gtdEndityDaoAction == null)
            gtdEndityDaoAction = new GtdEntityDaoAction();
        return gtdEndityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<GtdEntityGdBean,Void> getCustomDao() {
        return daoSession.getGtdEntityGdBeanDao();
    }

    public GtdEntityGdBean queryById(String id){
        if(TextUtils.isEmpty(id))
            return null;
        List<GtdEntityGdBean> gtdEntityGdBeanList = query(new KeyQueryFilter(id));
        if(gtdEntityGdBeanList == null || gtdEntityGdBeanList.isEmpty())
            return null;
        return gtdEntityGdBeanList.get(0);
    }

    public List<GtdEntityGdBean> queryByCreatTime(DateTime dateTIme){
        if(dateTIme == null)
            return null;
        return query(new CreateTimeQueryFileter(dateTIme));
    }

    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<GtdEntityGdBean>{
        private String key;

        KeyQueryFilter(String key){
            this.key = key;
        }

        @Override
        public QueryBuilder<GtdEntityGdBean> toFilt(QueryBuilder<GtdEntityGdBean> queryBuilder) {
            return queryBuilder.where(GtdEntityGdBeanDao.Properties.Key.eq(key));
        }
    }
    class CreateTimeQueryFileter implements  IGreenDaoQueryFiltVisitor<GtdEntityGdBean>{
        private DateTime dateTime;

        CreateTimeQueryFileter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<GtdEntityGdBean> toFilt(QueryBuilder<GtdEntityGdBean> queryBuilder) {
            return queryBuilder.orderDesc(GtdEntityGdBeanDao.Properties.CreateTime)
                    .where(GtdEntityGdBeanDao.Properties.CreateTime.eq(dateTime));
        }
    }

}
