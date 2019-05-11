package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.entity.DeedEntityGdBean;
import ahtewlg7.utimer.entity.AGtdUtimerBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class GtdDeedBuilder extends AGtdUtimerBuilder<GtdDeedEntity, GtdDeedBuilder> {
    public static final String TAG = GtdDeedBuilder.class.getSimpleName();

    protected DeedEntityGdBean gdBean;
    protected List<DateTime> warningTimeList;

    public GtdDeedBuilder setGbBean(DeedEntityGdBean gdBean){
        this.gdBean = gdBean;
        return this;
    }
    public GtdDeedBuilder setWarningTimeList(List<DateTime> warningTimeList){
        this.warningTimeList = warningTimeList;
        return this;
    }

    @NonNull
    @Override
    public GtdDeedEntity build() {
        return new GtdDeedEntity(this);
    }
}
