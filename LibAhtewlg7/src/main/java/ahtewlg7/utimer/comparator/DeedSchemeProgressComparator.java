package ahtewlg7.utimer.comparator;


import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;

public class DeedSchemeProgressComparator extends ABaseIntComparator<DeedSchemeEntity> {
    @Override
    protected int getComparatorInt(DeedSchemeEntity deedSchemeEntity) {
        return deedSchemeEntity.getProgress();
    }
}
