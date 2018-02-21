package ahtewlg7.utimer.db.autogen;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import ahtewlg7.utimer.db.entity.GtdEntityGdBean;
import ahtewlg7.utimer.db.entity.NoteEntityGdBean;

import ahtewlg7.utimer.db.autogen.GtdEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.NoteEntityGdBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig gtdEntityGdBeanDaoConfig;
    private final DaoConfig noteEntityGdBeanDaoConfig;

    private final GtdEntityGdBeanDao gtdEntityGdBeanDao;
    private final NoteEntityGdBeanDao noteEntityGdBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        gtdEntityGdBeanDaoConfig = daoConfigMap.get(GtdEntityGdBeanDao.class).clone();
        gtdEntityGdBeanDaoConfig.initIdentityScope(type);

        noteEntityGdBeanDaoConfig = daoConfigMap.get(NoteEntityGdBeanDao.class).clone();
        noteEntityGdBeanDaoConfig.initIdentityScope(type);

        gtdEntityGdBeanDao = new GtdEntityGdBeanDao(gtdEntityGdBeanDaoConfig, this);
        noteEntityGdBeanDao = new NoteEntityGdBeanDao(noteEntityGdBeanDaoConfig, this);

        registerDao(GtdEntityGdBean.class, gtdEntityGdBeanDao);
        registerDao(NoteEntityGdBean.class, noteEntityGdBeanDao);
    }
    
    public void clear() {
        gtdEntityGdBeanDaoConfig.clearIdentityScope();
        noteEntityGdBeanDaoConfig.clearIdentityScope();
    }

    public GtdEntityGdBeanDao getGtdEntityGdBeanDao() {
        return gtdEntityGdBeanDao;
    }

    public NoteEntityGdBeanDao getNoteEntityGdBeanDao() {
        return noteEntityGdBeanDao;
    }

}
