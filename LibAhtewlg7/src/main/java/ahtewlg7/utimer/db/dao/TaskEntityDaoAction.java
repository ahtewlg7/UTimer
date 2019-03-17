package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.TaskEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.TaskEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class TaskEntityDaoAction extends AGreenDaoAction<TaskEntityGdBean, Void> {
    public static final String TAG = TaskEntityDaoAction.class.getSimpleName();

    private static TaskEntityDaoAction gtdEventEntityDaoAction;

    private TaskEntityDaoAction(){
        super();
    }

    public static TaskEntityDaoAction getInstance(){
        if(gtdEventEntityDaoAction == null)
            gtdEventEntityDaoAction = new TaskEntityDaoAction();
        return gtdEventEntityDaoAction;
    }

    @Override
    protected @NonNull
    AbstractDao<TaskEntityGdBean,Void> getCustomDao() {
        return daoSession.getTaskEntityGdBeanDao();
    }

    @Override
    public Optional<TaskEntityGdBean> queryByKey(String title){
        if(TextUtils.isEmpty(title))
            return Optional.absent();
        List<TaskEntityGdBean> actEntityGdBeanList = query(new KeyQueryFilter(title));
        if(actEntityGdBeanList == null || actEntityGdBeanList.isEmpty())
            return Optional.absent();
        return Optional.of(actEntityGdBeanList.get(0));
    }

    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<TaskEntityGdBean>{
        private String key;

        KeyQueryFilter(String key){
            this.key = key;
        }

        @Override
        public QueryBuilder<TaskEntityGdBean> toFilt(QueryBuilder<TaskEntityGdBean> queryBuilder) {
            return queryBuilder.where(TaskEntityGdBeanDao.Properties.Title.eq(key));
        }
    }
}
