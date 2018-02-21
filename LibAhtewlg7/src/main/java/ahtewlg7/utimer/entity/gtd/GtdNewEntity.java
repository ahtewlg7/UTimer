package ahtewlg7.utimer.entity.gtd;

import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/9/27.
 */

public class GtdNewEntity extends AGtdEntity{
    public static final String TAG = GtdNewEntity.class.getSimpleName();

    public GtdNewEntity(){
        super();
    }

    public GtdType getTaskType(){
        return GtdType.NEW;
    }
}
