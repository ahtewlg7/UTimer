package ahtewlg7.utimer.db.converter;


import android.support.annotation.NonNull;

import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2016/9/6.
 */
public class GtdTypeConverter implements PropertyConverter<GtdType,Integer> {
    public static final String TAG = GtdTypeConverter.class.getSimpleName();

    @Override
    public GtdType convertToEntityProperty(@NonNull Integer databaseValue) {
        return GtdType.values()[databaseValue];
    }

    @Override
    public Integer convertToDatabaseValue(@NonNull GtdType entityProperty) {
        return entityProperty.ordinal();
    }
}
