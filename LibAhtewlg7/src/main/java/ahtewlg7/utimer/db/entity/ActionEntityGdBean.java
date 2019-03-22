package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;

import ahtewlg7.utimer.db.converter.GtdActionTypeConverter;
import ahtewlg7.utimer.db.converter.W5h2HowMuchConverter;
import ahtewlg7.utimer.db.converter.W5h2WhatConverter;
import ahtewlg7.utimer.db.converter.W5h2WhenConverter;
import ahtewlg7.utimer.entity.w5h2.W5h2HowMuch;
import ahtewlg7.utimer.entity.w5h2.W5h2What;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.enumtype.GtdActionType;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "ACTION"
)
public class ActionEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    public static final String TAG = ActionEntityGdBean.class.getSimpleName();
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
        if(actionType != null)
            builder.append(",actionType=").append(actionType.name());
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
    public GtdActionType getActionType() {
        return this.actionType;
    }
    public void setActionType(GtdActionType actionType) {
        this.actionType = actionType;
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

    @NotNull
    @Index(unique = true)
    private String uuid;
    @NotNull
    private String title;
    @NotNull
    private String detail;
    private String attachFileRPath;
    @Convert(converter = GtdActionTypeConverter.class, columnType = Integer.class)
    private GtdActionType actionType;
    @Property(nameInDb = "WHEN")
    @Convert(converter = W5h2WhenConverter.class, columnType = String.class)
    private W5h2When w5h2When;
    @Property(nameInDb = "WHAT")
    @Convert(converter = W5h2WhatConverter.class, columnType = String.class)
    private W5h2What w5h2What;
    @Property(nameInDb = "HOWMUCh")
    @Convert(converter = W5h2HowMuchConverter.class, columnType = String.class)
    private W5h2HowMuch w5h2HowMuch;

    @Generated(hash = 1603180036)
    public ActionEntityGdBean(@NotNull String uuid, @NotNull String title,
            @NotNull String detail, String attachFileRPath,
            GtdActionType actionType, W5h2When w5h2When, W5h2What w5h2What,
            W5h2HowMuch w5h2HowMuch) {
        this.uuid = uuid;
        this.title = title;
        this.detail = detail;
        this.attachFileRPath = attachFileRPath;
        this.actionType = actionType;
        this.w5h2When = w5h2When;
        this.w5h2What = w5h2What;
        this.w5h2HowMuch = w5h2HowMuch;
    }
    @Generated(hash = 1154478005)
    public ActionEntityGdBean() {
    }
}
