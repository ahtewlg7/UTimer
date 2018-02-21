package ahtewlg7.utimer.entity.gtd;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.TaskStepBean;

/**
 * Created by lw on 2017/10/24.
 */

public abstract class AGtdTaskEntity extends AGtdEntity {
    public static final String TAG = AGtdTaskEntity.class.getSimpleName();

    private int priority;
    private int completedPercent;
    private int archivedPercent;

    private boolean done;

    private DateTime startedDateTime;
    private DateTime deadlineDateTime;
    private DateTime completedDateTime;
    private DateTime abandonedDateTime;
    private DateTime beginWorkDateTime;
    private DateTime endWorkDateTime;

    private List<String> noteIdList;

    private List<AGtdEntity> subTaskEntity;

    private List<TaskStepBean> taskStepList;

    public AGtdTaskEntity(){
        super();
        taskStepList = new ArrayList<TaskStepBean>();
    }

    public boolean addNoteEntity(NoteEntity noteEntity) {
        if(noteIdList == null)
            noteIdList = new ArrayList<String>();
        return !noteIdList.contains(noteEntity.getId()) && noteIdList.add(noteEntity.getId());
    }

    public boolean removeNoteEntity(NoteEntity noteEntity){
        return noteIdList != null && noteIdList.remove(noteEntity.getId());
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getCompletedPercent() {
        return completedPercent;
    }

    public void setCompletedPercent(int completedPercent) {
        this.completedPercent = completedPercent;
    }

    public int getArchivedPercent() {
        return archivedPercent;
    }

    public void setArchivedPercent(int archivedPercent) {
        this.archivedPercent = archivedPercent;
    }


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public DateTime getStartedDateTime() {
        return startedDateTime;
    }

    public void setStartedDateTime(DateTime startedDateTime) {
        this.startedDateTime = startedDateTime;
    }

    public DateTime getDeadlineDateTime() {
        return deadlineDateTime;
    }

    public void setDeadlineDateTime(DateTime deadlineDateTime) {
        this.deadlineDateTime = deadlineDateTime;
    }

    public DateTime getCompletedDateTime() {
        return completedDateTime;
    }

    public void setCompletedDateTime(DateTime completedDateTime) {
        this.completedDateTime = completedDateTime;
    }

    public DateTime getAbandonedDateTime() {
        return abandonedDateTime;
    }

    public void setAbandonedDateTime(DateTime abandonedDateTime) {
        this.abandonedDateTime = abandonedDateTime;
    }

    public DateTime getBeginWorkDateTime() {
        return beginWorkDateTime;
    }

    public void setBeginWorkDateTime(DateTime beginWorkDateTime) {
        this.beginWorkDateTime = beginWorkDateTime;
    }

    public DateTime getEndWorkDateTime() {
        return endWorkDateTime;
    }

    public void setEndWorkDateTime(DateTime endWorkDateTime) {
        this.endWorkDateTime = endWorkDateTime;
    }

    public List<String> getNoteEntityList() {
        return noteIdList;
    }

    public void setNoteEntityList(List<String> noteIdList) {
        this.noteIdList = noteIdList;
    }

    public void addTaskStep(TaskStepBean stepBean){
        taskStepList.add(stepBean);
    }
    public void removeTaskStep(TaskStepBean stepBean){
        taskStepList.remove(stepBean);
    }
    public List<TaskStepBean> getTaskStepList(){
        return taskStepList;
    }


    @Override
    public String toString() {
        StringBuilder builder =  new StringBuilder();
        String tmp = ", priority = " + priority + ", completedPercent = " + completedPercent + ", archivedPercent = " + archivedPercent;
        builder.append(super.toString());
        builder.append(tmp);
        if(startedDateTime != null){
            tmp = ", startedDateTime = " + startedDateTime.toString();
            builder.append(tmp);
        }
        if(deadlineDateTime != null){
            tmp = ", deadlineDateTime = " + deadlineDateTime.toString();
            builder.append(tmp);
        }
        if(completedDateTime != null){
            tmp = ", completedDateTime = " + completedDateTime.toString();
            builder.append(tmp);
        }
        if(abandonedDateTime != null){
            tmp = ", abandonedDateTime = " + abandonedDateTime.toString();
            builder.append(tmp);
        }
        if(beginWorkDateTime != null){
            tmp = ", beginWorkDateTime = " + beginWorkDateTime.toString();
            builder.append(tmp);
        }
        if(endWorkDateTime != null){
            tmp = ", endWorkDateTime = " + endWorkDateTime.toString();
            builder.append(tmp);
        }
        for(String noteId : noteIdList){
            String bean =  ",Note{" + noteId + "}";
            builder.append(bean);
        }
        for(TaskStepBean stepBean : taskStepList){
            String bean =  ",Task{" + stepBean.toString() + "}";
            builder.append(bean);
        }
        builder.append("}");
        return builder.toString();
    }

}
