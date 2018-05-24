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
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class NoteRecylerViewMvpP implements IRecyclerMvpP {
    public static final String TAG = NoteRecylerViewMvpP.class.getSimpleName();

    private List<NoteEntity> noteEntityList;
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

    public void toTrash(){

    }

    public void toGtdProject(NoteEntity noteEntity){

    }

    public void onNoteNew(String noteId){
        noteRecyclerViewMvpM.loadEntity(Flowable.just(noteId))
            .compose(noteRecylerViewMvpV.getUiContext().<Optional<NoteEntity>>bindUntilEvent(FragmentEvent.DESTROY))
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Optional<NoteEntity>>(){
                @Override
                public void onNext(Optional<NoteEntity> noteEntity) {
                    super.onNext(noteEntity);
                    if(noteEntity.isPresent())
                        noteEntityList.add(0, noteEntity.get());
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
                .compose(noteRecylerViewMvpV.getUiContext().<Optional<NoteEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .filter(new Predicate<Optional<NoteEntity>>() {
                    @Override
                    public boolean test(Optional<NoteEntity> noteEntityOptional) throws Exception {
                        return noteEntityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<NoteEntity>, NoteEntity>() {
                    @Override
                    public NoteEntity apply(Optional<NoteEntity> noteEntityOptional) throws Exception {
                        return noteEntityOptional.get();
                    }
                })
                .sorted(Ordering.natural().onResultOf(new com.google.common.base.Function<NoteEntity, Comparable>() {
                    @Override
                    @ParametersAreNonnullByDefault
                    public Comparable apply(NoteEntity input) {
                        return input.getLastAccessTime();
                    }
                }).reverse())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<NoteEntity>(){
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        noteEntityList.clear();
                        noteRecylerViewMvpV.onRecyclerViewInitStart();
                    }

                    @Override
                    public void onNext(NoteEntity noteEntity) {
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

    public interface INoteRecyclerViewMvpM extends IBaseRecyclerViewMvpM<NoteEntity>{

    }

    public interface INoteRecylerViewMvpV extends IRecyclerViewMvpV<NoteEntity>{
        public @NonNull RxFragment getUiContext();

        public void onNoteNewStart();
    }
}
