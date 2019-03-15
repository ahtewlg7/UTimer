package ahtewlg7.utimer.nlp;

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

    public List<String> getKeyWords(String raw){
        return hanlpAction.getKeyWords(raw);
    }
}
