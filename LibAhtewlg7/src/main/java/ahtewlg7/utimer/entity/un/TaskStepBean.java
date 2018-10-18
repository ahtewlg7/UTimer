package ahtewlg7.utimer.entity.un;

import ahtewlg7.utimer.entity.degree.DegreeEntity;
import ahtewlg7.utimer.entity.degree.IPriorityBean;
import ahtewlg7.utimer.entity.taskContext.un.ATaskContext;
import ahtewlg7.utimer.enumtype.PriorityLevel;

/**
 * Created by lw on 2017/11/8.
 */

public class TaskStepBean implements IPriorityBean {
    public static final String TAG = TaskStepBean.class.getSimpleName();

    private DegreeEntity importantLevel;
    private DegreeEntity emergencyLevel;
    private DegreeEntity energyLevel;
    private PriorityLevel priorityLevel;
    private TimeStampEntity timeEntity;
    private ATaskContext taskContext;

    private int completedPercent;
    private int gainedPercent;

    private boolean isFinished;

    private RepeatEntity repeatEntity;

    public DegreeEntity getImportant() {
        return importantLevel;
    }

    public void setImportant(DegreeEntity importantLevel) {
        this.importantLevel = importantLevel;
    }

    public DegreeEntity getEmergency() {
        return emergencyLevel;
    }

    public void setEmergency(DegreeEntity emergencyLevel) {
        this.emergencyLevel = emergencyLevel;
    }

    public DegreeEntity getEnergy() {
        return energyLevel;
    }

    public void setEnergy(DegreeEntity energyLevel) {
        this.energyLevel = energyLevel;
    }

    @Override
    public PriorityLevel getPriorityLevel() {
        return priorityLevel;
    }

    @Override
    public void setPriorityLevel(PriorityLevel priorityLevel) {
        this.priorityLevel = priorityLevel;
    }


    public int getCompletedPercent() {
        return completedPercent;
    }

    public void setCompletedPercent(int completedPercent) {
        this.completedPercent = completedPercent;
    }

    public int getGainedPercent() {
        return gainedPercent;
    }

    public void setGainedPercent(int gainedPercent) {
        this.gainedPercent = gainedPercent;
    }

    public TimeStampEntity getTimeEntity() {
        return timeEntity;
    }

    public void setTimeEntity(TimeStampEntity timeEntity) {
        this.timeEntity = timeEntity;
    }

    public ATaskContext getTaskContext() {
        return taskContext;
    }

    public void setTaskContext(ATaskContext taskContext) {
        this.taskContext = taskContext;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public RepeatEntity getRepeatEntity() {
        return repeatEntity;
    }

    public void setRepeatEntity(RepeatEntity repeatEntity) {
        this.repeatEntity = repeatEntity;
    }


    @Override
    public String toString() {
        return "priority = " + priorityLevel.toString() + ", importantLevel = " + importantLevel.toString()
                + ", emergencyLevel = " + emergencyLevel.toString() + ", energyLevel = " + energyLevel.toString()
                + ", completedPercent = " + completedPercent + ", gainedPercent = " + gainedPercent
                + ", taskContext = " + taskContext.toString() + ", isFinished = " + isFinished
                + ", priorityLevel = " + priorityLevel
                + ", timeEntity = " + timeEntity.toString() + ", taskRepeatEntity = " + repeatEntity.toString();
    }
}
