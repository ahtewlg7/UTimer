package ahtewlg7.utimer.gtd;

import org.joda.time.DateTime;

import ahtewlg7.utimer.R;
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

    public GtdLife getLife(DateTime dateTime){
        GtdLife actDateTime = null;
        if(isTodayWork(dateTime))
            actDateTime = GtdLife.TODAY;
        else if(isTomorrowWork(dateTime))
            actDateTime = GtdLife.TOMORROW;
        else if(ifWeekWork(dateTime))
            actDateTime = GtdLife.WEEK;
        else if(ifNextWeekWork(dateTime))
            actDateTime = GtdLife.NEXT_WEEK;
        else if(ifMonthWork(dateTime))
            actDateTime = GtdLife.MONTH;
        else if(ifNextMonthWork(dateTime))
            actDateTime = GtdLife.NEXT_MONTH;
        else if(ifQuarterWork(dateTime))
            actDateTime = GtdLife.QUARTER_YEAR;
        else if(ifYearWork(dateTime))
            actDateTime = GtdLife.YEAR;
        else if(ifHalfYearWork(dateTime))
            actDateTime = GtdLife.HALF_YEAR;
        return actDateTime;
    }

    public boolean isTodayWork(DateTime dateTime){
        if(dateTime == null)
            return false;
        return dateTimeAction.isInToday(dateTime);
    }
    public boolean isTomorrowWork(DateTime dateTime){
        if(dateTime == null)
            return false;
        return dateTimeAction.isInTomorrow(dateTime);
    }
    public boolean ifWeekWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInWeek(dateTime);
    }
    public boolean ifNextWeekWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInNextWeek(dateTime);
    }
    public boolean ifMonthWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInMonth(dateTime);
    }
    public boolean ifNextMonthWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInNextMonth(dateTime);
    }
    public boolean ifQuarterWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInQuarter(dateTime);
    }
    public boolean ifHalfYearWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInHalfYear(dateTime);
    }
    public boolean ifYearWork(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInYear(dateTime);
    }

    public String getLifeDetail(GtdLife actLife){
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
            case NEXT_WEEK:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_next_week);
                break;
            case MONTH:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_month);
                break;
            case NEXT_MONTH:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_next_month);
                break;
            case QUARTER_YEAR:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_quarter_year);
                break;
            case HALF_YEAR:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_half_year);
                break;
            case YEAR:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_year);
                break;
        }
        return detail;
    }
}
