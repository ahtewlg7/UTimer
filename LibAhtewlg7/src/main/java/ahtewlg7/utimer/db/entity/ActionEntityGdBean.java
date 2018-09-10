package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.joda.time.DateTime;

import ahtewlg7.utimer.db.autogen.ActionEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.DaoSession;
import ahtewlg7.utimer.db.autogen.TaskEntityGdBeanDao;
import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;
import ahtewlg7.utimer.db.converter.GtdTypeConverter;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "ACTION_ENTITY"
)
public class ActionEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    public static final String TAG = ActionEntityGdBean.class.getSimpleName();
    //KEEP FIELDS END

    @Id(autoincrement = true)
    private long id;
    @Convert(converter = GtdTypeConverter.class, columnType = Integer.class)
    private GtdType gtdType;
    @Index(unique = true)
    private String title;
    private String value;
    private int attachNum;
    private int delayNum;
    private String what;
    @Convert(converter = DateTimeTypeConverter.class, columnType = String.class)
    private DateTime when;
    private String who;
    private String where;
    private String zygote;
    private long taskId;
    @ToOne(joinProperty = "taskId")
    private TaskEntityGdBean task;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 227755961)
    private transient ActionEntityGdBeanDao myDao;

    @Generated(hash = 1120737525)
    public ActionEntityGdBean(long id, GtdType gtdType, String title, String value, int attachNum,
            int delayNum, String what, DateTime when, String who, String where, String zygote,
            long taskId) {
        this.id = id;
        this.gtdType = gtdType;
        this.title = title;
        this.value = value;
        this.attachNum = attachNum;
        this.delayNum = delayNum;
        this.what = what;
        this.when = when;
        this.who = who;
        this.where = where;
        this.zygote = zygote;
        this.taskId = taskId;
    }

    @Generated(hash = 1154478005)
    public ActionEntityGdBean() {
    }

    @Generated(hash = 100676365)
    private transient Long task__resolvedKey;

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        builder.append("{").append(id);
        builder.append(",task").append(taskId);
        builder.append(",attachNum").append(attachNum);
        if(!TextUtils.isEmpty(title))
            builder.append(",title").append(title);
        if(!TextUtils.isEmpty(what))
            builder.append(",what").append(what);
        if(when != null)
            builder.append(",when").append(when.toString());
        if(!TextUtils.isEmpty(where))
            builder.append(",where").append(where);
        if(!TextUtils.isEmpty(what))
            builder.append(",what").append(what);
        if(!TextUtils.isEmpty(zygote))
            builder.append(",zygote").append(zygote);
        return builder.append("}").toString();
    }
    // KEEP METHODS END

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAttachNum() {
        return this.attachNum;
    }

    public void setAttachNum(int attachNum) {
        this.attachNum = attachNum;
    }

    public String getWhat() {
        return this.what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public DateTime getWhen() {
        return this.when;
    }

    public void setWhen(DateTime when) {
        this.when = when;
    }

    public String getWho() {
        return this.who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getWhere() {
        return this.where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getZygote() {
        return this.zygote;
    }

    public void setZygote(String zygote) {
        this.zygote = zygote;
    }

    public long getTaskId() {
        return this.taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1415548687)
    public TaskEntityGdBean getTask() {
        long __key = this.taskId;
        if (task__resolvedKey == null || !task__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TaskEntityGdBeanDao targetDao = daoSession.getTaskEntityGdBeanDao();
            TaskEntityGdBean taskNew = targetDao.load(__key);
            synchronized (this) {
                task = taskNew;
                task__resolvedKey = __key;
            }
        }
        return task;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1291500309)
    public void setTask(@NotNull TaskEntityGdBean task) {
        if (task == null) {
            throw new DaoException(
                    "To-one property 'taskId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.task = task;
            taskId = task.getId();
            task__resolvedKey = taskId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 546874885)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActionEntityGdBeanDao() : null;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public GtdType getGtdType() {
        return this.gtdType;
    }

    public void setGtdType(GtdType gtdType) {
        this.gtdType = gtdType;
    }

    public int getDelayNum() {
        return this.delayNum;
    }

    public void setDelayNum(int delayNum) {
        this.delayNum = delayNum;
    }
}
