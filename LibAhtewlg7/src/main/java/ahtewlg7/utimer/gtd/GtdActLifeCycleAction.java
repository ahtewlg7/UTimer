package ahtewlg7.utimer.gtd;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActLife;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/3/31.
 */
public class GtdActLifeCycleAction{

    private DateTimeAction dateTimeAction;

    public GtdActLifeCycleAction() {
        dateTimeAction = new DateTimeAction();
    }

    public ActLife getLife(GtdActionEntity entity){
        ActLife actDateTime = null;
        if(isTodayWork(entity))
            actDateTime = ActLife.TODAY;
        else if(isTomorrowWork(entity))
            actDateTime = ActLife.TOMORROW;
        else if(ifWeekWork(entity))
            actDateTime = ActLife.WEEK;
        else if(ifMonthWork(entity))
            actDateTime = ActLife.MONTH;
        else if(ifQuarterWork(entity))
            actDateTime = ActLife.QUARTER;
        else if(ifYearWork(entity))
            actDateTime = ActLife.YEAR;
        else
            actDateTime = ActLife.TODAY;
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

    public String getActLifeDetail(ActLife actLife){
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
        }
        return detail;
    }
}
