package ahtewlg7.utimer.common;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.enumtype.ActState;
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
        Optional<W5h2When> whenOptional = getWhen(raw);
        if(!whenOptional.isPresent())
            return Optional.absent();
        BaseW5h2Entity w5h2Entity = new BaseW5h2Entity();
        w5h2Entity.setWhen(whenOptional.get());

        GtdActionBuilder builder  = new GtdActionBuilder()
                                    .setW5h2Entity(w5h2Entity)
                                    .setCreateTime(DateTime.now())
                                    .setDetail(raw)
                                    .setUuid(new IdAction().getUUId());
        Optional<String> keyWords = getTitle(raw);
        if(keyWords.isPresent())
            builder.setTitle(keyWords.get());
        GtdActionEntity gtdActionEntity = builder.build();
        gtdActionEntity.setActionState(ActState.MAYBE);
        gtdActionEntity.setLastModifyTime(DateTime.now());
        gtdActionEntity.setLastAccessTime(DateTime.now());
        return Optional.of(gtdActionEntity);
    }

    private Optional<W5h2When> getWhen(String raw){
        List<DateTime> timeList = nlpAction.toSegTimes(raw);
        if(timeList == null || timeList.size() == 0)
            return Optional.absent();
        W5h2When when = new W5h2When();
        when.setCreateTime(DateTime.now());
        when.setWorkTime(timeList);
        return Optional.of(when);
    }

    private Optional<String> getTitle(String raw){
        List<String> keyWords = nlpAction.getKeyWords(raw);
        if(keyWords == null || keyWords.size() == 0)
            return Optional.absent();
        StringBuilder title = new StringBuilder();
        for(String keyword : keyWords){
            title.append(keyword).append(",");
        }
        return Optional.of(title.toString());
    }
}
