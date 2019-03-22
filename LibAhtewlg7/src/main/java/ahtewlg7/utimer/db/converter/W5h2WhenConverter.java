package ahtewlg7.utimer.db.converter;


import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.entity.w5h2.W5h2When;

/**
 * Created by lw on 2016/9/6.
 */
public class W5h2WhenConverter implements PropertyConverter<W5h2When,String> {
    @Override
    public W5h2When convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue, W5h2When.class);
    }

    @Override
    public String convertToDatabaseValue(W5h2When entityProperty) {
        return JSON.toJSONString(entityProperty);
    }
}
