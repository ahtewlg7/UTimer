package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.ActionEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.ActionEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 * the detail is "detail"
 */
public class ActionEntityDaoAction extends AGreenDaoAction<ActionEntityGdBean, String> {
    private static ActionEntityDaoAction gtdEventEntityDaoAction;

    private ActionEntityDaoAction(){
        super();
    }

    public static ActionEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new ActionEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<ActionEntityGdBean,Long> getCustomDao() {
        return daoSession.getActionEntityGdBeanDao();
    }

    @Override
    public Optional<ActionEntityGdBean> queryByKey(String uuid){
        if(TextUtils.isEmpty(uuid))
            return Optional.absent();
        List<ActionEntityGdBean> actEntityGdBeanList = query(new KeyQueryFilter(uuid));
        if(actEntityGdBeanList == null || actEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(actEntityGdBeanList.get(0));
    }

    public Optional<ActionEntityGdBean> queryByDetail(String detail){
        if(TextUtils.isEmpty(detail))
            return Optional.absent();
        List<ActionEntityGdBean> actEntityGdBeanList = query(new DetailQueryFilter(detail));
        if(actEntityGdBeanList == null || actEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(actEntityGdBeanList.get(0));
    }

    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<ActionEntityGdBean>{
        private String key;

        KeyQueryFilter(String key){
            this.key = key;
        }

        @Override
        public QueryBuilder<ActionEntityGdBean> toFilt(QueryBuilder<ActionEntityGdBean> queryBuilder) {
            return queryBuilder.where(ActionEntityGdBeanDao.Properties.Uuid.eq(key));
        }
    }
    class DetailQueryFilter implements IGreenDaoQueryFiltVisitor<ActionEntityGdBean>{
        private String detail;

        DetailQueryFilter(String detail){
            this.detail = detail;
        }

        @Override
        public QueryBuilder<ActionEntityGdBean> toFilt(QueryBuilder<ActionEntityGdBean> queryBuilder) {
            return queryBuilder.where(ActionEntityGdBeanDao.Properties.Detail.eq(detail));
        }
    }

    /*public List<ActionEntityGdBean> queryByWhen(DateTime dateTime){
        if(dateTime == null)
            return null;
        return query(new WarningTimeQueryFilter(dateTime));
    }*/
    /*public List<ActionEntityGdBean> queryByState(ActState gtdActionType){
        if(gtdActionType == null)
            return null;
        return query(new GtdStateQueryFilter(gtdActionType));
    }

    class WarningTimeQueryFilter implements  IGreenDaoQueryFiltVisitor<ActionEntityGdBean>{
        private DateTime dateTime;

        WarningTimeQueryFilter(DateTime dateTime){
            this.dateTime = dateTime;
        }

        @Override
        public QueryBuilder<ActionEntityGdBean> toFilt(QueryBuilder<ActionEntityGdBean> queryBuilder) {
//            return queryBuilder.orderDesc(ActionEntityGdBeanDao.Properties.ActionType)
//                    .where(ActionEntityGdBeanDao.Properties.When.eq(dateTime));
            return null;
        }
    }
    class GtdStateQueryFilter implements  IGreenDaoQueryFiltVisitor<ActionEntityGdBean> {
        private ActState gtdType;

        GtdStateQueryFilter(ActState actState) {
            this.gtdType = actState;
        }

        @Override
        public QueryBuilder<ActionEntityGdBean> toFilt(QueryBuilder<ActionEntityGdBean> queryBuilder) {
//            return queryBuilder.where(ActionEntityGdBeanDao.Properties.ActionType.eq(gtdType));
            return null;
        }
    }*/
}
