package ahtewlg7.utimer.gtd;

import android.text.TextUtils;

import com.google.common.base.Optional;
import com.hankcs.hanlp.seg.common.Term;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.context.ContactContext;
import ahtewlg7.utimer.entity.context.DeedContext;
import ahtewlg7.utimer.entity.context.PlaceContext;
import ahtewlg7.utimer.entity.context.TimeContext;
import ahtewlg7.utimer.entity.gtd.GtdDeedBuilder;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.entity.w5h2.W5h2What;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.entity.w5h2.W5h2Where;
import ahtewlg7.utimer.entity.w5h2.W5h2Who;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.nlp.NlpAction;

/**
 * Created by lw on 2019/3/14.
 */
public class GtdDeedParser {

    public GtdDeedParser(){
    }

    public Optional<GtdDeedEntity> toParseAction(String raw){
        if(TextUtils.isEmpty(raw))
            return Optional.absent();
        Optional<List<DateTime>> timeElementOptional = toParseTimeElement(raw);
        if(!timeElementOptional.isPresent())
            return Optional.absent();

        GtdDeedBuilder builder  = new GtdDeedBuilder()
                                    .setCreateTime(DateTime.now())
                                    .setWarningTimeList(timeElementOptional.get())
                                    .setDetail(raw)
                                    .setTitle(raw)
                                    .setUuid(new IdAction().getUUId());
        GtdDeedEntity gtdActionEntity = builder.build();
        gtdActionEntity.setDeedState(DeedState.MAYBE);
        gtdActionEntity.setLastModifyTime(DateTime.now());
        gtdActionEntity.setLastAccessTime(DateTime.now());
        return Optional.of(gtdActionEntity);
    }

    public Optional<List<DateTime>> toParseTimeElement(String raw){
        List<DateTime> timeList = NlpAction.getInstance().toSegTimes(raw);
        if(timeList == null || timeList.isEmpty())
            return Optional.absent();
        Collections.sort(timeList,DateTimeComparator.getInstance());
        return Optional.of(timeList);
    }

    public Optional<BaseW5h2Entity> toParseW5h2(String raw){
        BaseW5h2Entity w5h2Entity = new BaseW5h2Entity();
        List<Term> termList = NlpAction.getInstance().toSegAllTerm(raw);
        boolean result = false;
        for(Term term : termList) {
            boolean tmp = toHandleW5h2(w5h2Entity, term);
            result = result || tmp;
        }
        if(!result)
            return Optional.absent();
        return Optional.of(w5h2Entity);
    }
    public Optional<String> toParseKeyWords(String raw){
        List<String> keyWords = NlpAction.getInstance().getKeyWords(raw);
        if(keyWords == null || keyWords.size() == 0)
            return Optional.absent();
        StringBuilder title = new StringBuilder();
        for(String keyword : keyWords)
            title.append(keyword).append(",");
        return Optional.of(title.toString());
    }

    private boolean toHandleW5h2(BaseW5h2Entity w5h2Entity, Term term){
        boolean result = toHandleWho(w5h2Entity, term);
        if(!result)
            result = toHandleWhen(w5h2Entity, term);
        if(!result)
            result = toHandlePlace(w5h2Entity, term);
        if(!result)
            result = toHandleWhat(w5h2Entity, term);
        return result;
    }

    private boolean toHandleWhen(BaseW5h2Entity w5h2Entity, Term term){
        if(w5h2Entity == null || term == null)
            return false;
        if(!NlpAction.getInstance().isTime(term))
            return false;
        W5h2When when = w5h2Entity.getWhen();
        if(when == null)
            when = new W5h2When();
        when.addWorkTime(new TimeContext(term.word, NlpAction.getInstance().toSegTimes(term.word).get(0)));
        w5h2Entity.setWhen(when);
        return true;
    }

    private boolean toHandlePlace(BaseW5h2Entity w5h2Entity, Term term){
        if(w5h2Entity == null || term == null)
            return false;
        if(!NlpAction.getInstance().isPlace(term) && !NlpAction.getInstance().isOrganization(term))
            return false;
        W5h2Where where = w5h2Entity.getWhere();
        if(where == null)
            where = new W5h2Where();
        if(NlpAction.getInstance().isPlace(term))
            where.addPlace(new PlaceContext(term.word));
        else
            where.addPlace(new PlaceContext(term.word, true));
        w5h2Entity.setWhere(where);
        return true;
    }

    private boolean toHandleWho(BaseW5h2Entity w5h2Entity, Term term){
        if(w5h2Entity == null || term == null)
            return false;
        if(!NlpAction.getInstance().isPerson(term))
            return false;
        W5h2Who who = w5h2Entity.getWho();
        if(who == null)
            who = new W5h2Who();
        who.addContact(new ContactContext(term.word));
        w5h2Entity.setWho(who);
        return true;
    }
    private boolean toHandleWhat(BaseW5h2Entity w5h2Entity, Term term){
        if(w5h2Entity == null || term == null)
            return false;
        if(!NlpAction.getInstance().isDeed(term))
            return false;
        W5h2What what = w5h2Entity.getWhat();
        if(what == null)
            what = new W5h2What();
        what.addDeed(new DeedContext(term.word));
        w5h2Entity.setWhat(what);
        return true;
    }
}
