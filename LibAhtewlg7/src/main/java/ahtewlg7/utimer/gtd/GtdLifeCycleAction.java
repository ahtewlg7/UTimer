package ahtewlg7.utimer.gtd;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.GtdLife;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/3/31.
 */
public class GtdLifeCycleAction {

    private DateTimeAction dateTimeAction;

    public GtdLifeCycleAction() {
        dateTimeAction = new DateTimeAction();
    }

    public GtdLife getLife(GtdActionEntity entity){
        GtdLife actDateTime = null;
        if(isTodayWork(entity))
            actDateTime = GtdLife.TODAY;
        else if(isTomorrowWork(entity))
            actDateTime = GtdLife.TOMORROW;
        else if(ifWeekWork(entity))
            actDateTime = GtdLife.WEEK;
        else if(ifMonthWork(entity))
            actDateTime = GtdLife.MONTH;
        else if(ifQuarterWork(entity))
            actDateTime = GtdLife.QUARTER;
        else if(ifYearWork(entity))
            actDateTime = GtdLife.YEAR;
        else
            actDateTime = GtdLife.TODAY;
        return actDateTime;
    }

    public boolean isTodayWork(GtdActionEntity entity){
        if(entity == null || !entity.ifValid() || !entity.getFirstWorkTime().isPresent())
            return false;
        return dateTimeAction.isInToday(entity.getFirstWorkTime().get());
    }
    public boolean isTomorrowWork(GtdActionEntity entity){
        if(entity == null || !entity.ifValid() || !entity.getFirstWorkTime().isPresent())
            return false;
        return dateTimeAction.isInTomorrow(entity.getFirstWorkTime().get());
    }
    public boolean ifWeekWork(GtdActionEntity entity) {
        if (entity == null || !entity.ifValid() || !entity.getFirstWorkTime().isPresent())
            return false;
        return dateTimeAction.isInWeek(entity.getFirstWorkTime().get());
    }
    public boolean ifMonthWork(GtdActionEntity entity) {
        if (entity == null || !entity.ifValid() || !entity.getFirstWorkTime().isPresent())
            return false;
        return dateTimeAction.isInMonth(entity.getFirstWorkTime().get());
    }
    public boolean ifQuarterWork(GtdActionEntity entity) {
        if (entity == null || !entity.ifValid() || !entity.getFirstWorkTime().isPresent())
            return false;
        return dateTimeAction.isInQuarter(entity.getFirstWorkTime().get());
    }
    public boolean ifYearWork(GtdActionEntity entity) {
        if (entity == null || !entity.ifValid() || !entity.getFirstWorkTime().isPresent())
            return false;
        return dateTimeAction.isInYear(entity.getFirstWorkTime().get());
    }

    public String getActLifeDetail(GtdLife actLife){
        String detail = null;
        switch (actLife){
            case TODAY:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_today);
                break;
            case TOMORROW:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_tomorrow);
                break;
            case WEEK:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_week);
                break;
            case MONTH:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_month);
                break;
            case QUARTER:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_quarter);
                break;
            case YEAR:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_year);
                break;
        }
        return detail;
    }
}
