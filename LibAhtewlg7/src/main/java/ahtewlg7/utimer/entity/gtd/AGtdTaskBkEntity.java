package ahtewlg7.utimer.entity.gtd;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.RepeatEntity;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/10/24.
 */

public abstract class AGtdTaskBkEntity extends AGtdEntity {
    public static final String TAG = AGtdTaskBkEntity.class.getSimpleName();

    private int priority;
    private int completedPercent;
    private int archivedPercent;

    private boolean referenced;
    private boolean incubated;

    private GtdType preTaskType;

    private DateTime startedDateTime;
    private DateTime deadlineDateTime;
    private DateTime completedDateTime;
    private DateTime abandonedDateTime;
    private DateTime beginWorkDateTime;
    private DateTime endWorkDateTime;

    private RepeatEntity taskRepeatEntity;
    private NoteEntity noteEntity;

    private List<AGtdEntity> subTaskEntity;

    public AGtdTaskBkEntity(){
        super();
    }


    @Override
    public String toString() {
        return super.toString() + ", preTaskType = " + preTaskType.name()
                + ", priority = " + priority + ", completedPercent = " + completedPercent + ", archivedPercent = " + archivedPercent
                + ", startedDateTime = " + startedDateTime.toString()
                + ", deadlineDateTime = " + deadlineDateTime.toString() + ", completedDateTime = " + completedDateTime.toString()
                + ", abandonedDateTime = " + abandonedDateTime.toString() + ", beginWorkDateTime = " + beginWorkDateTime.toString()
                + ", endWorkDateTime = " + endWorkDateTime.toString() + ", taskRepeatEntity = " + taskRepeatEntity.toString()
                + ", noteEntity = " + noteEntity.toString() + ", parentTaskEntity = " + parentTaskEntity.toString();
    }

}
