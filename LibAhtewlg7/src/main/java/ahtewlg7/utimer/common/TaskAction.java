package ahtewlg7.utimer.common;

import java.util.UUID;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;

/**
 * Created by lw on 2017/10/1.
 */

public class TaskAction {
    public static final String TAG = TaskAction.class.getSimpleName();

    public AGtdEntity createNewTask(){
        String taskId = UUID.randomUUID().toString();
        return null;
    }
}
