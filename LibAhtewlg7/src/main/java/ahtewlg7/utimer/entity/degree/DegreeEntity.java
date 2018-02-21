package ahtewlg7.utimer.entity.degree;

import ahtewlg7.utimer.enumtype.DegreeLevel;

/**
 * Created by lw on 2017/11/29.
 */

public class DegreeEntity implements IDegreeLevelBean {
    public static final String TAG = DegreeEntity.class.getSimpleName();

    private DegreeLevel curDegreeLevel;

    @Override
    public DegreeLevel getDegreeLevel() {
        return curDegreeLevel;
    }

    @Override
    public void setDegreeLevel(DegreeLevel degreeLevel) {
        this.curDegreeLevel = degreeLevel;
    }

    @Override
    public String toString() {
        return "curDegreeLevel = " + curDegreeLevel;
    }
}
