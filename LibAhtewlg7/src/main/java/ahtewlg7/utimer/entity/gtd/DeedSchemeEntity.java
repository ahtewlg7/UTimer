package ahtewlg7.utimer.entity.gtd;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import org.joda.time.DateTime;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/7/20.
 */
public class DeedSchemeEntity {
    public static final int INVALID_PROGRESS  = -1;
    public static final String DEFAULT_SCHEME = MyRInfo.getStringByID(R.string.title_calendar_scheme);

    private int progress =  INVALID_PROGRESS;

    private String tip;
    private String uuid;
    private DateTime dateTime;

    public DeedSchemeEntity() {
    }
    public DeedSchemeEntity(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof DeedSchemeEntity)
            return TextUtils.isEmpty(uuid) && uuid.equals(((DeedSchemeEntity) obj).getUuid());
        return false;
    }
}
