package ahtewlg7.utimer.gtd;

import org.joda.time.DateTime;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.enumtype.DateLife;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/3/31.
 */
public class DateLifeCycleAction {

    private DateTimeAction dateTimeAction;

    public DateLifeCycleAction() {
        dateTimeAction = new DateTimeAction();
    }

    public boolean inPast(DateTime dateTime){
        if(dateTime == null)
            return false;
        return dateTimeAction.isInPast(dateTime);
    }
    public boolean isToday(DateTime dateTime){
        if(dateTime == null)
            return false;
        return dateTimeAction.isInToday(dateTime);
    }
    public boolean isTomorrow(DateTime dateTime){
        if(dateTime == null)
            return false;
        return dateTimeAction.isInTomorrow(dateTime);
    }
    public boolean ifThisWeek(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInWeek(dateTime);
    }
    public boolean ifNextWeek(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInNextWeek(dateTime);
    }
    public boolean ifThisMonth(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInMonth(dateTime);
    }
    public boolean ifNextMonth(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInNextMonth(dateTime);
    }
    public boolean ifThisQuarter(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInQuarter(dateTime);
    }
    public boolean ifHalfYear(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInHalfYear(dateTime);
    }
    public boolean ifThisYear(DateTime dateTime) {
        if (dateTime == null)
            return false;
        return dateTimeAction.isInYear(dateTime);
    }

    public DateLife getLife(DateTime dateTime){
        if(dateTime == null)
            return null;
        DateLife actDateTime = null;
        if(inPast(dateTime))
            actDateTime = DateLife.PAST;
        else if(isToday(dateTime))
            actDateTime = DateLife.TODAY;
        else if(isTomorrow(dateTime))
            actDateTime = DateLife.TOMORROW;
        else if(ifThisWeek(dateTime))
            actDateTime = DateLife.WEEK;
        else if(ifNextWeek(dateTime))
            actDateTime = DateLife.NEXT_WEEK;
        else if(ifThisMonth(dateTime))
            actDateTime = DateLife.MONTH;
        else if(ifNextMonth(dateTime))
            actDateTime = DateLife.NEXT_MONTH;
        else if(ifThisQuarter(dateTime))
            actDateTime = DateLife.QUARTER_YEAR;
        else if(ifThisYear(dateTime))
            actDateTime = DateLife.YEAR;
        else if(ifHalfYear(dateTime))
            actDateTime = DateLife.HALF_YEAR;
        return actDateTime;
    }

    public String getLifeDetail(DateLife actLife){
        String detail = null;
        switch (actLife){
            case PAST:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_overdue);
                break;
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
