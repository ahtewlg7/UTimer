package ahtewlg7.utimer.db.autogen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;
import ahtewlg7.utimer.db.converter.GtdTypeConverter;
import ahtewlg7.utimer.enumtype.GtdType;
import org.joda.time.DateTime;

import ahtewlg7.utimer.db.entity.GtdEntityGdBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "GTD_ENTITY".
*/
public class GtdEntityGdBeanDao extends AbstractDao<GtdEntityGdBean, Long> {

    public static final String TABLENAME = "GTD_ENTITY";

    /**
     * Properties of entity GtdEntityGdBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property Key = new Property(1, String.class, "key", false, "KEY");
        public final static Property Value = new Property(2, String.class, "value", false, "VALUE");
        public final static Property GtdType = new Property(3, Integer.class, "gtdType", false, "GTD_TYPE");
        public final static Property CreateTime = new Property(4, String.class, "createTime", false, "CREATE_TIME");
        public final static Property LastAccessTime = new Property(5, String.class, "lastAccessTime", false, "LAST_ACCESS_TIME");
    }

    private final GtdTypeConverter gtdTypeConverter = new GtdTypeConverter();
    private final DateTimeTypeConverter createTimeConverter = new DateTimeTypeConverter();
    private final DateTimeTypeConverter lastAccessTimeConverter = new DateTimeTypeConverter();

    public GtdEntityGdBeanDao(DaoConfig config) {
        super(config);
    }
    
    public GtdEntityGdBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"GTD_ENTITY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: id
                "\"KEY\" TEXT UNIQUE ," + // 1: key
                "\"VALUE\" TEXT," + // 2: value
                "\"GTD_TYPE\" INTEGER," + // 3: gtdType
                "\"CREATE_TIME\" TEXT," + // 4: createTime
                "\"LAST_ACCESS_TIME\" TEXT);"); // 5: lastAccessTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"GTD_ENTITY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, GtdEntityGdBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(2, key);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        GtdType gtdType = entity.getGtdType();
        if (gtdType != null) {
            stmt.bindLong(4, gtdTypeConverter.convertToDatabaseValue(gtdType));
        }
 
        DateTime createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(5, createTimeConverter.convertToDatabaseValue(createTime));
        }
 
        DateTime lastAccessTime = entity.getLastAccessTime();
        if (lastAccessTime != null) {
            stmt.bindString(6, lastAccessTimeConverter.convertToDatabaseValue(lastAccessTime));
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, GtdEntityGdBean entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String key = entity.getKey();
        if (key != null) {
            stmt.bindString(2, key);
        }
 
        String value = entity.getValue();
        if (value != null) {
            stmt.bindString(3, value);
        }
 
        GtdType gtdType = entity.getGtdType();
        if (gtdType != null) {
            stmt.bindLong(4, gtdTypeConverter.convertToDatabaseValue(gtdType));
        }
 
        DateTime createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(5, createTimeConverter.convertToDatabaseValue(createTime));
        }
 
        DateTime lastAccessTime = entity.getLastAccessTime();
        if (lastAccessTime != null) {
            stmt.bindString(6, lastAccessTimeConverter.convertToDatabaseValue(lastAccessTime));
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public GtdEntityGdBean readEntity(Cursor cursor, int offset) {
        GtdEntityGdBean entity = new GtdEntityGdBean();
        readEntity(cursor, entity, offset);
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, GtdEntityGdBean entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setKey(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setValue(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setGtdType(cursor.isNull(offset + 3) ? null : gtdTypeConverter.convertToEntityProperty(cursor.getInt(offset + 3)));
        entity.setCreateTime(cursor.isNull(offset + 4) ? null : createTimeConverter.convertToEntityProperty(cursor.getString(offset + 4)));
        entity.setLastAccessTime(cursor.isNull(offset + 5) ? null : lastAccessTimeConverter.convertToEntityProperty(cursor.getString(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(GtdEntityGdBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(GtdEntityGdBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(GtdEntityGdBean entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
