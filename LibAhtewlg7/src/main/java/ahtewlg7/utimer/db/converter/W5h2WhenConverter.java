package ahtewlg7.utimer.db.converter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.json.DateTimeFastjson;

/**
 * Created by lw on 2016/9/6.
 */
public class W5h2WhenConverter implements PropertyConverter<W5h2When,String> {
    public W5h2WhenConverter(){
        SerializeConfig.getGlobalInstance().put(DateTime.class, new DateTimeFastjson());
        ParserConfig.getGlobalInstance().putDeserializer(DateTime.class, new DateTimeFastjson());
    }

    @Override
    public W5h2When convertToEntityProperty(String databaseValue) {
        return JSON.parseObject(databaseValue, W5h2When.class);
    }

    @Override
    public String convertToDatabaseValue(W5h2When entityProperty) {
        return JSON.toJSONString(entityProperty);
    }
}
