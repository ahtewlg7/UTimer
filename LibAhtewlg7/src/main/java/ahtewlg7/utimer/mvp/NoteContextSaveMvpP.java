package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class NoteContextSaveMvpP {
    public static final String TAG = NoteContextSaveMvpP.class.getSimpleName();

    private INoteSaveMvpV noteSaveMvpV;
    private INoteSaveMvpM noteSaveMvpM;

    public NoteContextSaveMvpP(@NonNull INoteSaveMvpV noteSaveMvpV){
        this.noteSaveMvpV = noteSaveMvpV;
        noteSaveMvpM      = new NoteEntityAction();
    }

    public void toSaveNoteContext(final NoteEntity noteEntity){
        noteSaveMvpM.toSaveContext(noteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(aBoolean)
                        noteSaveMvpV.onNoteSaveSucc(noteEntity);
                    else
                        noteSaveMvpV.onNoteSaveFailed(noteEntity);
                }

                @Override
                public void onError(Throwable t) {
                    noteSaveMvpV.onNoteSaveErr(noteEntity, t);
                }

                @Override
                public void onComplete() {
                    noteSaveMvpV.onNoteSaveComplete(noteEntity);
                }
            });
    }

    public interface INoteSaveMvpV {
        public void onNoteSaveFailed(NoteEntity noteEntity);
        public void onNoteSaveSucc(NoteEntity noteEntity);
        public void onNoteSaveErr(NoteEntity noteEntity, Throwable e);
        public void onNoteSaveComplete(NoteEntity noteEntity);
    }

    public interface INoteSaveMvpM {
        public Flowable<Boolean> toSaveContext(NoteEntity noteEntity);

    }
}
