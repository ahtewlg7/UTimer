package ahtewlg7.utimer.nlp;

import android.support.annotation.NonNull;

import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by lw on 2019/3/2.
 */
public class NlpAction {
    private HanlpAction hanlpAction;
    private TimeNlpAction timeNlpAction;
    private static NlpAction instance;

    private NlpAction(){
        hanlpAction   = new HanlpAction();
        timeNlpAction = new TimeNlpAction();
    }

    public static NlpAction getInstance(){
        if(instance == null)
            instance = new NlpAction();
        return instance;
    }

    public void initNlp(){
//        hanlpAction.initNLP();//todo
        timeNlpAction.initNLP();
    }

    public List<DateTime> toSegTimes(String raw){
        return timeNlpAction.toParse(raw);
    }

    public List<Term> toSegAllTerm(String raw){
        return hanlpAction.toSegment(raw, true, true,true);
    }

    public List<String> getKeyWords(String raw){
        return hanlpAction.getKeyWords(raw);
    }

    public boolean isPerson(@NonNull Term term){
        return term.nature.startsWith(Nature.nr.toString());
    }
    public boolean isOrganization(@NonNull Term term){
        return term.nature.startsWith(Nature.ni.toString());
    }
    public boolean isPlace(@NonNull Term term){
        return !isPerson(term) && term.nature.startsWith(Nature.n.toString());//todo
    }
    public boolean isTime(@NonNull Term term){
        return term.nature.startsWith(Nature.t.toString());
    }
    public boolean isDeed(@NonNull Term term){
        return term.nature.startsWith(Nature.v.toString());
    }
}
