package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.gtd.GtdShortHandEditAction;
import ahtewlg7.utimer.view.md.MdEditText;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lw on 2018/10/18.
 */
public class ShorthandEditMvpP extends AUtimerEditMvpP<ShortHandEntity> {
    public static final String TAG = ShorthandEditMvpP.class.getSimpleName();

    public PublishSubject<EditElement> editPublishSubject;

    public ShorthandEditMvpP(ShortHandEntity shortHandEntity , IShorthandEditMvpV shorthandEditMvpV) {
        super(shortHandEntity,shorthandEditMvpV);
        editPublishSubject = PublishSubject.create();
    }

    public void toWatchText(MdEditText editText, EditElement editElement){

    }

    @Override
    protected IUtimerEditMvpM getEditMvpM(AUtimerEntity utimerEntity) {
        return new ShorthandEditMvpM((ShortHandEntity) utimerEntity);
    }


    class ShorthandEditMvpM implements IUtimerEditMvpM{
        private ShortHandEntity shortHandEntity;
        private GtdShortHandEditAction shortHandEditAction;

        public ShorthandEditMvpM(ShortHandEntity shortHandEntity) {
            this.shortHandEntity = shortHandEntity;
            shortHandEditAction = new GtdShortHandEditAction(shortHandEntity);
        }

        @Override
        public Flowable<EditElement> toLoadTxt() {
            return shortHandEditAction.toLoad().filter(new Predicate<Optional<EditElement>>() {
                        @Override
                        public boolean test(Optional<EditElement> editElementOptional) throws Exception {
                            return editElementOptional.isPresent();
                        }
                    })
                    .map(new Function<Optional<EditElement>, EditElement>() {
                        @Override
                        public EditElement apply(Optional<EditElement> editElementOptional) throws Exception {
                            return editElementOptional.get();
                        }
                    });
        }

        @Override
        public CharSequence toParseRaw(@NonNull String rawTxt) {
            return shortHandEditAction.toParse(rawTxt);
        }

        @Override
        public Flowable<Boolean> toSave(Flowable<EditElement> elementObservable) {
            return elementObservable.doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                shortHandEditAction.toSave("",false);//to clear txt first
                            }
                        })
                    .map(new Function<EditElement, Boolean>() {
                        @Override
                        public Boolean apply(EditElement editElement) throws Exception {
                            return shortHandEditAction.toSave(editElement.getRawText().trim(),true);
                        }
                    }).subscribeOn(Schedulers.io());
        }
    }

    public interface IShorthandEditMvpV extends IUtimerEditMvpV{
    }
}
