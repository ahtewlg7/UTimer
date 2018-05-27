package ahtewlg7.utimer.db.dao;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import ahtewlg7.utimer.db.AGreenDaoAction;
import ahtewlg7.utimer.db.autogen.NoteEntityGdBeanDao;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;

/**
 * Created by lw on 2016/9/6.
 */
public class NoteEntityDaoAction extends AGreenDaoAction<NoteEntityGdBean, Long> {
    public static final String TAG = NoteEntityDaoAction.class.getSimpleName();

    private static NoteEntityDaoAction gtdEndityDaoAction;

    private NoteEntityDaoAction(){
        super();
    }

    public static NoteEntityDaoAction getInstance(){
        if(gtdEndityDaoAction == null)
            gtdEndityDaoAction = new NoteEntityDaoAction();
        return gtdEndityDaoAction;
    }

    @Override
    protected @NonNull AbstractDao<NoteEntityGdBean,Long> getCustomDao() {
        return daoSession.getNoteEntityGdBeanDao();
    }

    public NoteEntityGdBean queryById(String id){
        if(TextUtils.isEmpty(id))
            return null;
        List<NoteEntityGdBean> noteEntityGdBeanList = query(new KeyQueryFilter(id));
        if(noteEntityGdBeanList == null || noteEntityGdBeanList.isEmpty())
            return null;
        return noteEntityGdBeanList.get(0);
    }

    class KeyQueryFilter implements IGreenDaoQueryFiltVisitor<NoteEntityGdBean> {
        private String key;

        KeyQueryFilter(String key){
            this.key = key;
        }

        @Override
        public QueryBuilder<NoteEntityGdBean> toFilt(QueryBuilder<NoteEntityGdBean> queryBuilder) {
            return queryBuilder.where(NoteEntityGdBeanDao.Properties.Key.eq(key));
        }
    }
}
