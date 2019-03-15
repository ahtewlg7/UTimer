package ahtewlg7.utimer.common;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.nlp.NlpAction;

/**
 * Created by lw on 2019/3/14.
 */
public class GtdActParser {
    protected NlpAction nlpAction;

    public GtdActParser(){
        nlpAction     = new NlpAction();
    }

    public Optional<GtdActionEntity> toParseAction(String raw){
        if(TextUtils.isEmpty(raw))
            return Optional.absent();
        List<DateTime> timeList = nlpAction.toSegTimes(raw);
        if(timeList == null || timeList.size() == 0)
            return Optional.absent();
        GtdActionBuilder builder  = new GtdActionBuilder().setDetail(raw).setUuid(new IdAction().getUUId());
        Optional<String> keyWords = getActionTitle(raw);
        if(keyWords.isPresent())
            builder.setTitle(keyWords.get());
        GtdActionEntity gtdActionEntity = builder.build();
        return Optional.of(gtdActionEntity);
    }

    private Optional<String> getActionTitle(String raw){
        List<String> keyWords = nlpAction.getKeyWords(raw);
        if(keyWords == null || keyWords.size() == 0)
            return Optional.absent();
        StringBuilder title = new StringBuilder();
        for(String keyword : keyWords){
            title.append(keyword).append(";");
        }
        return Optional.of(title.toString());
    }
}
