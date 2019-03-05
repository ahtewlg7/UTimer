package ahtewlg7.utimer.nlp;

import android.text.TextUtils;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;

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

    public Optional<GtdActionEntity> toParseAction(String raw){
        if(TextUtils.isEmpty(raw))
            return Optional.absent();
        GtdActionEntity gtdActionEntity = new GtdActionBuilder().setTitle(raw).setTimeList(timeNlpAction.toParse(raw)).build();
        return Optional.of(gtdActionEntity);
    }
}
