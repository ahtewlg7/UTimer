package ahtewlg7.utimer.entity.un;

import ahtewlg7.utimer.entity.degree.IPriorityBean;
import ahtewlg7.utimer.enumtype.PriorityLevel;

/**
 * Created by lw on 2017/11/7.
 */

public class TaskPurposeBean implements IPriorityBean {
    public static final String TAG = TaskPurposeBean.class.getSimpleName();

    private PriorityLevel priority;
    private String title;
    private String detail;

    @Override
    public PriorityLevel getPriorityLevel() {
        return priority;
    }

    @Override
    public void setPriorityLevel(PriorityLevel priority) {
        this.priority = priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
