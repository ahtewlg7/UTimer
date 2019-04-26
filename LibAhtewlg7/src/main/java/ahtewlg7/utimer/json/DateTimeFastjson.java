package ahtewlg7.utimer.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;

import org.joda.time.DateTime;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by lw on 2019/3/22.
 */
public class DateTimeFastjson implements ObjectSerializer, ObjectDeserializer {

    @Override
    public  <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        Long time = parser.parseObject(Long.class);
        return (T)new DateTime(time);
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType) throws IOException {
        serializer.write(((DateTime)object).getMillis());
    }

    /*@Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        serializer.write(((DateTime)object).getMillis());
    }*/
}
