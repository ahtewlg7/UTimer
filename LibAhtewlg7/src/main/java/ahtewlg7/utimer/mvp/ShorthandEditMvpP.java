package ahtewlg7.utimer.mvp;

import androidx.annotation.NonNull;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.gtd.GtdShortHandEditAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/10/18.
 */
public class ShorthandEditMvpP extends AUtimerTxtEditMvpP<ShortHandEntity> {
    public ShorthandEditMvpP(ShortHandEntity shortHandEntity , IUtimerEditMvpV shorthandEditMvpV) {
        super(shortHandEntity,shorthandEditMvpV);
    }

    @Override
    protected IUtimerEditMvpM getEditMvpM(ShortHandEntity utimerEntity) {
        return new ShorthandEditMvpM(utimerEntity);
    }

    class ShorthandEditMvpM implements IUtimerEditMvpM{
        private ShortHandEntity shortHandEntity;
        private GtdShortHandEditAction shortHandEditAction;

        public ShorthandEditMvpM(ShortHandEntity shortHandEntity) {
            this.shortHandEntity = shortHandEntity;
            shortHandEditAction = new GtdShortHandEditAction(shortHandEntity);
        }

        @Override
        public Flowable<Boolean> toSaveElement(@NonNull Flowable<EditElement> elementObservable) {
            if(editMvpV != null && !editMvpV.ifTxtChanged())
                return Flowable.just(true);
            return elementObservable.doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                shortHandEditAction.toSave("",false);//to clear txt first
                            }
                        })
                    .map(new Function<EditElement, Boolean>() {
                        @Override
                        public Boolean apply(EditElement editElement) throws Exception {
                            return shortHandEditAction.toSave(editElement.getRawText(),true);
                        }
                    })
                    .subscribeOn(Schedulers.io());
        }
    }
}
