package ahtewlg7.utimer.db.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.enumtype.ActState;

/**
 * Created by lw on 2016/9/6.
 */
public class GtdActStateConverter implements PropertyConverter<ActState,Integer> {
    public static final String TAG = GtdActStateConverter.class.getSimpleName();

    @Override
    public ActState convertToEntityProperty(Integer databaseValue) {
        return ActState.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(ActState entityProperty) {
        return entityProperty.ordinal();
    }
}
