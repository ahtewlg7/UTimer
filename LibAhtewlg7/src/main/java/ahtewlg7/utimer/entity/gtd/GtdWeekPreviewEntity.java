package ahtewlg7.utimer.entity.gtd;

import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdWeekPreviewEntity extends AGtdTaskEntity {
    public static final String TAG = GtdWeekPreviewEntity.class.getSimpleName();

    private int currWeek;
    private int currYear;
    private String preview;

    @Override
    public GtdType getTaskType() {
        return GtdType.WEEKPREVIEW;
    }

    public int getCurrWeek() {
        return currWeek;
    }

    public void setCurrWeek(int currWeek) {
        this.currWeek = currWeek;
    }

    public int getCurrYear() {
        return currYear;
    }

    public void setCurrYear(int currYear) {
        this.currYear = currYear;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getPreview() {
        return preview;
    }

    @Override
    public String toString() {
        return super.toString() + ", currYear = " + currYear + ", currWeek = " + currWeek
                + ", preview = " + preview;
    }
}
