package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.gtd.GtdActParser;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2019/5/3.
 */
public class ActionEditMvpP {
    private ActionMvpM mvpM;
    private IActEditMvpV mvpV;
    private GtdActionEntity actionEntity;

    public ActionEditMvpP(IActEditMvpV mvpV, GtdActionEntity actionEntity){
        this.mvpV           = mvpV;
        this.actionEntity   = actionEntity;
        mvpM                = new ActionMvpM();
    }

    public void toParseW5h2(){
        mvpM.toParseSeg(Flowable.just(actionEntity))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Optional<List<String>>>(){
                @Override
                public void onNext(Optional<List<String>> segOptional) {
                    super.onNext(segOptional);
                    if(mvpV != null && segOptional.isPresent()){
                        StringBuilder builder = new StringBuilder();
                        for(String tmp : segOptional.get())
                            builder.append(tmp).append("\n");
                        mvpV.onParseEnd(builder.toString());
                    }
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onParseErr(t);
                }

                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        mvpV.onParseStart();
                }
            });
    }

    class ActionMvpM{
        private GtdActParser gtdActParser;

        ActionMvpM(){
            gtdActParser        = new GtdActParser();
        }

        public Flowable<Optional<List<String>>> toParseSeg(@NonNull Flowable<GtdActionEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdActionEntity, Optional<List<String>>>() {
                @Override
                public Optional<List<String>> apply(GtdActionEntity actionEntity) throws Exception {
                    if(actionEntity == null || !actionEntity.ifValid())
                        return Optional.absent();
                    return Optional.of(gtdActParser.toSegTemp(actionEntity.getDetail().get()));
                }
            }).subscribeOn(Schedulers.computation());
        }
        public Flowable<Optional<BaseW5h2Entity>> toParseW5h2(@NonNull Flowable<GtdActionEntity> actionEntityRx){
            return actionEntityRx.map(new Function<GtdActionEntity, Optional<BaseW5h2Entity>>() {
                @Override
                public Optional<BaseW5h2Entity> apply(GtdActionEntity actionEntity) throws Exception {
                    if(actionEntity == null || !actionEntity.ifValid())
                        return Optional.absent();
                    return gtdActParser.toParseW5h2(actionEntity.getDetail().get());
                }
            }).subscribeOn(Schedulers.computation());
        }
    }
    public interface IActEditMvpV extends IRxLifeCycleBindView{
        public void onParseStart();
        public void onParseEnd(String detail);
        public void onParseErr(Throwable t);
    }
}
