package ahtewlg7.utimer.db.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.enumtype.GtdActionType;

/**
 * Created by lw on 2016/9/6.
 */
public class GtdActionTypeConverter implements PropertyConverter<GtdActionType,Integer> {
    public static final String TAG = GtdActionTypeConverter.class.getSimpleName();

    @Override
    public GtdActionType convertToEntityProperty(Integer databaseValue) {
        return GtdActionType.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(GtdActionType entityProperty) {
        return entityProperty.ordinal();
    }
}
