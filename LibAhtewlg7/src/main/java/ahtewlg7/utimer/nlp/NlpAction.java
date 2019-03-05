package ahtewlg7.utimer.nlp;

import android.text.TextUtils;

import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

    public Observable<GtdActionEntity> toParseAction(String raw){
        if(TextUtils.isEmpty(raw))
            return Observable.empty();
        return Observable.just(raw).subscribeOn(Schedulers.computation())
                .map(new Function<String, GtdActionEntity>() {
                    @Override
                    public GtdActionEntity apply(String s) throws Exception {
                        return new GtdActionBuilder().setTitle(s).setTimeList(timeNlpAction.toParse(s)).build();
                    }
                });
    }
}
