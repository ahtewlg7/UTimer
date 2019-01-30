package ahtewlg7.utimer.mvp;

import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.gtd.GtdProjectAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by lw on 2018/10/18.
 */
public class ProjectEditMvpP implements IUtimerEditMvpP{
    public static final String TAG = ProjectEditMvpP.class.getSimpleName();

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

    @Override
    public void toLoad() {
        toLoadNote();
    }

    public void toLoadNote(){
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
                    Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
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

    @Override
    public void toFinishEdit() {

    }

    public class ProjectEditMvpM{
        private GtdProjectAction projectAction;

        public Flowable<NoteEntity> loadAllNote() {
            return projectAction.loadAllNote();
        }
    }

    public interface IProjectEditMvpV extends IAllItemListMvpV<NoteEntity>{
    }
}
