package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;
import com.hankcs.hanlp.seg.common.Term;

import org.joda.time.DateTime;
import org.joda.time.DateTimeComparator;

import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.context.Contact;
import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.entity.w5h2.W5h2Who;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.nlp.NlpAction;

/**
 * Created by lw on 2019/3/14.
 */
public class GtdActParser {

    public GtdActParser(){
    }

    public Optional<GtdActionEntity> toParseAction(String raw){
        if(TextUtils.isEmpty(raw))
            return Optional.absent();
        Optional<List<DateTime>> timeElementOptional = getTimeElement(raw);
        if(!timeElementOptional.isPresent())
            return Optional.absent();

        GtdActionBuilder builder  = new GtdActionBuilder()
                                    .setCreateTime(DateTime.now())
                                    .setWarningTimeList(timeElementOptional.get())
                                    .setDetail(raw)
                                    .setTitle(raw)
                                    .setUuid(new IdAction().getUUId());
        GtdActionEntity gtdActionEntity = builder.build();
        gtdActionEntity.setActionState(ActState.MAYBE);
        gtdActionEntity.setLastModifyTime(DateTime.now());
        gtdActionEntity.setLastAccessTime(DateTime.now());
        return Optional.of(gtdActionEntity);
    }
    public Optional<BaseW5h2Entity> toParseW5h2(String raw){
        BaseW5h2Entity w5h2Entity = new BaseW5h2Entity();
        List<Term> termList = NlpAction.getInstance().toSegAllTerm(raw);
        for(Term term : termList)
            toHandleW5h2(w5h2Entity, term);
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

    //todo
    public List<String> toSegTemp(String raw){
        return NlpAction.getInstance().getKeyWords(raw);
    }

    private void toHandleW5h2(BaseW5h2Entity w5h2Entity, Term term){
        boolean result = toHandleWho(w5h2Entity, term);
        if(!result)
            result = toHandleWhen(w5h2Entity, term);
        /*if(result)
            //todo*/
    }

    private boolean toHandleWhen(BaseW5h2Entity w5h2Entity, Term term){
        if(w5h2Entity == null || term == null)
            return false;
        return true;
    }
    private boolean toHandleWho(BaseW5h2Entity w5h2Entity, Term term){
        if(w5h2Entity == null || term == null)
            return false;
        Optional<Contact> contactOptional = toContact(term);
        if(!contactOptional.isPresent())
            return false;
        W5h2Who who = w5h2Entity.getWho();
        if(who == null)
            who = new W5h2Who();
        who.addContact(contactOptional.get());
        return true;
    }

    private Optional<Contact> toContact(@NonNull Term term){
        if(NlpAction.getInstance().isPerson(term))
            return Optional.of(new Contact(term.word));
        return Optional.absent();
    }

    private Optional<List<DateTime>> getTimeElement(String raw){
        List<DateTime> timeList = NlpAction.getInstance().toSegTimes(raw);
        if(timeList == null || timeList.isEmpty())
            return Optional.absent();
        Collections.sort(timeList,DateTimeComparator.getInstance());
        return Optional.of(timeList);
    }
}
