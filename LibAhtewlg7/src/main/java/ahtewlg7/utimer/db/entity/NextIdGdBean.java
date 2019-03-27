package ahtewlg7.utimer.db.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

import ahtewlg7.utimer.db.converter.GtdTypeConverter;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "NEXTID"
)
public class NextIdGdBean {
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getNextId() {
        return this.nextId;
    }
    public void setNextId(Long nextId) {
        this.nextId = nextId;
    }
    public GtdType getGtdType() {
        return this.gtdType;
    }
    public void setGtdType(GtdType gtdType) {
        this.gtdType = gtdType;
    }

    @Id
    private Long id;
    @NotNull
    private Long nextId;
    @Unique
    @Convert(converter = GtdTypeConverter.class, columnType = Integer.class)
    private GtdType gtdType;
    @Generated(hash = 2021132301)
    public NextIdGdBean(Long id, @NotNull Long nextId, GtdType gtdType) {
        this.id = id;
        this.nextId = nextId;
        this.gtdType = gtdType;
    }
    @Generated(hash = 749784596)
    public NextIdGdBean() {
    }

}
