package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.entity.ActionEntityGdBean;
import ahtewlg7.utimer.entity.AGtdUtimerBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class GtdActionBuilder extends AGtdUtimerBuilder<GtdActionEntity, GtdActionBuilder> {
    public static final String TAG = GtdActionBuilder.class.getSimpleName();

    protected List<DateTime> timeList;
    protected ActionEntityGdBean gdBean;

    public GtdActionBuilder setTimeList(List<DateTime> timeList){
        this.timeList = timeList;
        return this;
    }

    public GtdActionBuilder setGbBean(ActionEntityGdBean gdBean){
        this.gdBean = gdBean;
        return this;
    }

    @NonNull
    @Override
    public GtdActionEntity build() {
        return new GtdActionEntity(this);
    }
}
