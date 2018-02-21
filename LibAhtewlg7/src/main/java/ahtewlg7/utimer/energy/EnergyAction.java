package ahtewlg7.utimer.energy;

import android.support.annotation.NonNull;

import java.util.Comparator;

import ahtewlg7.utimer.comparator.DegreeLevelComparator;
import ahtewlg7.utimer.entity.degree.DegreeEntity;
import ahtewlg7.utimer.enumtype.ComparatorType;

/**
 * Created by lw on 2017/11/26.
 */

public class EnergyAction {
    public static final String TAG = EnergyAction.class.getSimpleName();

    public DegreeEntity getCurrEnergy(){
        return null;
    }

    public boolean isTaskOk(@NonNull DegreeEntity energyEntity){
        DegreeEntity curEnergyEntity = getCurrEnergy();
        if(curEnergyEntity == null)
            return true;
        Comparator<DegreeEntity> comparator = new DegreeLevelComparator<DegreeEntity>();
        int compareResult = comparator.compare(energyEntity, curEnergyEntity);
        return compareResult == ComparatorType.EQUAL.value() || compareResult == ComparatorType.GREATER.value();
    }
}
