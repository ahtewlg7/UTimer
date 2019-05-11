package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.DeedEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.DeedEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 * the detail is "detail"
 */
public class DeedEntityDaoAction extends AGreenDaoAction<DeedEntityGdBean, String> {
    private static DeedEntityDaoAction gtdEventEntityDaoAction;

    private DeedEntityDaoAction(){
        super();
    }

    public static DeedEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new DeedEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<DeedEntityGdBean,Long> getCustomDao() {
        return daoSession.getDeedEntityGdBeanDao();
    }

    @Override
    public Optional<DeedEntityGdBean> queryByKey(String uuid){
        if(TextUtils.isEmpty(uuid))
            return Optional.absent();
        List<DeedEntityGdBean> actEntityGdBeanList = query(new KeyQueryFilter(uuid));
        if(actEntityGdBeanList == null || actEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(actEntityGdBeanList.get(0));
    }

    public Optional<DeedEntityGdBean> queryByDetail(String detail){
        if(TextUtils.isEmpty(detail))
            return Optional.absent();
        List<DeedEntityGdBean> actEntityGdBeanList = query(new DetailQueryFilter(detail));
        if(actEntityGdBeanList == null || actEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(actEntityGdBeanList.get(0));
    }

    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<DeedEntityGdBean>{
        private String key;

        KeyQueryFilter(String key){
            this.key = key;
        }

        @Override
        public QueryBuilder<DeedEntityGdBean> toFilt(QueryBuilder<DeedEntityGdBean> queryBuilder) {
            return queryBuilder.where(DeedEntityGdBeanDao.Properties.Uuid.eq(key));
        }
    }
    class DetailQueryFilter implements IGreenDaoQueryFiltVisitor<DeedEntityGdBean>{
        private String detail;

        DetailQueryFilter(String detail){
            this.detail = detail;
        }

        @Override
        public QueryBuilder<DeedEntityGdBean> toFilt(QueryBuilder<DeedEntityGdBean> queryBuilder) {
            return queryBuilder.where(DeedEntityGdBeanDao.Properties.Detail.eq(detail));
        }
    }

    /*public List<DeedEntityGdBean> queryByWhen(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new WarningTimeQueryFilter(dateTime));
    }*/
    /*public List<DeedEntityGdBean> queryByState(ActState gtdActionType){
        if(gtdActionType == null)
            return null;
        return query(new GtdStateQueryFilter(gtdActionType));
    }

    class WarningTimeQueryFilter implements  IGreenDaoQueryFiltVisitor<DeedEntityGdBean>{
        private DateTime dateTime;

        WarningTimeQueryFilter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<DeedEntityGdBean> toFilt(QueryBuilder<DeedEntityGdBean> queryBuilder) {
//            return queryBuilder.orderDesc(ActionEntityGdBeanDao.Properties.ActionType)
//                    .where(ActionEntityGdBeanDao.Properties.When.eq(dateTime));
            return null;
        }
    }
    class GtdStateQueryFilter implements  IGreenDaoQueryFiltVisitor<DeedEntityGdBean> {
        private ActState gtdType;

        GtdStateQueryFilter(ActState actState) {
            this.gtdType = actState;
        }

        @Override
        public QueryBuilder<DeedEntityGdBean> toFilt(QueryBuilder<DeedEntityGdBean> queryBuilder) {
//            return queryBuilder.where(ActionEntityGdBeanDao.Properties.ActionType.eq(gtdType));
            return null;
        }
    }*/
}
