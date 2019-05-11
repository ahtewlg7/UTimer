package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.converter.DateTimeListTypeConverter;
import ahtewlg7.utimer.db.converter.GtdDeedStateConverter;
import ahtewlg7.utimer.db.converter.W5h2HowMuchConverter;
import ahtewlg7.utimer.db.converter.W5h2WhatConverter;
import ahtewlg7.utimer.db.converter.W5h2WhenConverter;
import ahtewlg7.utimer.entity.w5h2.W5h2HowMuch;
import ahtewlg7.utimer.entity.w5h2.W5h2What;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.enumtype.ActState;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "DEED"
)
public class DeedEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    public static final String TAG = DeedEntityGdBean.class.getSimpleName();
    //KEEP FIELDS END

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        builder.append("{");
        if(!TextUtils.isEmpty(uuid))
            builder.append(",uuid=").append(uuid);
        if(!TextUtils.isEmpty(title))
            builder.append(",title=").append(title);
        if(!TextUtils.isEmpty(detail))
            builder.append(",detail=").append(detail);
        if(actionState != null)
            builder.append(",actionState=").append(actionState.name());
        return builder.append("}").toString();
    }
    // KEEP METHODS END
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public String getAttachFileRPath() {
        return this.attachFileRPath;
    }
    public void setAttachFileRPath(String attachFileRPath) {
        this.attachFileRPath = attachFileRPath;
    }
    public ActState getActionState() {
        return this.actionState;
    }
    public void setActionState(ActState actionState) {
        this.actionState = actionState;
    }
    public W5h2When getW5h2When() {
        return this.w5h2When;
    }
    public void setW5h2When(W5h2When w5h2When) {
        this.w5h2When = w5h2When;
    }
    public W5h2What getW5h2What() {
        return this.w5h2What;
    }
    public void setW5h2What(W5h2What w5h2What) {
        this.w5h2What = w5h2What;
    }
    public W5h2HowMuch getW5h2HowMuch() {
        return this.w5h2HowMuch;
    }
    public void setW5h2HowMuch(W5h2HowMuch w5h2HowMuch) {
        this.w5h2HowMuch = w5h2HowMuch;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<DateTime> getWarningTimeList() {
        return this.warningTimeList;
    }
    public void setWarningTimeList(List<DateTime> warningTimeList) {
        this.warningTimeList = warningTimeList;
    }


    @Id(autoincrement = true)
    private Long id;
    @NotNull
    @Index(unique = true)
    private String uuid;
    @NotNull
    private String title;
    @NotNull
    private String detail;
    private String attachFileRPath;
    @Convert(converter = GtdDeedStateConverter.class, columnType = Integer.class)
    private ActState actionState;
    @Property(nameInDb = "WARNING_TIME")
    @Convert(converter = DateTimeListTypeConverter.class, columnType = String.class)
    private List<DateTime> warningTimeList;
    @Property(nameInDb = "WHEN")
    @Convert(converter = W5h2WhenConverter.class, columnType = String.class)
    private W5h2When w5h2When;
    @Property(nameInDb = "WHAT")
    @Convert(converter = W5h2WhatConverter.class, columnType = String.class)
    private W5h2What w5h2What;
    @Property(nameInDb = "HOWMUCH")
    @Convert(converter = W5h2HowMuchConverter.class, columnType = String.class)
    private W5h2HowMuch w5h2HowMuch;

    @Generated(hash = 256283279)
    public DeedEntityGdBean(Long id, @NotNull String uuid, @NotNull String title,
            @NotNull String detail, String attachFileRPath, ActState actionState,
            List<DateTime> warningTimeList, W5h2When w5h2When, W5h2What w5h2What,
            W5h2HowMuch w5h2HowMuch) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.detail = detail;
        this.attachFileRPath = attachFileRPath;
        this.actionState = actionState;
        this.warningTimeList = warningTimeList;
        this.w5h2When = w5h2When;
        this.w5h2What = w5h2What;
        this.w5h2HowMuch = w5h2HowMuch;
    }
    @Generated(hash = 1274789254)
    public DeedEntityGdBean() {
    }

}
