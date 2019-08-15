package ahtewlg7.utimer.db.converter;


import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.entity.w5h2.W5h2HowMuch;

/**
 * Created by lw on 2016/9/6.
 */
public class W5h2HowMuchConverter implements PropertyConverter<W5h2HowMuch,String> {
    @Override
    public W5h2HowMuch convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue, W5h2HowMuch.class);
    }

    @Override
    public String convertToDatabaseValue(W5h2HowMuch entityProperty) {
        return JSON.toJSONString(entityProperty);
    }
}
