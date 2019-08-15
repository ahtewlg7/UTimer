package ahtewlg7.utimer.db.converter;


import com.snatik.storage.helpers.SizeUnit;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by lw on 2016/9/6.
 */
public class SizeUnitConverter implements PropertyConverter<SizeUnit,Long> {
    public static final long SIZE_UNIT_B             = 1;
    public static final long SIZE_UNIT_KB            = 1024;
    public static final long SIZE_UNIT_MB            = 1024 * 1024;
    public static final long SIZE_UNIT_GB            = 1024 * 1024 * 1024;
    public static final long SIZE_UNIT_TB            = 1024 * 1024 * 1024 * 1024;

    @Override
    public SizeUnit convertToEntityProperty(Long databaseValue) {
        if(databaseValue == null)
            return null;
        SizeUnit tmpUnit = null;
        if(databaseValue == SIZE_UNIT_B)
            tmpUnit = SizeUnit.B;
        else if(databaseValue == SIZE_UNIT_KB)
            tmpUnit = SizeUnit.KB;
        else if(databaseValue == SIZE_UNIT_MB)
            tmpUnit = SizeUnit.MB;
        else if(databaseValue == SIZE_UNIT_GB)
            tmpUnit = SizeUnit.GB;
        else if(databaseValue == SIZE_UNIT_TB)
            tmpUnit = SizeUnit.TB;
        return tmpUnit;
    }

    @Override
    public Long convertToDatabaseValue(SizeUnit entityProperty) {
        if(entityProperty == null)
            return null;
        return entityProperty.inBytes();
    }
}
