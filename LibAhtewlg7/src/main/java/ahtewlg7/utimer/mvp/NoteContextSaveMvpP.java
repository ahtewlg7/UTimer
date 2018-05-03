package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.INoteEntity;
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

    public void toSaveNoteContext(INoteEntity iNoteEntity){
        noteSaveMvpM.toSaveContext(iNoteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(aBoolean)
                        noteSaveMvpV.onNoteSaveSucc();
                    else
                        noteSaveMvpV.onNoteSaveFailed();
                }

                @Override
                public void onError(Throwable t) {
                    noteSaveMvpV.onNoteSaveErr(t);
                }

                @Override
                public void onComplete() {
                    noteSaveMvpV.onNoteSaveComplete();
                }
            });
    }

    public interface INoteSaveMvpV {
        public void onNoteSaveFailed();
        public void onNoteSaveSucc();
        public void onNoteSaveErr(Throwable e);
        public void onNoteSaveComplete();
    }

    public interface INoteSaveMvpM {
        public Flowable<Boolean> toSaveContext(INoteEntity noteEntity);

    }
}
