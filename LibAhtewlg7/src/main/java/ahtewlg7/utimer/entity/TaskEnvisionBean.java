package ahtewlg7.utimer.entity;

import ahtewlg7.utimer.entity.degree.IPriorityBean;
import ahtewlg7.utimer.enumtype.PriorityLevel;

/**
 * Created by lw on 2017/11/7.
 */

public class TaskEnvisionBean implements IPriorityBean {
    public static final String TAG = TaskEnvisionBean.class.getSimpleName();

    private PriorityLevel priority;
    private String outcome;

    @Override
    public PriorityLevel getPriorityLevel() {
        return priority;
    }

    @Override
    public void setPriorityLevel(PriorityLevel priority) {
        this.priority = priority;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
