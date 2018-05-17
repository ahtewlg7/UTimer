package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import ahtewlg7.utimer.common.NoteEntityAction;
import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class NoteRecylerViewMvpP implements IRecyclerMvpP {
    public static final String TAG = NoteRecylerViewMvpP.class.getSimpleName();

    private List<INoteEntity> noteEntityList;
    private INoteRecylerViewMvpV noteRecylerViewMvpV;
    private INoteRecyclerViewMvpM noteRecyclerViewMvpM;

    public NoteRecylerViewMvpP(INoteRecylerViewMvpV noteRecylerViewMvpV){
        this.noteRecylerViewMvpV    = noteRecylerViewMvpV;
        noteEntityList              = Lists.newArrayList();
        noteRecyclerViewMvpM        = new NoteEntityAction();
    }

    public void toNewNote(){
        noteRecylerViewMvpV.onNoteNewStart();
    }

    public void onNoteNew(String noteId){
        noteRecyclerViewMvpM.loadEntity(Flowable.just(noteId))
            .compose(noteRecylerViewMvpV.getUiContext().<Optional<INoteEntity>>bindUntilEvent(FragmentEvent.DESTROY))
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Optional<INoteEntity>>(){
                @Override
                public void onNext(Optional<INoteEntity> noteEntity) {
                    super.onNext(noteEntity);
                    if(noteEntity.isPresent())
                        noteEntityList.add(noteEntity.get());
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(!noteEntityList.isEmpty())
                        noteRecylerViewMvpV.resetRecyclerView(noteEntityList);
                }
            });
    }

    @Override
    public void loadAllData() {
        noteRecyclerViewMvpM.loadAllEntity()
                .compose(noteRecylerViewMvpV.getUiContext().<Optional<INoteEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .filter(new Predicate<Optional<INoteEntity>>() {
                    @Override
                    public boolean test(Optional<INoteEntity> iNoteEntityOptional) throws Exception {
                        return iNoteEntityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<INoteEntity>, INoteEntity>() {
                    @Override
                    public INoteEntity apply(Optional<INoteEntity> iNoteEntityOptional) throws Exception {
                        return iNoteEntityOptional.get();
                    }
                })
                .sorted(Ordering.natural().onResultOf(new com.google.common.base.Function<INoteEntity, Comparable>() {
                    @Override
                    @ParametersAreNonnullByDefault
                    public Comparable apply(INoteEntity input) {
                        return input.getLastAccessTime();
                    }
                }).reverse())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<INoteEntity>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        noteRecylerViewMvpV.onRecyclerViewInitStart();
                    }

                    @Override
                    public void onNext(INoteEntity noteEntity) {
                        super.onNext(noteEntity);
                        noteEntityList.add(noteEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        noteRecylerViewMvpV.onRecyclerViewInitErr();
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(noteEntityList.isEmpty())
                            noteRecylerViewMvpV.onRecyclerViewInitEnd();
                        else
                            noteRecylerViewMvpV.initRecyclerView(noteEntityList);
                    }
                });

    }

    public interface INoteRecyclerViewMvpM extends IBaseRecyclerViewMvpM<INoteEntity>{

    }

    public interface INoteRecylerViewMvpV extends IRecyclerViewMvpV<INoteEntity>{
        public @NonNull RxFragment getUiContext();

        public void onNoteNewStart();
    }
}
