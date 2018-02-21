package ahtewlg7.utimer.GTD;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdWeekPreviewEntity;

/**
 * Created by lw on 2017/10/23.
 */

public class WeekPreviewEntityFactory {
    public static final String TAG = WeekPreviewEntityFactory.class.getSimpleName();

    public boolean isPreviewed(@NonNull AGtdEntity gtdEntity){
        List<GtdWeekPreviewEntity> weekPreviewList = getWeekPreviewList(gtdEntity);
        if(weekPreviewList == null)
            return false;
        int currWeek = DateTime.now().getWeekOfWeekyear();
        int currYear = DateTime.now().getYear();
        boolean result = false;
        for(GtdWeekPreviewEntity gtdWeekPreviewEntity : weekPreviewList){
            if(gtdWeekPreviewEntity.getCurrWeek() == currWeek && gtdWeekPreviewEntity.getCurrYear() == currYear) {
                result = true;
                break;
            }
        }
        return result;
    }

    public List<GtdWeekPreviewEntity> getWeekPreviewList(AGtdEntity gtdEntity){
        return null;
    }

    public boolean ifexsit(Object object) {
        return false;
    }

    public GtdWeekPreviewEntity toCreate(Object object) {
        return null;
    }
}
