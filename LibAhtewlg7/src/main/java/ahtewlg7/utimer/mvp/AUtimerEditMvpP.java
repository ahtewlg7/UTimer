package ahtewlg7.utimer.mvp;


import android.support.annotation.NonNull;

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
import ahtewlg7.utimer.enumtype.EditMode;
import ahtewlg7.utimer.enumtype.ElementEditType;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.MySimpleObserver;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2018/10/20.
 */
public abstract class AUtimerEditMvpP<T extends AUtimerEntity> {
    public static final String TAG = AUtimerEditMvpP.class.getSimpleName();

    public static final int INIT_POSITION = -1;

    protected abstract IUtimerEditMvpM getEditMvpM(AUtimerEntity utimerEntity);

    protected int preEditPosition = INIT_POSITION;
    protected boolean isChangeSaved;

    protected Disposable loadDisposable;
    protected Disposable insertDisplose;
    protected Disposable modifyDispose;

    protected AUtimerEntity utimerEntity;
    protected List<EditElement> editElementList;

    protected IUtimerEditMvpV editMvpV;
    protected IUtimerEditMvpM editMvpM;
    protected EditMementoOriginator mdMementoOriginator;
    protected EditMementoCaretaker mdMementoCaretaker;

    public AUtimerEditMvpP(AUtimerEntity utimerEntity , IUtimerEditMvpV editMvpV) {
        this.utimerEntity   = utimerEntity;
        this.editMvpV       = editMvpV;
        editMvpM            = getEditMvpM(utimerEntity);
    }

    public boolean ifEditElementEmpty(){
        return editElementList == null || editElementList.isEmpty();
    }

    public void onEditViewClick(int position){
        Logcat.i(TAG,"onEditViewClick position = " + position + ",preEditPosition = " + preEditPosition);

        if(preEditPosition != position && editMvpV != null) {
            editMvpV.onEditMode(preEditPosition, EditMode.OFF, getEditElement(preEditPosition));
            editMvpV.onEditMode(position, EditMode.ON, getEditElement(position));
        }
        preEditPosition = position;
    }

    public void toLoadTxt(){
        if(utimerEntity.getAttachFile() == null || !utimerEntity.getAttachFile().ifValid()){
            Logcat.i(TAG,"toLoadTxt : attachFile is not valid , so cancel");
            initElementList();
            editMvpV.onLoadEnd(editElementList);

            /*if(mdMementoCaretaker == null)
                initMemento();*/
//            editMvpV.onLoadEnd(mdMementoOriginator.getMdElementList());
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
//                        initMemento(editElements);
//                        editMvpV.onLoadEnd(mdMementoOriginator.getMdElementList());
                        if(editElements.size() > 0)
                            editElementList = editElements;
                        else
                            initElementList();
                        editMvpV.onLoadEnd(editElementList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logcat.i(TAG,"toLoadTxt err");
                        editMvpV.onLoadErr(e);
                    }
                });
    }

    public void toModify(final int index, Observable<String> rawTxtRx){
        toEdit(index, ElementEditType.MODIFY, rawTxtRx.filter(new Predicate<String>() {
            @Override
            public boolean test(String s) throws Exception {
                return !s.equals(editElementList.get(index).getMdCharSequence().toString())
                        && !s.equals(editElementList.get(index).getRawText());
            }
        }));
    }

    protected void toEdit(final int index, final ElementEditType editType,
                          @NonNull Observable<String> rawTxtRx){
        rawTxtRx.map(new Function<String, EditElement>() {
                    @Override
                    public EditElement apply(String s) throws Exception {
                        Logcat.i(TAG,"toEdit rawText : " + s);
                        EditElement element = new EditElement(s);
                        element.setMdCharSequence(editMvpM.toParseRaw(s));
                        return element;
                    }
                })
                .compose(((RxFragment)editMvpV.getRxLifeCycleBindView()).<EditElement>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleObserver<EditElement>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        super.onSubscribe(d);
                        if(editType == ElementEditType.INSERT)
                            insertDisplose = d;
                        else if(editType == ElementEditType.MODIFY)
                            modifyDispose = d;
                        editMvpV.onParseStart();
                    }

                    @Override
                    public void onNext(EditElement element) {
                        super.onNext(element);
                        isChangeSaved = false;
                        editElementList.set(index, element);
                        editMvpV.onParseSucc(index, element);
                        preEditPosition = INIT_POSITION;
                        /*Optional<EditMementoBean>  mementoOptional = mdMementoOriginator.createMemento(index, editType, parseedTxt);
                        if(mementoOptional.isPresent()) {
                            mdMementoCaretaker.saveMemento(mementoOptional.get());
                            editMvpV.onParseSucc(mementoOptional.get());
                        }*/
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        editMvpV.onParseErr(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        editMvpV.onParseEnd();
                    }
                });
    }

    public void toDel(int fromIndex, int toIndex){
        editMvpV.onParseStart();
        if(toIndex < fromIndex || fromIndex < 0 || mdMementoOriginator == null || fromIndex > mdMementoOriginator.getMdElementList().size()){
            editMvpV.onParseErr(new IndexOutOfBoundsException());
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

    public void toFinishEdit(){
        if(isChangeSaved || editElementList == null || editElementList.size() == 0){
            Logcat.i(TAG,"toFinishEdit cancel");
            return;
        }
        Logcat.i(TAG,"toFinishEdit");
        editMvpM.toSave(Flowable.fromIterable(editElementList))
                .compose(((RxFragment) editMvpV.getRxLifeCycleBindView()).<Boolean>bindUntilEvent(FragmentEvent.DESTROY))
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
                        isChangeSaved = true;
                    }
                });
    }


    public void cancelLoadTx(){
        if(loadDisposable != null && !loadDisposable.isDisposed())
            loadDisposable.isDisposed();
        loadDisposable = null;
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
            editMvpV.onRestoreEnd(mementoOptional.get());
            return true;
        }
        return false;
    }

    protected Optional<EditElement> getEditElement(int index){
        EditElement editElement = null;
        try{
            if(editElementList != null)
                editElement = editElementList.get(index);
        }catch (Exception e){}
        return Optional.fromNullable(editElement);
    }
    protected void initElementList(){
        if(editElementList == null)
            editElementList = Lists.newArrayList();
        editElementList.add(new EditElement(""));
    }

    public interface IUtimerEditMvpM{
        public Flowable<EditElement> toLoadTxt();
        public CharSequence toParseRaw(@NonNull String rawTxt);
        public Flowable<Boolean> toSave(@NonNull Flowable<EditElement> elementObservable);
    }

    public interface IUtimerEditMvpV extends IRxLifeCycleBindView{
        public void onEditMode(int position, @NonNull EditMode editMode, @NonNull Optional<EditElement> editElementOptional);

        public void onLoadStart();
        public void onLoadErr(Throwable e);
        public void onLoadEnd(List<EditElement> mdElementList);

        public void onParseStart();
        public void onParseErr(Throwable e);
        public void onParseSucc(int index, EditElement editElement);
        public void onParseEnd();

        public void onRestoreEnd(EditMementoBean MdMementoBean);

        public void onSaveStart();
        public void onSaveErr(Throwable e);
        public void onSaveEnd();
    }
}