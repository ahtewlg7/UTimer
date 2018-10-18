package ahtewlg7.utimer.entity.un;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2018/1/9.
 */

public interface IUtimerEntity extends IValidEntity {
    public String getId();
    public String getTitle();
    public String getDetail();
    public @NonNull GtdType getGtdType();
}
