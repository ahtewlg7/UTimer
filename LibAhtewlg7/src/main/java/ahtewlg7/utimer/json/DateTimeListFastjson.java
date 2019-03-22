package ahtewlg7.utimer.json;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by lw on 2019/3/22.
 */
public class DateTimeListFastjson implements ObjectSerializer, ObjectDeserializer {

    @Override
    public  <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        String timeList = parser.parseObject(String.class);
        List<DateTime> dateTimeList = Lists.newArrayList();
        List<String> timeLists = Splitter.on(";").trimResults().omitEmptyStrings().splitToList(timeList);
        for(String timeS : timeLists){
            Long timeL = Long.parseLong(timeS);
            dateTimeList.add(new DateTime(timeL));
        }
        return (T)timeLists;
    }

    @Override
    public int getFastMatchToken() {
        return JSONToken.LITERAL_INT;
    }

    @Override
    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        StringBuilder builder = new StringBuilder();
        List<DateTime> timeList = (List<DateTime>)object;
        for(DateTime datTime :timeList)
            builder.append(datTime.getMillis()).append(";");
        serializer.out.writeString(builder.toString());
    }
}
