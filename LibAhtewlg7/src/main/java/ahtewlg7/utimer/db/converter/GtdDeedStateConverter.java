package ahtewlg7.utimer.db.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.enumtype.DeedState;

/**
 * Created by lw on 2016/9/6.
 */
public class GtdDeedStateConverter implements PropertyConverter<DeedState,Integer> {

    @Override
    public DeedState convertToEntityProperty(Integer databaseValue) {
        return DeedState.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(DeedState entityProperty) {
        return entityProperty.ordinal();
    }
}
