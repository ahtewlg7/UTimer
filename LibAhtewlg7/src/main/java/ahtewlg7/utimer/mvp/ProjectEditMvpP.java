package ahtewlg7.utimer.mvp;

import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.NoteByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by lw on 2018/10/18.
 */
public class ProjectEditMvpP implements IUtimerEditMvpP{
    protected IProjectEditMvpV mvpV;
    protected ProjectEditMvpM mvpM;
    protected GtdProjectEntity entity;
    protected List<NoteEntity> noteEntityList;
    protected Disposable loadDisposable;

    public ProjectEditMvpP(IProjectEditMvpV mvpV, GtdProjectEntity entity) {
        this.mvpV       = mvpV;
        this.entity     = entity;
        mvpM            = new ProjectEditMvpM();
        noteEntityList  = Lists.newArrayList();
    }

    public void toLoadNote(){
        UTimerBusEvent busEvent = new UTimerBusEvent(GtdBusEventType.LOAD, entity);
        EventBusFatory.getInstance().getDefaultEventBus().post(busEvent);
    }
    public void toDelNote(NoteEntity entity){
        UTimerBusEvent busEvent = new UTimerBusEvent(GtdBusEventType.DELETE, entity);
        EventBusFatory.getInstance().getDefaultEventBus().post(busEvent);
    }
    public void onNoteLoaded(){
        mvpM.loadAllNote()
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<NoteEntity>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<NoteEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    noteEntityList.clear();
                    if(mvpV != null)
                        mvpV.onItemLoadStart();
                }

                @Override
                public void onNext(NoteEntity entity) {
                    super.onNext(entity);
                    noteEntityList.add(entity);
                    if(mvpV != null)
                        mvpV.onItemLoad(entity);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onItemLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        mvpV.onItemLoadEnd(noteEntityList);
                }
            });
    }

    public void onNoteCreated(NoteEntity entity) {
        noteEntityList.add(entity);
        if(mvpV != null)
            mvpV.resetView(noteEntityList);
    }

    public void onNoteEdited(int index, NoteEntity entity) {
        if(index > 0)
            noteEntityList.set(index - 1, entity);
        if(mvpV != null)
            mvpV.resetView(index, entity);
    }

    class ProjectEditMvpM{
        public Flowable<NoteEntity> loadAllNote() {
            return  NoteByUuidFactory.getInstance().getAllLifeEntity();
        }
    }

    public interface IProjectEditMvpV extends IRxLifeCycleBindView {
        public void resetView(List<NoteEntity> dataList);
        public void resetView(int index , NoteEntity t);

        public void onItemLoadStart();
        public void onItemLoad(NoteEntity data);
        public void onItemLoadErr(Throwable t);
        public void onItemLoadEnd(List<NoteEntity> alldata);

        public void onItemCreate(NoteEntity data);
        public void onItemEdit(NoteEntity data);
    }
}
