package ahtewlg7.utimer.mvp;


import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.common.GtdActParser;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.entity.md.EditMementoCaretaker;
import ahtewlg7.utimer.entity.md.EditMementoOriginator;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdActionCacheFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2018/10/20.
 */
public abstract class AUtimerTxtEditMvpP<T extends AUtimerEntity> implements IUtimerEditMvpP{
    public static final String TAG = AUtimerTxtEditMvpP.class.getSimpleName();

    protected abstract IUtimerEditMvpM getEditMvpM(T t);


    protected Disposable insertDisplose;
    protected Disposable modifyDispose;

    protected T t;

    protected GtdActParser gtdActParser;
    protected IUtimerEditMvpV editMvpV;
    protected IUtimerEditMvpM editMvpM;
    protected EditMementoOriginator mdMementoOriginator;
    protected EditMementoCaretaker mdMementoCaretaker;

    public AUtimerTxtEditMvpP(T t , IUtimerEditMvpV editMvpV) {
        this.t = t;
        this.editMvpV = editMvpV;
        editMvpM      = getEditMvpM(t);
        gtdActParser  = new GtdActParser();
    }

    public void toDel(int fromIndex, int toIndex){
//        editMvpV.onParseStart();
        if(toIndex < fromIndex || fromIndex < 0 || mdMementoOriginator == null || fromIndex > mdMementoOriginator.getMdElementList().size()){
//            editMvpV.onParseErr(new IndexOutOfBoundsException());
            return;
        }

        /*List<EditElement> delList = mdMementoOriginator.getMdElementList().subList(fromIndex, toIndex);
        Optional<EditMementoBean>  mementoOptional = mdMementoOriginator.createMemento(fromIndex, ElementEditType.DELETE, delList);
        if(mementoOptional.isPresent()) {
            mdMementoCaretaker.saveMemento(mementoOptional.get());
            editMvpV.onParseSucc(mementoOptional.get());
        }*/
    }

    public boolean toRedo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextRedo());
    }

    public boolean toUndo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextUndo());
    }

    public void toFinishEdit(@NonNull Flowable<EditElement> editElementRx){
        editMvpM.toSave(editElementRx)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Boolean>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        editMvpV.onSaveStart();
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        editMvpV.onSaveErr(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        editMvpV.onSaveEnd();
//                        isChangeSaved = true;
                    }
                });
    }
    public void toPostAction(@NonNull Flowable<EditElement> elementRx){
        elementRx.subscribeOn(Schedulers.computation())
            .subscribe(new MySafeSubscriber<EditElement>() {
                @Override
                public void onNext(EditElement editElement) {
                    super.onNext(editElement);
                    Optional<GtdActionEntity> gtdActionOptional = gtdActParser.toParseAction(editElement.getMdCharSequence().toString());
                    if(gtdActionOptional.isPresent()) {
                        gtdActionOptional.get().setDetail(editElement.getRawText());
                        GtdActionCacheFactory.getInstance().add(gtdActionOptional.get().getUuid(), gtdActionOptional.get());
                        EventBusFatory.getInstance().getDefaultEventBus().postSticky((GtdActionEntity) gtdActionOptional.get());
                    }
                }
            });
    }

    public void cancelInsert(){
        if(insertDisplose != null && !insertDisplose.isDisposed())
            insertDisplose.dispose();
        insertDisplose = null;
    }
    public void cancelModify(){
        if(modifyDispose != null && !modifyDispose.isDisposed())
            modifyDispose.dispose();
        modifyDispose = null;
    }

    protected void initMemento(){
        mdMementoCaretaker   = new EditMementoCaretaker();
        mdMementoOriginator  = new EditMementoOriginator();
    }
    protected void initMemento(List<EditElement> mdElementList){
        mdMementoCaretaker   = new EditMementoCaretaker();
        mdMementoOriginator  = new EditMementoOriginator(mdElementList);
    }

    protected boolean restoreEdit(Optional<EditMementoBean> mementoOptional){
        if(mementoOptional.isPresent()){
            mdMementoOriginator.restoreMemento(mementoOptional.get());
//            editMvpV.onRestoreEnd(mementoOptional.get());
            return true;
        }
        return false;
    }

    public interface IUtimerEditMvpM{
        public Flowable<Boolean> toSave(@NonNull Flowable<EditElement> elementObservable);
    }

    public interface IUtimerEditMvpV{
        public void onSaveStart();
        public void onSaveErr(Throwable e);
        public void onSaveEnd();
    }
}