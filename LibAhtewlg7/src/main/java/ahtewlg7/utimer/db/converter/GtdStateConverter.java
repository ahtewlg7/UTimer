package ahtewlg7.utimer.db.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.enumtype.GtdState;

/**
 * Created by lw on 2016/9/6.
 */
public class GtdStateConverter implements PropertyConverter<GtdState,Integer> {
    public static final String TAG = GtdStateConverter.class.getSimpleName();

    @Override
    public GtdState convertToEntityProperty(Integer databaseValue) {
        return GtdState.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(GtdState entityProperty) {
        return entityProperty.ordinal();
    }
}
