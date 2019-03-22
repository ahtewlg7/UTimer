package ahtewlg7.utimer.db.converter;


import com.alibaba.fastjson.JSON;

import org.greenrobot.greendao.converter.PropertyConverter;

import ahtewlg7.utimer.entity.w5h2.W5h2What;

/**
 * Created by lw on 2016/9/6.
 */
public class W5h2WhatConverter implements PropertyConverter<W5h2What,String> {
    public static final String TAG = W5h2WhatConverter.class.getSimpleName();

    @Override
    public W5h2What convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue, W5h2What.class);
    }

    @Override
    public String convertToDatabaseValue(W5h2What entityProperty) {
        return JSON.toJSONString(entityProperty);
    }
}
