package ahtewlg7.utimer.nlp;

import com.hankcs.hanlp.seg.common.Term;

import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by lw on 2019/3/2.
 */
public class NlpAction {
    private HanlpAction hanlpAction;
    private TimeNlpAction timeNlpAction;

    public NlpAction(){
        hanlpAction   = new HanlpAction();
        timeNlpAction = new TimeNlpAction();

        hanlpAction.initNLP();
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
}
