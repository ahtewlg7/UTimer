package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.gtd.NoteEditAction;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by lw on 2018/10/18.
 */
public class NoteEditMvpP extends AUtimerTxtEditMvpP<NoteEntity> {
    public static final String TAG = NoteEditMvpP.class.getSimpleName();

    public PublishSubject<EditElement> editPublishSubject;

    public NoteEditMvpP(NoteEntity entity , INoteEditMvpV shorthandEditMvpV) {
        super(entity,shorthandEditMvpV);
        editPublishSubject = PublishSubject.create();
    }

    @Override
    protected IUtimerEditMvpM getEditMvpM(NoteEntity utimerEntity) {
        return new NoteEditMvpM((NoteEntity) utimerEntity);
    }

    class NoteEditMvpM implements IUtimerEditMvpM{
        private NoteEntity noteEntity;
        private NoteEditAction editAction;

        public NoteEditMvpM(NoteEntity noteEntity) {
            this.noteEntity = noteEntity;
            editAction      = new NoteEditAction(noteEntity);
        }

        @Override
        public Flowable<EditElement> toLoadTxt() {
            return editAction.toLoad().filter(new Predicate<Optional<EditElement>>() {
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
            return editAction.toParse(rawTxt);
        }

        @Override
        public Flowable<Boolean> toSave(Flowable<EditElement> elementObservable) {
            return elementObservable.doOnSubscribe(new Consumer<Subscription>() {
                            @Override
                            public void accept(Subscription subscription) throws Exception {
                                editAction.toSave("",false);//to clear txt first
                            }
                        })
                    .map(new Function<EditElement, Boolean>() {
                        @Override
                        public Boolean apply(EditElement editElement) throws Exception {
                            return editAction.toSave(editElement.getRawText(),true);
                        }
                    }).subscribeOn(Schedulers.io());
        }
    }

    public interface INoteEditMvpV extends IUtimerEditMvpV{
    }
}
