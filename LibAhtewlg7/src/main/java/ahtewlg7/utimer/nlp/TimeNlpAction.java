package ahtewlg7.utimer.nlp;

import com.blankj.utilcode.util.Utils;
import com.google.common.collect.Lists;
import com.google.common.io.CharStreams;
import com.time.nlp.TimeNormalizer;
import com.time.nlp.TimeUnit;

import org.joda.time.DateTime;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import ahtewlg7.utimer.R;

/**
 * Created by lw on 2019/3/2.
 */
public class TimeNlpAction implements INlp {

    private InputStream inputStream;
    private TimeNormalizer timeNormalizer;

    @Override
    public void initNLP() {
        Pattern pattern = toLoadPattern();

        if(pattern != null)
            timeNormalizer = new TimeNormalizer(pattern);
    }

    public List<DateTime> toParse(String raw){
        TimeUnit[] timeUnites =  timeNormalizer.parse(raw);
        List<DateTime> dateTimeList = Lists.newArrayList();
        for(TimeUnit timeUnit : timeUnites)
            dateTimeList.add(new DateTime(timeUnit.getTime().getTime()));
        return dateTimeList;
    }

    private Pattern toLoadPattern(){
        Pattern pattern = null;
        try{
            inputStream = Utils.getApp().getResources().openRawResource(R.raw.regex);
            String regex = CharStreams.toString( new InputStreamReader( inputStream, "UTF-8" ) );
            pattern = pattern.compile(regex);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pattern;
    }
}
