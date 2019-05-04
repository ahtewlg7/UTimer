package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.gtd.GtdProjectListAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2018/12/9.
 */
public class ProjectListMvpP{
    public static final String TAG = ProjectListMvpP.class.getSimpleName();

    private List<GtdProjectEntity> projectList;

    private IProjectListMvpV mvpV;
    private ProjectListMvpM mvpM;

    public ProjectListMvpP(IProjectListMvpV mvpV) {
        this.mvpV       = mvpV;
        mvpM            = new ProjectListMvpM();
        projectList     = Lists.newArrayList();
    }

    public void toLoadAllItem() {
        Logcat.i(TAG,"toLoadAllItem");
        mvpM.loadAllEntity()
                .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<GtdProjectEntity>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<GtdProjectEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        projectList.clear();
                        if(mvpV != null)
                            mvpV.onItemLoadStart();
                    }

                    @Override
                    public void onNext(GtdProjectEntity entity) {
                        super.onNext(entity);
                        Logcat.i(TAG,"toLoadAllItem onNext : " + entity.toString());
                        projectList.add(entity);
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
                            mvpV.onItemLoadEnd(projectList);
                    }
                });
    }

    public void toDeleteItem(@NonNull Flowable<GtdProjectEntity>  entityRx) {
        /*entityRx.subscribeOn(Schedulers.io())
                .doOnNext(new Consumer<ShortHandEntity>() {
                    @Override
                    public void accept(ShortHandEntity entity) throws Exception {
                        boolean result = projectListMvpM.toDelEntity(entity);
                        Logcat.i(TAG,"toDeleteItem " + entity.getTitle() + " : " + result);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<ShortHandEntity>() {
                    @Override
                    public void onNext(ShortHandEntity entity) {
                        super.onNext(entity);

                        boolean ifExited = entity.getAttachFile().ifValid();
                        if (mvpV != null && !ifExited) {
                            int index  = projectList.indexOf(entity);
//                            projectList.remove(entity);
                            mvpV.onDeleteSucc(index, entity);
                        }else if (mvpV != null)
                            mvpV.onDeleteFail(entity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mvpV != null)
                            mvpV.onDeleteErr(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (mvpV != null)
                            mvpV.onDeleteEnd();
                    }
                });*/
    }

    public void onItemCreated(GtdProjectEntity entity) {
        projectList.add(entity);
        if(mvpV != null)
            mvpV.resetView(projectList);
    }

    public void onItemEdited(int index, GtdProjectEntity entity) {
        projectList.set(index, entity);
        if(mvpV != null)
            mvpV.resetView(index, entity);
    }

    class ProjectListMvpM implements IAllItemListMvpM<GtdProjectEntity> {
        private GtdProjectListAction projectListAction;

        ProjectListMvpM(){
            projectListAction = new GtdProjectListAction();
        }

        @Override
        public Flowable<GtdProjectEntity> loadAllEntity() {
            return projectListAction.loadAllEntity();
        }

        @Override
        public boolean toDelEntity(GtdProjectEntity entity) {
            /*if(entity != null){
                DelBusEvent delBusEvent = new DelBusEvent(entity.getId(),entity.getAttachFile().getAbsPath().get());
                EventBusFatory.getInstance().getDefaultEventBus().postSticky(delBusEvent);
            }*/

            boolean result = false;
            try{
                if(entity != null && entity.getAttachFile().ifValid())
                    result = FileUtils.deleteFile(entity.getAttachFile().getFile());
            }catch (Exception e){
                e.printStackTrace();
            }
            return result;
        }
    }

    public interface IProjectListMvpV extends IAllItemListMvpV<GtdProjectEntity> {
    }
}
