package ahtewlg7.utimer.mvp;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.entity.md.EditMementoCaretaker;
import ahtewlg7.utimer.entity.md.EditMementoOriginator;
import ahtewlg7.utimer.enumtype.ElementEditType;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by lw on 2018/10/20.
 */
public abstract class AUtimerEditMvpP<T extends AUtimerEntity> {
    public static final String TAG = AUtimerEditMvpP.class.getSimpleName();

    protected abstract IUtimerEditMvpM initEditMvpM(AUtimerEntity utimerEntity);

    protected Disposable loadDisposable;
    protected Subscription insertSubscription;
    protected Subscription modifySubscription;
    protected AUtimerEntity utimerEntity;
    protected IUtimerEditMvpV editMvpV;
    protected IUtimerEditMvpM editMvpM;
    protected EditMementoOriginator mdMementoOriginator;
    protected EditMementoCaretaker mdMementoCaretaker;

    public AUtimerEditMvpP(AUtimerEntity utimerEntity , IUtimerEditMvpV editMvpV) {
        this.utimerEntity   = utimerEntity;
        this.editMvpV       = editMvpV;
        editMvpM            = initEditMvpM(utimerEntity);
    }

    public void toLoadTxt(){
        if(utimerEntity.getAttachFile() == null || !utimerEntity.getAttachFile().ifValid()){
            Logcat.i(TAG,"toLoadTxt : attachFile is not valid , so cancel");
            if(mdMementoCaretaker == null)
                initMemento();
            editMvpV.onLoadEnd(mdMementoOriginator.getMdElementList());
            return;
        }
        if(loadDisposable != null){
            Logcat.i(TAG,"toLoadTxt : is loading, so cancel");
            return;
        }

        editMvpM.toLoadTxt()
                .toList()
                .compose(((RxFragment)editMvpV.getRxLifeCycleBindView()).<List<EditElement>>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<EditElement>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logcat.i(TAG,"toLoadTxt start");
                        loadDisposable = d;
                        editMvpV.onLoadStart();
                    }

                    @Override
                    public void onSuccess(List<EditElement> editElements) {
                        Logcat.i(TAG,"toLoadTxt succ");
                        initMemento(editElements);
                        editMvpV.onLoadEnd(mdMementoOriginator.getMdElementList());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logcat.i(TAG,"toLoadTxt err");
                        editMvpV.onLoadErr(e);
                    }
                });
    }

    public void toInsert(int index, String rawTxt){
        toEdit(index, ElementEditType.INSERT, rawTxt);
    }
    public void toModify(int index, String rawTxt){
        toEdit(index, ElementEditType.MODIFY, rawTxt);
    }

    protected void toEdit(final int index, final ElementEditType editType, String rawTxt){
        if(TextUtils.isEmpty(rawTxt)){
            Logcat.i(TAG,"toEdit cancel");
            editMvpV.onActionCancel();
            return;
        }
        Logcat.i(TAG,"toEdit");
        editMvpM.toParseRaw(rawTxt)
                .compose(((RxFragment)editMvpV.getRxLifeCycleBindView()).<EditElement>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<EditElement>(){
                    List<EditElement> editElementList = Lists.newArrayList();

                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(editType == ElementEditType.INSERT)
                            insertSubscription = s;
                        else if(editType == ElementEditType.MODIFY)
                            modifySubscription = s;
                        editMvpV.onParseStart();
                        editElementList.clear();
                    }

                    @Override
                    public void onNext(EditElement editElement) {
                        super.onNext(editElement);
                        editElementList.add(editElement);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        editMvpV.onParseErr(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        Optional<EditMementoBean>  mementoOptional = mdMementoOriginator.createMemento(index, editType, editElementList);
                        if(mementoOptional.isPresent()) {
                            mdMementoCaretaker.saveMemento(mementoOptional.get());
                            editMvpV.onParseEnd(mementoOptional.get());
                        }
                    }
                });
    }

    public void toDel(int fromIndex, int toIndex){
        editMvpV.onParseStart();
        if(toIndex < fromIndex || fromIndex < 0 || mdMementoOriginator == null || fromIndex > mdMementoOriginator.getMdElementList().size()){
            editMvpV.onParseErr(new IndexOutOfBoundsException());
            return;
        }

        List<EditElement> delList = mdMementoOriginator.getMdElementList().subList(fromIndex, toIndex);
        Optional<EditMementoBean>  mementoOptional = mdMementoOriginator.createMemento(fromIndex, ElementEditType.DELETE, delList);
        if(mementoOptional.isPresent()) {
            mdMementoCaretaker.saveMemento(mementoOptional.get());
            editMvpV.onParseEnd(mementoOptional.get());
        }
    }

    public boolean toRedo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextRedo());
    }

    public boolean toUndo(){
        return mdMementoCaretaker != null && restoreEdit(mdMementoCaretaker.popNextUndo());
    }

    public void finishEdit(){
        if(mdMementoOriginator != null && mdMementoOriginator.getMdElementList().size() > 0) {
            editMvpM.toSave(Flowable.fromIterable(mdMementoOriginator.getMdElementList()))
                    .compose(((RxFragment) editMvpV.getRxLifeCycleBindView()).<Boolean>bindUntilEvent(FragmentEvent.DETACH))
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
                        }
                    });
        }
    }


    public void cancelLoadTx(){
        if(loadDisposable != null && !loadDisposable.isDisposed())
            loadDisposable.isDisposed();
        loadDisposable = null;
    }
    public void cancelInsert(){
        if(insertSubscription != null)
            insertSubscription.cancel();
        insertSubscription = null;
    }
    public void cancelModify(){
        if(modifySubscription != null )
            modifySubscription.cancel();
        modifySubscription = null;
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
            editMvpV.onRestoreEnd(mementoOptional.get());
            return true;
        }
        return false;
    }
    public interface IUtimerEditMvpM{
        public Flowable<EditElement> toLoadTxt();
        public Flowable<EditElement> toParseRaw(@NonNull String rawTxt);
        public Flowable<Boolean> toSave(@NonNull Flowable<EditElement> elementObservable);
    }

    public interface IUtimerEditMvpV extends IRxLifeCycleBindView{
        public void onLoadStart();
        public void onLoadErr(Throwable e);
        public void onLoadEnd(List<EditElement> mdElementList);

        public void onParseStart();
        public void onParseErr(Throwable e);
        public void onParseEnd(EditMementoBean MdMementoBean);

        public void onRestoreEnd(EditMementoBean MdMementoBean);

        public void onSaveStart();
        public void onSaveErr(Throwable e);
        public void onSaveEnd();

        public void onActionCancel();
    }
}